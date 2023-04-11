package com.etrungpro.appshoppet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etrungpro.appshoppet.models.Product;
import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.models.Category;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context mContext;
    private ArrayList<Category> mListCategory;

    public interface ICategoryEnvent {
        void showProductDetail(Product product);
        void showProductList(String category);
    }

    private ICategoryEnvent iCategoryEnvent;
    public CategoryAdapter(Context mContext, ICategoryEnvent iCategoryEnvent) {
        this.iCategoryEnvent = iCategoryEnvent;
        this.mContext = mContext;
    }

    public void setData(ArrayList<Category> list) {
        this.mListCategory = list;
        this.iCategoryEnvent = iCategoryEnvent;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);

        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = mListCategory.get(position);
        if(category == null) {
            return ;
        }
        holder.categoryName.setText(category.getName());

        LinearLayoutManager linearLayout = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        holder.rcvProduct.setLayoutManager(linearLayout);

        ProductAdapter productAdapter = new ProductAdapter(mContext, new ProductAdapter.IProductEvent() {
            @Override
            public void showDetail(Product product) {
                iCategoryEnvent.showProductDetail(product);
            }
        });
        productAdapter.setData(category.getProducts());
        holder.rcvProduct.setAdapter(productAdapter);

        holder.btnShowALl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iCategoryEnvent.showProductList(category.getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mListCategory != null) return mListCategory.size();
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryName;
        private RecyclerView rcvProduct;
        private MaterialButton btnShowALl;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            rcvProduct = itemView.findViewById(R.id.rcv_product);
            btnShowALl = itemView.findViewById(R.id.btn_show_all);

        }
    }
}
