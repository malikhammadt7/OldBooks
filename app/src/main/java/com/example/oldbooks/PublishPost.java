package com.example.oldbooks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.UUID;

public class PublishPost extends AppCompatActivity {

//region Attributes
    private ActivityPublishPostBinding act_binding;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference postDB = db.getReference("post");
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    StorageReference postImgDB = storageRef.child("postImgs");

    private ImageButton lastClickedImgBtn;
    private List<String> selectedImageUrls = new ArrayList<>();

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
                // Commented on purpose
                //createAndAddNewButton();
            }
        });
        act_binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new ImageButton and add it to the container
                lastClickedImgBtn = (ImageButton) view;
                pickImage(view);
            }
        });
        act_binding.btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new ImageButton and add it to the container
                PostIt();
            }
        });
    }

    public void PostIt() {
        // Create a unique key for the new post
        String postId = postDB.push().getKey();

        if(AnythingNull()){
            Toast.makeText(this, "Not Everything is filled", Toast.LENGTH_SHORT).show();
            return;
        }

        postImgDB.child("/" + postId).putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image upload was successful. Now, get the download URL of the image.
                    postImgDB.child("/" + postId).getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                selectedImageUrls.clear();
                                selectedImageUrls.add(uri.toString());
                                // Save the imageUrl to the Realtime Database.
                                Post post = PopulatePostData();
                                post.setImageURLs(selectedImageUrls);
                                // Push the post to the database under the generated key
                                postDB.child(postId).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            act_binding.txtBookName.setText("");
                                            act_binding.txtBookAuthor.setText("");
                                            act_binding.editText.setText("");
                                            act_binding.txtPostDesc.setText("");
                                            Toast.makeText(getApplicationContext(), "successfully uploaded", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "uploaded failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            })
                            .addOnFailureListener(e -> {
                                // Handle any errors while getting the download URL.

                                Toast.makeText(getApplicationContext(), "Image Upload failed", Toast.LENGTH_SHORT).show();

                            });
                })
                .addOnFailureListener(e -> {
                    // Handle any errors while uploading the image.
                });
    }

    private void createAndAddNewButton() {
        ImageButton newButton = new ImageButton(this);
        newButton.setLayoutParams(new LinearLayout.LayoutParams(
                100, 100));
        newButton.setImageResource(android.R.drawable.ic_input_add);
        newButton.setContentDescription("Select Image");
        act_binding.imageButtonContainer.addView(newButton, act_binding.imageButtonContainer.getChildCount() - 1);
    }
    private Post PopulatePostData() {
        Post post = new Post();
        // Populate the currentPost object
        post.setBookName(act_binding.txtBookName.getText().toString());

        // Handle image button click to set image URLs in imageURLs array (assuming you are storing URLs)
        // You should implement this part based on your image selection logic.

        // Handle radio group to set book condition
        int selectedRadioButtonId = act_binding.radioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId == R.id.radioButtonNew) {
            post.setBookCondition(Enums.BookCondition.NEW);
        } else if (selectedRadioButtonId == R.id.radioButtonUsed) {
            post.setBookCondition(Enums.BookCondition.USED);
        }

        // Set price
        String priceText = act_binding.editText.getText().toString();
        if (!priceText.isEmpty()) {
            int price = Integer.parseInt(priceText);
            post.setPrice(price);
        }

        // Set phoneSwitch
        post.setContactVisible(act_binding.phoneswitch.isChecked());

        // Set other properties
        post.setAuthor(act_binding.txtBookAuthor.getText().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        String timestamp = sdf.format(now);
        post.setDate(timestamp);
        if(!selectedImageUrls.isEmpty())
        {
            post.setImageURLs(selectedImageUrls);
        }
        post.setDescription(act_binding.txtPostDesc.getText().toString());

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
    public static final int PICK_IMAGE = 1;
    public void pickImage(View view) {
        Log.d(TAG, " pickImage: requestPermission");
        requestPermission();
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Please Select Image"), PICK_IMAGE);
    }
    Uri imageUri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            try {
                // Convert the URI to a Bitmap (you can adjust the size as needed)
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                // Set the selected image as the src of the ImageButton
                lastClickedImgBtn.setImageBitmap(bitmap);
                selectedImageUrls.add(imageUri.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private final String TAG = "PublishPostActivity:";
    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

}