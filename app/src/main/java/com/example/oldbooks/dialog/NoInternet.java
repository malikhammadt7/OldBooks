package com.example.oldbooks.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.oldbooks.AppController;
import com.example.oldbooks.databinding.DialogNointernetBinding;
import com.example.oldbooks.manager.FirebaseManager;

public class NoInternet extends Dialog {

    //region Attributes

    //region Class Constants
    private DialogNointernetBinding binding;
    private Activity activity;
    private final String TAG = "Dialog NoInternet";
    //endregion Class Constants

    private Context context;
    //endregion Attributes


    public NoInternet(@NonNull Context context) {
        super(context, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        this.context = context;
        initialize();
    }

    private void initialize() {
        binding = DialogNointernetBinding.inflate(LayoutInflater.from(context));
        setContentView(binding.getRoot());
        setCancelable(true);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        binding.btnClose.setOnClickListener(view -> {
            this.dismiss();
        });

        binding.btnRetry.setOnClickListener(view -> {
            this.dismiss();
        });

        this.show();
        scaleAnimationMethod();
    }

    private void scaleAnimationMethod() {

        Log.d(TAG, "scaleAnimationMethod: 1");
        binding.getRoot().setVisibility(View.VISIBLE);
        // Create a ScaleAnimation to zoom the logo from 0 to 1 scale
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0f, 1f, // Start and end scale X
                0f, 1f, // Start and end scale Y
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point X (center)
                Animation.RELATIVE_TO_SELF, 0.5f  // Pivot point Y (center)
        );
        scaleAnimation.setDuration(500); // Animation duration in milliseconds
        scaleAnimation.setFillAfter(true); // Keep the final scale after the animation ends

        Log.d(TAG, "scaleAnimationMethod: 2");
        // Set an animation listener to make the ImageView visible after the animation ends
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "scaleAnimationMethod: 4");
                binding.getRoot().setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        // Start the animation
        binding.getRoot().startAnimation(scaleAnimation);
        Log.d(TAG, "scaleAnimationMethod: 3");
    }
}