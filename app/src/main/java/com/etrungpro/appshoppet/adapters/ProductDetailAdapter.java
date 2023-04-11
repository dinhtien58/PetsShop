package com.etrungpro.appshoppet.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.models.ProductDetail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ProductDetailAdapter extends RecyclerView.Adapter<ProductDetailAdapter.ProductDetailViewHolder> {

    ArrayList<ProductDetail> mList;
    Context mContext;

    public interface IProductDetailEvent {
        void set(String productImageUrl);
    }

    private IProductDetailEvent iProductEvent;
    public ProductDetailAdapter(Context mContext, IProductDetailEvent iProductEvent) {
        this.mContext = mContext;
        this.iProductEvent = iProductEvent;
    }

    public void setList(ArrayList<ProductDetail> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_img, parent, false);
        return new ProductDetailAdapter.ProductDetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDetailViewHolder holder, int position) {
        ProductDetail productDetail = mList.get(position);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        storageRef.child(productDetail.getImg()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext)
                        .load(uri)
                        .into(holder.productImg);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e("status", "false");

            }
        });
        Glide.with(mContext).load(productDetail.getImg()).into(holder.productImg);
        holder.productImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iProductEvent.set(productDetail.getImg());
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

    public class ProductDetailViewHolder extends RecyclerView.ViewHolder {

        ImageView productImg;

        public ProductDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            productImg = itemView.findViewById(R.id.product_detail_sm);
        }

    }

}
