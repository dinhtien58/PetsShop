package com.etrungpro.appshoppet.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.CategoryViewHolder>  {

    ArrayList<Product> mList;
    Context mContext;
    public interface IProductEvent {
        void showDetail(Product product);
    }

    private IProductEvent iProductEvent;
    public ProductAdapter(Context context,IProductEvent iProductEvent ) {
        this.mContext = context;
        this.iProductEvent = iProductEvent;
    }

    public void setData(ArrayList<Product> list) {
        this.mList = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Product product = mList.get(position);
        if(product == null) {
            Log.e("value", "null");
            return ;
        }

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            storageRef.child(product.getImgBig()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(mContext)
                            .load(uri)
                            .into(holder.imgProduct);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Log.e("status", "false");

                }
            });

        holder.tvProductName.setText(product.getName());
        holder.tvProductPrice.setText(String.valueOf(product.getPrice()) + " đ");

        holder.tvProductName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iProductEvent.showDetail(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mList != null) return mList.size();
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView tvProductName;
        private TextView tvProductPrice;

        public CategoryViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.product_img);
            tvProductName = itemView.findViewById(R.id.product_name);
            tvProductPrice = itemView.findViewById(R.id.product_price);
        }
    }
}
