package com.etrungpro.appshoppet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.etrungpro.appshoppet.models.Product;
import com.etrungpro.appshoppet.adapters.ProductAdapter;
import com.etrungpro.appshoppet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    TextView tvDanhMuc;
    RecyclerView rcvDanhMuc;
    ProductAdapter productAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initUI();

        String categoryName = getIntent().getStringExtra("categoryName");
        tvDanhMuc.setText(categoryName);
        productAdapter = new ProductAdapter(CategoryActivity.this, new ProductAdapter.IProductEvent() {
            @Override
            public void showDetail(Product product) {
                Intent intent = new Intent(CategoryActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("productId", product.getId());
                bundle.putString("productName", product.getName());
                bundle.putInt("productPrice", product.getPrice());
                bundle.putStringArrayList("productImages", product.getImg());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(CategoryActivity.this, 2);
        rcvDanhMuc.setLayoutManager(gridLayoutManager);
        rcvDanhMuc.setAdapter(productAdapter);
        getProductCategoryList(categoryName);
    }

    private void getProductCategoryList(String category) {
        ArrayList<Product> products = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .whereEqualTo("category", category)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product product = document.toObject(Product.class);
                                product.setId(document.getId());
                                products.add(product);
                            }
                            productAdapter.setData(products);
                        } else {
                            Toast.makeText(CategoryActivity.this, "False to get collection!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    void initUI() {
        tvDanhMuc = findViewById(R.id.txt_danhmuc);
        rcvDanhMuc = findViewById(R.id.rcv_danhmuc);
    }
}