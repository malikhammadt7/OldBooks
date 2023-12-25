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
        AppController.getInstance().setCurrentActivity(activity);

//        if (!FirebaseApp.getApps(this).isEmpty()){
//            try{
//                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        AppController.getInstance().initialize();
        GoogleAdMobManager.getInstance().Initialize(activity);

        animScaleUp(actBinding.splashApplogo, 500, 0, false);
        animScaleUp(actBinding.splashAppname, 200, 200, false);
        animScaleUp(actBinding.splashApptagline, 200, 400, true);
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

    private <T extends View> void animScaleUp(T view, long duration, long offset, boolean _changeActivity) {
        view.setVisibility(View.VISIBLE);

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0f, 1f,
                0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(duration);
        scaleAnimation.setStartOffset(offset);
        scaleAnimation.setFillAfter(true);

        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
                if(_changeActivity){
                    ChangeActivity();
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        view.startAnimation(scaleAnimation);
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