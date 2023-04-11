package com.etrungpro.appshoppet.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.adapters.PostAdapter;
import com.etrungpro.appshoppet.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FeedFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<Post> postArrayList;
    PostAdapter postAdapter;

    public FeedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        recyclerView = v.findViewById(R.id.rc_post);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        postArrayList = new ArrayList<Post>();
        postAdapter = new PostAdapter(getActivity(), postArrayList);
        recyclerView.setAdapter(postAdapter);
        getPostList();

        return v;
    }

    private void getPostList()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {

                            for(QueryDocumentSnapshot document : task.getResult())
                            {
                                Post post = document.toObject(Post.class);
                                post.setId(document.getId());
                                postArrayList.add(post);

                            }
                            postAdapter.setPostArrayList(postArrayList);
                        }
                    }
                });

    }

}