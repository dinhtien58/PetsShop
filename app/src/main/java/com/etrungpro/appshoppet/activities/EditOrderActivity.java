package com.etrungpro.appshoppet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.adapters.OrderAdapter;
import com.etrungpro.appshoppet.models.DetailCart;
import com.etrungpro.appshoppet.models.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditOrderActivity extends AppCompatActivity {

    RecyclerView rcvCartOrder;
    TextView tvSoLuongSp;
    EditText edtEmailOrder;
    EditText edtFullNameOrder;
    EditText edtAddressOrder;
    MaterialButton btnThanhToan;
    String productNames = "";
    String totalPrice;
    ArrayList<DetailCart> detailCarts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        initUI();

        Bundle bundle = getIntent().getExtras();

        detailCarts = (ArrayList<DetailCart>) bundle.get("detailCarts");
        totalPrice = (String) bundle.get("totalPrice");

        OrderAdapter orderAdapter = new OrderAdapter(EditOrderActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                EditOrderActivity.this, LinearLayoutManager.VERTICAL, false);
        rcvCartOrder.setLayoutManager(linearLayoutManager);
        rcvCartOrder.setAdapter(orderAdapter);
        tvSoLuongSp.setText("Đơn hàng (" + String.valueOf(detailCarts.size()) + ")");
        orderAdapter.setList(detailCarts);

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("products")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        for(DetailCart detailCart : detailCarts) {
                                            if(document.getId().equals(detailCart.getProductId()))
                                                productNames += (String)document.get("name") + " ";
                                        }
                                    }

                                    Map<String, Object> order = new HashMap<>();
                                    order.put("overview", productNames);
                                    order.put("address", edtAddressOrder.getText().toString());
                                    String userId = FirebaseAuth.getInstance().getUid();
                                    order.put("userId", userId);
                                    order.put("totalPrice", Integer.parseInt(totalPrice));
                                    order.put("createAt", Timestamp.now());

                                    db.collection("orders")
                                            .add(order)
                                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    if(task.isSuccessful()) {
                                                        Toast.makeText(EditOrderActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(EditOrderActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }
                                            });
                                }
                            }
                        });



            }
        });
    }

    void initUI() {
        rcvCartOrder = findViewById(R.id.rcv_cart_order);
        edtEmailOrder = findViewById(R.id.edt_email_order);
        edtFullNameOrder = findViewById(R.id.edt_fullname_order);
        btnThanhToan = findViewById(R.id.btn_thanh_toan);
        tvSoLuongSp = findViewById(R.id.tv_don_hang_sl);
        edtAddressOrder = findViewById(R.id.edt_address_order);

    }
}