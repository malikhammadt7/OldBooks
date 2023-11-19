package com.example.oldbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.oldbooks.adapter.PostlistAdapter;
import com.example.oldbooks.databinding.ActivityPostListBinding;
import com.example.oldbooks.manager.FirebaseManager;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class PostList extends AppCompatActivity {

    private ActivityPostListBinding actBinding;
    private PostlistAdapter postlistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actBinding = ActivityPostListBinding.inflate(getLayoutInflater());
        setContentView(actBinding.getRoot());

        FirebaseRecyclerOptions<Post> options =
                new FirebaseRecyclerOptions.Builder<Post>()
                        .setQuery(AppController.getInstance().getManager(FirebaseManager.class).onlyFeaturedPosts(), Post.class)
                        .build();

        postlistAdapter = new PostlistAdapter(options);
        actBinding.recPostList.setAdapter(postlistAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        postlistAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        postlistAdapter.stopListening();
    }
}