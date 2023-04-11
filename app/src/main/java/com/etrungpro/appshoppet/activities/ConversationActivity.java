package com.etrungpro.appshoppet.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.adapters.ConversationAdapter;
import com.etrungpro.appshoppet.models.Conversation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ConversationActivity extends AppCompatActivity {

    private RecyclerView rcvConversation;
    private ConversationAdapter conversationAdapter;
    private ArrayList<Conversation> conversations;
    private ListenerRegistration conversationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        initUI();

        conversations = new ArrayList<>();
        conversationAdapter = new ConversationAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ConversationActivity.this, LinearLayoutManager.VERTICAL, false);
        conversationAdapter.setList(conversations);
        rcvConversation.setLayoutManager(linearLayoutManager);
        rcvConversation.setAdapter(conversationAdapter);

        getConversationList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getConversationList();
    }

    void initUI() {
        rcvConversation = findViewById(R.id.rcv_conversation);
    }

    private void getConversationList() {
        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        conversationListener = db.collection("conversations")
                .whereArrayContains("users", userId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            // Xử lý lỗi
                            return;
                        }

                        conversations.clear();

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Conversation conversation = document.toObject(Conversation.class);
                            conversations.add(conversation);
                        }

                        conversationAdapter.setList(conversations);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        conversationListener.remove();
    }
}