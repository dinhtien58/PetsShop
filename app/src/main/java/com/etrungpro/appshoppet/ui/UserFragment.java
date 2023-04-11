package com.etrungpro.appshoppet.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.activities.ChatActivity;
import com.etrungpro.appshoppet.activities.EditInfoActivity;
import com.etrungpro.appshoppet.activities.LoginActivity;
import com.etrungpro.appshoppet.models.Chat;
import com.etrungpro.appshoppet.models.Conversation;
import com.etrungpro.appshoppet.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        Button btnSignout = v.findViewById(R.id.btn_signout);
        TextView tvEditInfo = v.findViewById(R.id.tv_editInfo);
        TextView tvUserName = v.findViewById(R.id.tv_user_name);
        TextView tvHelp = v.findViewById(R.id.tv_help);
        ImageView imgView = v.findViewById(R.id.user_image);

        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        getUserInfo();
        db.collection("users").document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            tvUserName.setText(user.getFirstName() + " " + user.getLastName());
                            storageRef.child(user.getImg()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(getActivity())
                                            .load(uri)
                                            .into(imgView);
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

        tvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                ArrayList<String> users = new ArrayList<>();
                String userId = FirebaseAuth.getInstance().getUid();
                String adminId = "1B2oam1BObPL8Bd5qSiDxtyX5UG3";
                users.add(userId);
                users.add(adminId);
                Timestamp timestamp = Timestamp.now();
                Conversation conversation = new Conversation(users, timestamp, "");

                db.collection("conversations")
                        .add(conversation)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                String conversationId = documentReference.getId();

                                Timestamp createAt = Timestamp.now();
                                String greedy = "Hello, mình đến đây theo lời cầu cứu của bạn nè";
                                Chat chat = new Chat(conversationId, createAt, true, adminId, greedy);
                                db.collection("messages")
                                        .add(chat)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                conversation.setLastMessage(documentReference.getId());
                                                db.collection("conversations")
                                                        .document(conversationId)
                                                        .set(conversation);
                                                db.collection("users")
                                                        .document(adminId)
                                                        .get()
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                Intent intent = new Intent(getActivity(), ChatActivity.class);
                                                                String firstName = (String) documentSnapshot.get("firstName");
                                                                String lastName = (String) documentSnapshot.get("lastName");
                                                                String adminName = lastName + " " + firstName;
                                                                String profileImage = (String) documentSnapshot.get("i  mg");
                                                                intent.putExtra("name", adminName);
                                                                intent.putExtra("profileImage", profileImage);
                                                                intent.putExtra("conversationId", conversationId);
                                                                intent.putExtra("userId", userId);

                                                                startActivity(intent);

                                                            }
                                                        });
                                            }
                                        });

                            }
                        });

            }
        });

        tvEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                startActivity(intent);
            }
        });

        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth user = FirebaseAuth.getInstance();
                if (user != null) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }

            }
        });
        return v;
    }

    private void getUserInfo() {
    }
}