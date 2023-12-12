package com.example.oldbooks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.oldbooks.databinding.ActivityPublishPostBinding;
import com.example.oldbooks.databinding.DialogConfirmfeaturedpostBinding;
import com.example.oldbooks.extras.Enums;
import com.example.oldbooks.manager.CoinManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class PublishPost extends AppCompatActivity {

//region Attributes
    private ActivityPublishPostBinding act_binding;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference postDB = db.getReference("post");
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    StorageReference postImgDB = storageRef.child("postImgs");
    private ImageView lastClickedImgBtn;
    private List<String> selectedImageUrls = new ArrayList<>();

    private int maxImageViews = 6;
    private int currentImageViews = 1;
// endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act_binding = ActivityPublishPostBinding.inflate(getLayoutInflater());
        setContentView(act_binding.getRoot());
        postDB.setValue("Hello, World!");


        act_binding.btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new ImageButton and add it to the container
                Proceed();
            }
        });

        act_binding.firstImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastClickedImgBtn = (ImageView) view;
                pickImage(view);
            }
        });
    }

    private void createNewImageView() {
        if (currentImageViews < maxImageViews) {
            ImageView newImageView = new ImageView(this);
            // Maximum number of ImageView you want to allow
            int imagesPerColumn = 2; // Number of images you want to display per column
            int totalColumns = maxImageViews / imagesPerColumn; // Calculate the number of columns needed

            ImageView imageView = new ImageView(this); // Create a new ImageView
            imageView.setLayoutParams(new GridLayout.LayoutParams()); // Set layout parameters

            GridLayout.Spec rowSpec = GridLayout.spec(currentImageViews / imagesPerColumn); // Calculate the row position
            GridLayout.Spec colSpec = GridLayout.spec(currentImageViews % imagesPerColumn); // Calculate the column position
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, colSpec);
            params.setMargins(0 ,5,0,5);
            params.setGravity(Gravity.FILL);
            // Set width and height to match the existing ImageView
            params.height = act_binding.firstImageView.getMinimumWidth();
            params.height = act_binding.firstImageView.getMinimumHeight();// Adjust the height as needed

            newImageView.setLayoutParams(params);

            newImageView.setImageResource(android.R.drawable.ic_input_add);
            newImageView.setTag(currentImageViews);

            newImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastClickedImgBtn = (ImageView) v;
                    pickImage(v);
                }
            });

            act_binding.gridLayout.addView(newImageView);
            currentImageViews++;
        }
    }
    public void PostIt(boolean feature) {
        // Create a unique key for the new post
        String postId = postDB.push().getKey();

        postImgDB.child("/" + postId).putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    postImgDB.child("/" + postId).getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                selectedImageUrls.clear();
                                selectedImageUrls.add(uri.toString());
                                Post post = PopulatePostData();
                                post.setFeatured(feature);
                                post.setImageURLs(selectedImageUrls);
                                post.setDatetime(AppController.getCurrentTimestamp());
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

                                Toast.makeText(getApplicationContext(), "Image Upload failed", Toast.LENGTH_SHORT).show();

                            });
                })
                .addOnFailureListener(e -> {
                    // Handle any errors while uploading the image.
                });
    }

    public void Proceed()
    {
        if(AnythingNull())
        {
            Toast.makeText(this, "Not Everything is filled", Toast.LENGTH_SHORT).show();
        }
        else
        {
            showFeaturedDialog();
        }
    }

    private Post PopulatePostData() {
        Post post = new Post();
        // Populate the currentPost object
        post.setPostTitle(act_binding.txtBookName.getText().toString());

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
        post.setContactVisible(act_binding.phoneswitch.isChecked());
        post.setAuthor(act_binding.txtBookAuthor.getText().toString());
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
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);

                // Set the selected image as the src of the ImageButton
                lastClickedImgBtn.setImageBitmap(resizedBitmap);
                selectedImageUrls.add(imageUri.toString());
                lastClickedImgBtn.setOnClickListener(null);
                lastClickedImgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(view.getContext(), "The Work is done here", Toast.LENGTH_SHORT).show();
                    }
                });
                createNewImageView();
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


    private DialogConfirmfeaturedpostBinding dialogBinding;
    private AlertDialog alertDialog;
    private void showFeaturedDialog() {

        dialogBinding = DialogConfirmfeaturedpostBinding.inflate(LayoutInflater.from(this));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // Set the dialog to be non-cancelable

        // Inflate your custom layout for the dialog
        View view = getLayoutInflater().inflate(R.layout.dialog_confirmfeaturedpost, null);
        builder.setView(dialogBinding.getRoot());


        dialogBinding.dialogtxt.setText("Do you want to spend 5 coins to feature your post?");

        // Set actions for the positive button
        dialogBinding.btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                do it after adding user work

                CoinManager coinManager = AppController.getInstance().getManager(CoinManager.class);
                if(coinManager.getTotalCoins() >= 20){
                    coinManager.deductCoins(20);
                    Toast.makeText(getApplicationContext(), "coinManager.deductCoins(5)",Toast.LENGTH_LONG);

                    alertDialog.dismiss();
                }else{
                    alertDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Not Enough Coins",Toast.LENGTH_LONG);
                }
                PostIt(true);
            }
        });

        // Set actions for the negative button
        dialogBinding.btnreject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostIt(false);
                alertDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Posted",Toast.LENGTH_LONG);

            }
        });

        // Create the dialog
        alertDialog = builder.create();

        // Show the dialog
        alertDialog.show();
    }

}