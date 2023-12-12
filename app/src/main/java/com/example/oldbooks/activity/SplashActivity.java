package com.example.oldbooks.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.customadspackage.GoogleAdMobManager;
import com.example.oldbooks.AppController;
import com.example.oldbooks.databinding.ActivityMainBinding;
import com.example.oldbooks.databinding.ActivitySplashBinding;
import com.example.oldbooks.extras.Enums;
import com.example.oldbooks.manager.DialogManager;
import com.example.oldbooks.manager.FirebaseManager;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity {

    private Thread t;

    //region Attributes

    //region Class Constants
    private ActivitySplashBinding actBinding;
    private Activity activity;
    private final String TAG = "SplashActivity";
    //endregion Class Constants

    //endregion Attributes


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(actBinding.getRoot());
        activity = this;
//        if (!FirebaseApp.getApps(this).isEmpty()){
//            try{
//                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        AppController.getInstance().setCurrentActivity(activity);

        GoogleAdMobManager.getInstance().Initialize(activity);

        scaleAnimationMethod();
        scalenameAnimationMethod();
        scaletagAnimationMethod();
    }



    //region Methods

    public void ChangeActivity(){

        if (!isInternetConnected()) {
            DialogManager.getInstance().showDialog(Enums.DialogType.NOINTERNET);
        }else{
            t = new Thread()
            {
                public void run()
                {
                    try {
//                        sleep(10000);
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();
        }
    }

    private void scaleAnimationMethod() {
        ImageView imglogo = actBinding.splashApplogo;

        Log.d(TAG, "scaleAnimationMethod: 1");
        imglogo.setVisibility(View.VISIBLE);
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
                imglogo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        // Start the animation
        imglogo.startAnimation(scaleAnimation);
        Log.d(TAG, "scaleAnimationMethod: 3");
    }

    private void scalenameAnimationMethod() {
        TextView txtlogo = actBinding.splashAppname;

        txtlogo.setVisibility(View.VISIBLE);
        // Create a ScaleAnimation to zoom the logo from 0 to 1 scale
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0f, 1f, // Start and end scale X
                0f, 1f, // Start and end scale Y
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point X (center)
                Animation.RELATIVE_TO_SELF, 0.5f  // Pivot point Y (center)
        );
        scaleAnimation.setDuration(200); // Animation duration in milliseconds
        scaleAnimation.setStartOffset(200);
        scaleAnimation.setFillAfter(true); // Keep the final scale after the animation ends

        // Set an animation listener to make the ImageView visible after the animation ends
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "scaleAnimationMethod: 5");
                txtlogo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        // Start the animation
        txtlogo.startAnimation(scaleAnimation);
    }


    private void scaletagAnimationMethod() {
        TextView txttag = actBinding.splashApptagline;

        txttag.setVisibility(View.VISIBLE);
        // Create a ScaleAnimation to zoom the logo from 0 to 1 scale
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0f, 1f, // Start and end scale X
                0f, 1f, // Start and end scale Y
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point X (center)
                Animation.RELATIVE_TO_SELF, 0.5f  // Pivot point Y (center)
        );
        scaleAnimation.setDuration(200); // Animation duration in milliseconds
        scaleAnimation.setStartOffset(400);
        scaleAnimation.setFillAfter(true); // Keep the final scale after the animation ends

        // Set an animation listener to make the ImageView visible after the animation ends
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "scaleAnimationMethod: 6");
                txttag.setVisibility(View.VISIBLE);
                ChangeActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        // Start the animation
        txttag.startAnimation(scaleAnimation);
    }

    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected());
    }


    //endregion Methods


    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "only use in-app buttons", Toast.LENGTH_SHORT).show();
    }
}