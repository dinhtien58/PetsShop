package com.etrungpro.appshoppet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.adapters.ChatAdapter;
import com.etrungpro.appshoppet.models.Chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    ImageView senderImage;
    TextView tvSenderName;
    TextView tvStatus;
    Button btnSendMessage;
    EditText edtMessage;
    RecyclerView rcvChat;
    ChatAdapter chatAdapter;
    private ListenerRegistration conversationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initUI();

        tvSenderName.setText(getIntent().getStringExtra("name"));
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
//        storageRef.child(getIntent().getStringExtra("profileImage"))
//                .getDownloadUrl()
//                .addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Glide.with(ChatActivity.this)
//                        .load(uri)
//                        .into(senderImage);
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle any errors
//                        Log.e("status", "false");
//
//                    }
//            });
        chatAdapter = new ChatAdapter(ChatActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, true);
        rcvChat.setLayoutManager(linearLayoutManager);
        rcvChat.setAdapter(chatAdapter);
        getChatList();

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtMessage.length() == 0) {
                    return ;
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Timestamp timestamp = Timestamp.now();
                db.collection("messages")
                        .add(new Chat(getIntent().getStringExtra("conversationId"),
                                timestamp,
                                true,getIntent().getStringExtra("userId") ,
                                edtMessage.getText().toString()))
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                db.collection("conversations")
                                    .document(getIntent().getStringExtra("conversationId"))
                                    .update("lastMessage", documentReference.getId());
                                getChatList();
                            }
                        });

                edtMessage.setText("");
            }
        });
    }

    void initUI() {
        senderImage = findViewById(R.id.sender_chat_img);
        tvSenderName = findViewById(R.id.sender_chat_name);
        tvStatus = findViewById(R.id.tv_sender_chat_status);
        btnSendMessage = findViewById(R.id.btn_send_message);
        edtMessage = findViewById(R.id.edt_message);
        rcvChat = findViewById(R.id.rcv_chat);
    }

    void getChatList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        conversationListener =  db.collection("messages")
                .orderBy("createAt", Query.Direction.DESCENDING)
                .whereEqualTo("conversationId", getIntent().getStringExtra("conversationId"))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        ArrayList<Chat> chats = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : querySnapshot) {
                            Chat chat = documentSnapshot.toObject(Chat.class);
                            chats.add(chat);
                        }

                        chatAdapter.SetList(chats);
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        conversationListener.remove();
    }
}