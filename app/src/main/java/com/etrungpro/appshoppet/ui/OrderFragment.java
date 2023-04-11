package com.etrungpro.appshoppet.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.adapters.OrderListAdapter;
import com.etrungpro.appshoppet.models.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;


public class OrderFragment extends Fragment {


    RecyclerView rcvOrders;
    OrderListAdapter orderListAdapter;
    ArrayList<Order> orders;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order, container, false);
        rcvOrders = v.findViewById(R.id.rcv_orders);

        orders = new ArrayList<>();
        OrderListAdapter orderListAdapter = new OrderListAdapter(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        rcvOrders.setLayoutManager(linearLayoutManager);
        rcvOrders.setAdapter(orderListAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getUid();
        db.collection("orders")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(DocumentSnapshot document : task.getResult()) {
                                Order order = document.toObject(Order.class);
                                order.setId(document.getId());
                                orders.add(order);
                            }
                            orderListAdapter.setList(orders);
                        }
                    }
                });
        return v;

    }
}