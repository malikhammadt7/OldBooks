package com.example.oldbooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.oldbooks.databinding.ActivityPublishPostBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class PublishPost extends AppCompatActivity {

//region Attributes
    private ActivityPublishPostBinding act_binding;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference postDB = db.getReference("post");
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    StorageReference postImgDB = storageRef.child("postImgs");

// endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act_binding = ActivityPublishPostBinding.inflate(getLayoutInflater());
        setContentView(act_binding.getRoot());

        postDB.setValue("Hello, World!");

        act_binding.addImageButtonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new ImageButton and add it to the container
                createAndAddNewButton();
            }
        });

    }

    public void PostIt(String title, String content, String author) {
        // Create a unique key for the new post
        String postId = postDB.push().getKey();

        if(AnythingNull()){
            Toast.makeText(this, "Not Everything is filled", Toast.LENGTH_SHORT).show();
            return;
        }

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

    private void createAndAddNewButton() {
        Button newButton = new Button(this);
        newButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        newButton.setText("New Button"); // Set text as needed
        act_binding.imageButtonContainer.addView(newButton, act_binding.imageButtonContainer.getChildCount() - 1);
    }
    private Post PostData() {
        Post post = new Post();
        
        return post;
    }


    public boolean AnythingNull() {
        if (act_binding.txtBookName.getText().toString().isEmpty() ||
                act_binding.txtBookAuthor.getText().toString().isEmpty() ||
                act_binding.txtPostDesc.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return true;
        } else if (act_binding.radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please choose a book condition.", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "Post your data to Firebase or perform your action.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}