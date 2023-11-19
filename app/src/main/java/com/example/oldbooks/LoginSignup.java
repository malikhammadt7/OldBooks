package com.example.oldbooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.customadspackage.GoogleAdMobManager;
import com.example.oldbooks.databinding.ActivityLoginSignupBinding;
import com.example.oldbooks.manager.CoinManager;
import com.example.oldbooks.manager.FirebaseManager;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class LoginSignup extends AppCompatActivity {

//region Attributes
    private ActivityLoginSignupBinding act_binding;
    private final String TAG = "LoginSignupActivity:";
    private static final String AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
// endregion

    Runnable addCoinsCallback;
//region Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act_binding = ActivityLoginSignupBinding.inflate(getLayoutInflater());
        setContentView(act_binding.getRoot());

        act_binding.btnShowRewardAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "btnShowRewardAd clicked");
                GoogleAdMobManager.getInstance().ShowRewardedAd(LoginSignup.this, addCoinsCallback);
            }
        });
        addCoinsCallback = new Runnable() {
            @Override
            public void run() {
                AppController.getInstance().getManager(CoinManager.class).addCoins(10);
            }
        };
    }

// endregion

}