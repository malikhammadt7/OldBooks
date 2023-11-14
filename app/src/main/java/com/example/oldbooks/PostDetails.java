package com.example.oldbooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.oldbooks.databinding.ActivityPostDetailsBinding;
import com.example.oldbooks.manager.FirebaseManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostDetails extends AppCompatActivity {

    private ActivityPostDetailsBinding actBinding;
    private HashMap<String,Object> postData;
    private Post post = new Post();
    private List<String> imgLinks = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actBinding = ActivityPostDetailsBinding.inflate(getLayoutInflater());
        setContentView(actBinding.getRoot());

        String postId = getIntent().getStringExtra("postid");

        AppController.getInstance().getManager(FirebaseManager.class).DBPostPath.child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    postData = (HashMap<String, Object>) snapshot.getValue();

                    post.setPostTitle(postData.get("postTitle").toString());
                    actBinding.txtposttitle.setText(post.getPostTitle());
                    snapshot.child("imageURLs").getChildren();

                    for (DataSnapshot childSnapshot : snapshot.child("imageURLs").getChildren()) {
                        imgLinks.add(childSnapshot.toString());
                    }
                    Picasso.get().load(imgLinks.get(0)).into(actBinding.imgbookpost);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}