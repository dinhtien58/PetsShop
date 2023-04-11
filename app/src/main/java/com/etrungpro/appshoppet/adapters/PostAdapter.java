package com.etrungpro.appshoppet.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.models.Post;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    Context context;
    ArrayList<Post> postArrayList ;

    public PostAdapter(Context context, ArrayList<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }

    public void setPostArrayList(ArrayList<Post> postArrayList) {
        this.postArrayList = postArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Post post = postArrayList.get(position);

        holder.tvTitle.setText(post.getTitle());
        Timestamp timestamp = post.getPublished_at();
        Date date = timestamp.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedTime = sdf.format(date);
        holder.tvPublished.setText("Ngày đăng: " + formattedTime);
        Glide.with(context).load(post.getImage()).into(holder.ivImage);

        holder.btnXemThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = post.getUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle,tvPublished;
        public ImageView ivImage;
        public Button btnXemThem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvPost_title);
            tvPublished = itemView.findViewById(R.id.tvPost_published_at);
            ivImage = itemView.findViewById(R.id.ivPost_image);
            btnXemThem = itemView.findViewById(R.id.btnXemthem);
        }
    }
}
