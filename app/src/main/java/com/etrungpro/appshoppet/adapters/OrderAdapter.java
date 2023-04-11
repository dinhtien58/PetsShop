package com.etrungpro.appshoppet.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.etrungpro.appshoppet.models.Product;
import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.models.DetailCart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    Context context;
    ArrayList<DetailCart> mList;

    public OrderAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<DetailCart> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_order, parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        DetailCart detailCart = mList.get(position);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .document(detailCart.getProductId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            Product product = documentSnapshot.toObject(Product.class);
                            holder.tvProductOrderName.setText(product.getName());
                            holder.tvProductOrderPrice.setText(String.valueOf(product.getPrice()));
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference();
                            storageRef.child(product.getImgBig()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(context)
                                            .load(uri)
                                            .into(holder.productOrderImage);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                        }

                    }
                });
        holder.tvProductOrderQuatity.setText("Số lượng: " + String.valueOf(detailCart.getQuatity()));
    }

    @Override
    public int getItemCount() {
        if(mList != null) {
            return mList.size();
        }
        return 0;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        ImageView productOrderImage;
        TextView tvProductOrderName;
        TextView tvProductOrderQuatity;
        TextView tvProductOrderPrice;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productOrderImage = itemView.findViewById(R.id.product_order_img);
            tvProductOrderName = itemView.findViewById(R.id.product_order_name);
            tvProductOrderQuatity = itemView.findViewById(R.id.product_order_quatity);
            tvProductOrderPrice = itemView.findViewById(R.id.product_order_price);
        }
    }
}
