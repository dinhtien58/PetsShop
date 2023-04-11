package com.etrungpro.appshoppet.adapters;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.activities.ChatActivity;
import com.etrungpro.appshoppet.models.Conversation;
import com.etrungpro.appshoppet.models.Chat;
import com.etrungpro.appshoppet.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    ArrayList<Conversation> mList;
    Context mContext;

    public ConversationAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(ArrayList<Conversation> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false);
        return new ConversationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        Conversation conversation = mList.get(position);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final String[] coversationId = {""};
        String currentUser = FirebaseAuth.getInstance().getUid();
        Log.e("status", conversation.getLastMessage());
//        db.collection("messages")
//                .document(conversation.getLastMessage())
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        coversationId[0] = documentSnapshot.getString("conversationId");
//                        Chat chat = documentSnapshot.toObject(Chat.class);
//                        if(chat.getSenderId().equals(currentUser)) {
//                            holder.tvLastMessage.setText("Bạn: " + chat.getText());
//                        }
//                        else {
//                            holder.tvLastMessage.setText(chat.getText());
//                        }
//                        if(chat.isSeen() == true) {
//                            holder.tvLastMessage.setTextColor(Color.rgb(176, 179, 184));
//                        }
//                        Timestamp timestamp = chat.getCreateAt();
//                        Date date = timestamp.toDate();
//                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
//                        String formattedTime = sdf.format(date);
//                        holder.tvCreateAt.setText(formattedTime);
//                    }
//                });
        ArrayList<String> usersOfCurrentConversation = conversation.getUsers();
        String anotherUserID = "";
        for(String userId : usersOfCurrentConversation) {
            if(!userId.equals(currentUser)) {
                anotherUserID = userId;
            }
        }

        User anotherUser = new User();
        db.collection("users")
                .document(anotherUserID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            anotherUser.setFirstName(user.getFirstName());
                            anotherUser.setLastName(user.getLastName());
                            anotherUser.setImg(user.getImg());

                            holder.tvSenderName.setText(user.getLastName() + " " + user.getFirstName());
                            Log.e("img", user.getImg());
                            storageRef.child(user.getImg()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(mContext)
                                            .load(uri)
                                            .into(holder.circleSenderImage);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    Log.e("status", "false");
                                }
                            });
                        }
                    }
                });

        holder.layoutConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("conversationId",coversationId[0]);
                intent.putExtra("name", anotherUser.getLastName() + " " + anotherUser.getFirstName());
                intent.putExtra("profileImage", anotherUser.getImg());
                intent.putExtra("userId", currentUser);
                intent.putExtra("conversationId", coversationId[0]);

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

    public class ConversationViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleSenderImage;
        TextView tvSenderName;
        TextView tvLastMessage;
        TextView tvCreateAt;
        ConstraintLayout layoutConversation;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            circleSenderImage = itemView.findViewById(R.id.sender_image);
            tvSenderName = itemView.findViewById(R.id.sender_name);
            tvLastMessage = itemView.findViewById(R.id.tv_last_message);
            tvCreateAt = itemView.findViewById(R.id.tv_create_at);
            layoutConversation = itemView.findViewById(R.id.layout_conversation);

        }
    }
}
