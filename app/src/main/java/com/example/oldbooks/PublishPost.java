package com.example.oldbooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PublishPost extends AppCompatActivity {
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference postDB = db.getReference("post");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_post);

        postDB.setValue("Hello, World!");
    }

    public void PostIt(String title, String content, String author) {
        // Create a unique key for the new post
        String postId = postDB.push().getKey();

        // Create a Post object with the provided data
        Post post = new Post();

        // Push the post to the database under the generated key
        postDB.child(postId).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Post published successfully
                    // You can handle any additional logic here if needed
                } else {
                    // Handle the error
                }
            }
        });
    }
}