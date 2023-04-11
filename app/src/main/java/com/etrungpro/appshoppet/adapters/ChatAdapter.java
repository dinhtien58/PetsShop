package com.etrungpro.appshoppet.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.models.Chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    Context mContext;
    ArrayList<Chat> mList;

    public ChatAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void SetList(ArrayList<Chat> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = mList.get(position);
        holder.tvChatContent.setText(chat.getText());
        String userId = FirebaseAuth.getInstance().getUid();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        if(!chat.getSenderId().equals(userId)) {
            holder.tvChatContent.setBackgroundResource(R.drawable.my_msg_back);
            params.gravity = Gravity.START;
            holder.tvChatDate.setGravity(Gravity.START);
            holder.tvChatContent.setPadding(30, 20, 30, 20);
            params.setMargins(12, 12,12, 12);
            holder.tvChatDate.setLayoutParams(params);
            holder.tvChatContent.setLayoutParams(params);
        } else {
            holder.tvChatContent.setBackgroundResource(R.drawable.opo_msg_back);
            params.gravity = Gravity.END;
            holder.tvChatDate.setGravity(Gravity.END);
            holder.tvChatContent.setPadding(30, 20, 30, 20);
            params.setMargins(12, 12,12, 12);
            holder.tvChatDate.setLayoutParams(params);
            holder.tvChatContent.setLayoutParams(params);
        }

        Timestamp timestamp = chat.getCreateAt();
        Date date = timestamp.toDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
        String formattedTime = sdf.format(date);
        holder.tvChatDate.setText(formattedTime);

    }

    @Override
    public int getItemCount() {
        if(mList != null) {
            return mList.size();
        }

        return 0;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView tvChatContent;
        TextView tvChatDate;
        LinearLayout layoutChatItem;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChatContent = itemView.findViewById(R.id.tv_chat_content);
            tvChatDate = itemView.findViewById(R.id.tv_chat_date);
            layoutChatItem = itemView.findViewById(R.id.layout_chat_item);
        }
    }
}
