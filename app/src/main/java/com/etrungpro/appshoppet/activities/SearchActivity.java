package com.etrungpro.appshoppet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.adapters.ProductAdapter;
import com.etrungpro.appshoppet.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    TextView txtSearchSize;
    RecyclerView rcvSearch;
    String searchValue;
    ArrayList<Product> productList;
    ProductAdapter productAdapter;
//    androidx.appcompat.widget.SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initUI();

        String previousSearch = getIntent().getStringExtra("searchValue");
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, new ProductAdapter.IProductEvent() {
            @Override
            public void showDetail(Product product) {
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("productName", product.getName());
                bundle.putInt("productPrice", product.getPrice());
                bundle.putStringArrayList("productImages", product.getImg());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchActivity.this, 2);
        rcvSearch.setLayoutManager(gridLayoutManager);
        rcvSearch.setAdapter(productAdapter);
        getSearchResult(previousSearch);

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                getSearchResult(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });

    }

    private void getSearchResult(String searchValue) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(searchValue.length() == 0) {
            db.collection("products")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        for(QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            productList.add(product);
                        }
                        productAdapter.setData(productList);
                    } else {
                        // Làm gì đó
                    }
                }
            });
        }
        else {
            db.collection("products")
                    .orderBy("name")
                    .startAt(searchValue).endAt(searchValue + "\uf8ff")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for(QueryDocumentSnapshot document : task.getResult()) {
                                    Product product = document.toObject(Product.class);
                                    productList.add(product);
                                }
                                productAdapter.setData(productList);
                            }
                        }
                    });
        }

    }

    private void initUI() {
        txtSearchSize = findViewById(R.id.result_size);
        rcvSearch = findViewById(R.id.rcv_search);
//        searchView = findViewById(R.id.search_view);
    }
}