package com.etrungpro.appshoppet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.adapters.CartAdapter;
import com.etrungpro.appshoppet.models.DetailCart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    String productId;
    int productPrice;
    int productQuality;
    int existPIndex = -1;
    ArrayList<DetailCart> products;
    ArrayList<String> selectList = new ArrayList<>();
    CartAdapter cartAdapter;
    RecyclerView rcvCart;
    TextView tvTongTien;
    TextView cartTitle;
    Button btnThanhToan;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initUI();
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        if(intent.getStringExtra("productPrice") != null) {
            productPrice = Integer.parseInt(intent.getStringExtra("productPrice"));
        }
        if(intent.getStringExtra("productQuality") != null) {
            productQuality = Integer.parseInt(intent.getStringExtra("productQuality"));
        }
        cartAdapter = new CartAdapter(CartActivity.this, new CartAdapter.ICartEvent() {
            @Override
            public void add(String productId) {
                selectList.add(productId);
                calTotalPrice();
            }

            @Override
            public void remove(String productId) {
                selectList.remove(productId);
                calTotalPrice();
            }

            @Override
            public void updateTotalPrice(DetailCart detailCart, int position) {
                products.set(position, detailCart);
                calTotalPrice();
            }

            @Override
            public void delete(int position) {
                products.remove(position);
                cartAdapter.setList(products);
                cartTitle.setText("Giỏ hàng (" + String.valueOf(products.size()) + ")");
                calTotalPrice();
            }

        });
        products = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                CartActivity.this, LinearLayoutManager.VERTICAL, false);
        rcvCart.setLayoutManager(linearLayoutManager);
        rcvCart.setVisibility(View.GONE);
        rcvCart.setAdapter(cartAdapter);
        getCartProductList();

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(products.size() == 0) {
                    Toast.makeText(CartActivity.this, "Không có sản phẩm nào cả", Toast.LENGTH_SHORT).show();
                    return ;
                }
                Intent intent = new Intent(CartActivity.this, EditOrderActivity.class);
                Bundle bundle = new Bundle();
                ArrayList<DetailCart> detailCarts = new ArrayList<>();
                for(DetailCart detailCart : products) {
                    for(String id : selectList) {
                        if(detailCart.getId().equals(id)) {
                            detailCarts.add(detailCart);
                            selectList.remove(id);
                            break;
                        }
                    }
                }
                bundle.putSerializable("detailCarts",detailCarts);
                bundle.putString("totalPrice", tvTongTien.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

    private void initUI() {
        rcvCart = findViewById(R.id.rcv_cart);
        tvTongTien = findViewById(R.id.tv_tongtien);
        btnThanhToan = findViewById(R.id.btn_thanh_toan);
        cartTitle = findViewById(R.id.cart_title);
    }


    private void getCartProductList() {
        FirebaseAuth user = FirebaseAuth.getInstance();
        String userId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("carts")
                .whereEqualTo("userid", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                String cartId = queryDocumentSnapshot.getId();
                                db.collection("detailCarts")
                                    .whereEqualTo("cartId", cartId)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()) {

                                                int i = 0;
                                                for(QueryDocumentSnapshot document : task.getResult()) {
                                                    DetailCart detailCart = document.toObject(DetailCart.class);
                                                    detailCart.setId(document.getId());
                                                    products.add(detailCart);
                                                    if(productId != null && !productId.isEmpty() && detailCart.getProductId().equals(productId)) {
                                                        existPIndex = i;
                                                    }
                                                    i+=1;
                                                }
                                                rcvCart.setVisibility(View.VISIBLE);
                                                if(productId == null || productId.isEmpty()) {
                                                    cartAdapter.setList(products);
                                                    cartTitle.setText("Giỏ hàng (" + String.valueOf(products.size()) + ")");
                                                    calTotalPrice();
                                                    return ;
                                                }

                                                Map<String, Object> newDetailProduct = new HashMap<>();
                                                if(existPIndex == -1) {
                                                    Log.e("status", "go to 2.1");
                                                    newDetailProduct.put("cartId", cartId);
                                                    newDetailProduct.put("productId", productId);
                                                    newDetailProduct.put("productPrice", productPrice);
                                                    newDetailProduct.put("quatity",productQuality);
                                                    db.collection("detailCarts")
                                                    .add(newDetailProduct)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    String id = documentReference.getId();
                                                                    products.add(new DetailCart(id,cartId, productId, productQuality, productPrice));
                                                                    cartAdapter.setList(products);
                                                                    cartTitle.setText("Giỏ hàng (" + String.valueOf(products.size()) + ")");
                                                                    calTotalPrice();
                                                                }
                                                            });
                                                }
                                                else {
                                                    Log.e("status", "go to 2.2");
                                                    DetailCart existDetailCart = products.get(existPIndex);
                                                    int quality = existDetailCart.getQuatity();
                                                    existDetailCart.setQuatity(quality + productQuality);
                                                     products.set(existPIndex, existDetailCart);
                                                     newDetailProduct.put("cartId", cartId);
                                                     newDetailProduct.put("productId", productId);
                                                     newDetailProduct.put("productPrice", productPrice);
                                                     newDetailProduct.put("quatity",existDetailCart.getQuatity());
                                                     db.collection("detailCarts")
                                                          .document(existDetailCart.getId()).set(newDetailProduct);
                                                    cartAdapter.setList(products);
                                                    cartTitle.setText("Giỏ hàng (" + String.valueOf(products.size()) + ")");
                                                    calTotalPrice();
                                                }

                                            }
                                        }});
                            }
                        }
                    }
                });

    }

    private void calTotalPrice() {
        int totalPrice = 0;
        int totalPriceChoice = 0;
        for(DetailCart detailCart : products) {
            int val = detailCart.getQuatity() * detailCart.getProductPrice();
            totalPrice += val;
            if(selectList.contains(detailCart.getId())) {
                totalPriceChoice += val;
            }
        }
        if(selectList.size() == 0) {
            tvTongTien.setText(String.valueOf(totalPrice));
        } else {
            tvTongTien.setText(String.valueOf(totalPriceChoice));
        }
    }
}