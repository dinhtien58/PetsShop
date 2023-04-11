package com.etrungpro.appshoppet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.models.Comment;
import com.etrungpro.appshoppet.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    ArrayList<Comment> mList;
    Context mContext;

    public CommentAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(ArrayList<Comment> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_comment, parent, false);
        return new CommentAdapter.CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = mList.get(position);

        holder.tvUserComment.setText(comment.getContent());
        String userId = comment.getUserId();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            holder.imgUser.setImageResource(R.drawable.toc_tim);
                            holder.tvUserName.setText(user.getLastName() + " " + user.getFirstName());
                        } else {
                            // Làm gì đó;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // làm gì đó;
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

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView imgUser;
        TextView tvUserName;
        TextView tvUserComment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.user_comment_img);
            tvUserName = itemView.findViewById(R.id.tv_user_comment_name);
            tvUserComment = itemView.findViewById(R.id.tv_user_comment_content);
        }
    }
}
