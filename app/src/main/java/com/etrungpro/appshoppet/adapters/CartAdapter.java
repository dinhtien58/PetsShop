package com.etrungpro.appshoppet.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.etrungpro.appshoppet.models.Product;
import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.models.DetailCart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context mContext;
    ArrayList<DetailCart> mList;

    public interface ICartEvent {
        void add(String productId);
        void remove(String productId);
        void updateTotalPrice(DetailCart detailCart, int position);
        void delete(int position);
    }

    private ICartEvent iCartEvent;

    public CartAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public CartAdapter(Context mContext , ICartEvent iCartEvent) {
        this.iCartEvent = iCartEvent;
        this.mContext = mContext;
    }

    public void setList(ArrayList<DetailCart> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
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
                            holder.tvProductCartName.setText(product.getName());
                            holder.tvProductCartPrice.setText(String.valueOf(product.getPrice()));
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference();
                            storageRef.child(product.getImgBig()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(mContext)
                                            .load(uri)
                                            .into(holder.ProductCartImage);
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
        holder.tvProductCartQuality.setText(String.valueOf(detailCart.getQuatity()));

        holder.cbProductCartSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()) {
                    iCartEvent.add(detailCart.getId());
                } else {
                    iCartEvent.remove(detailCart.getId());
                }
            }
        });

        holder.btnPlusQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuality = Integer.parseInt(holder.tvProductCartQuality.getText().toString());
                currentQuality += 1 ;
                holder.tvProductCartQuality.setText(String.valueOf(currentQuality));
                detailCart.setQuatity(currentQuality);
                updateCartQuality(detailCart, holder.getAdapterPosition());
            }
        });
        holder.btnSubQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuality = Integer.parseInt(holder.tvProductCartQuality.getText().toString());
                currentQuality -= 1 ;
                holder.tvProductCartQuality.setText(String.valueOf(currentQuality));
                detailCart.setQuatity(currentQuality);
                updateCartQuality(detailCart, holder.getAdapterPosition());
            }
        });

        holder.tvDeleteProductCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               db.collection("detailCarts")
                       .document(detailCart.getId())
                       .delete()
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                               iCartEvent.delete(holder.getAdapterPosition());
                           }
                       });
            }
        });
    }

    private void updateCartQuality(DetailCart detailCart, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("detailCarts")
                .document(detailCart.getId())
                .set(detailCart)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            iCartEvent.updateTotalPrice(detailCart, position);
                        }
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

    public class CartViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductCartName;
        TextView tvProductCartPrice;
        TextView btnPlusQuality;
        TextView btnSubQuality;
        ImageView ProductCartImage;
        TextView tvProductCartQuality;
        TextView tvDeleteProductCart;
        CheckBox cbProductCartSelect;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductCartName = itemView.findViewById(R.id.product_cart_name);
            ProductCartImage = itemView.findViewById(R.id.product_cart_img);
            tvProductCartQuality = itemView.findViewById(R.id.product_cart_quatity);
            tvProductCartPrice = itemView.findViewById(R.id.product_cart_price);
            cbProductCartSelect = itemView.findViewById(R.id.cb_item_cart);
            btnPlusQuality = itemView.findViewById(R.id.btn_cart_plus);
            btnSubQuality = itemView.findViewById(R.id.btn_cart_sub);
            tvDeleteProductCart = itemView.findViewById(R.id.tv_delete_p_cart);
        }
    }
}
