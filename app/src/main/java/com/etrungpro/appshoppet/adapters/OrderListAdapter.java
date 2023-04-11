package com.etrungpro.appshoppet.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.activities.DetailOrderActivity;
import com.etrungpro.appshoppet.models.Order;

import java.util.ArrayList;
import java.util.Date;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder> {

    Context mContext;
    ArrayList<Order> mList;

    public OrderListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(ArrayList<Order> mList) {
        this.mList = mList;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public OrderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderListAdapter.OrderListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListViewHolder holder, int position) {
        Order order = mList.get(position);

        holder.tvOrderId.setText("ID: " + order.getId());
        holder.tvOrderOverview.setText("Overview: " + order.getOverview());
        holder.tvOrderAddress.setText(order.getAddress());

        Date date = order.getCreateAt().toDate();
        holder.tvOrderDate.setText("Date: " + date.toString());
        holder.tvOrderAddress.setText("Address: " + order.getAddress());
        holder.tvOrderTotalPrice.setText("Tổng tiền: " + String.valueOf(order.getTotalPrice()) + "đ");

        holder.layoutOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailOrderActivity.class);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mList != null) {
            return mList.size();
        }
        return 0;
    }

    public class OrderListViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrderId;
        TextView tvOrderOverview;
        TextView tvOrderAddress;
        TextView tvOrderDate;
        TextView tvOrderTotalPrice;
        LinearLayout layoutOrder;

        public OrderListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvOrderOverview = itemView.findViewById(R.id.tv_order_overview);
            tvOrderAddress = itemView.findViewById(R.id.tv_order_address);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date);
            tvOrderTotalPrice = itemView.findViewById(R.id.tv_order_total_price);
            layoutOrder = itemView.findViewById(R.id.layout_order_item);
        }
    }
}
