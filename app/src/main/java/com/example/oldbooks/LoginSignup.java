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
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                LoadRewardedAd();
            }
        });

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


    private RewardedAd rewardedAd;
    boolean isLoading;
    public void LoadRewardedAd(){
        if (rewardedAd == null) {
            isLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(
                    this,
                    AD_UNIT_ID,
                    adRequest,
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.
                            Log.d(TAG, loadAdError.getMessage());
                            rewardedAd = null;
                            LoginSignup.this.isLoading = false;
                            Toast.makeText(LoginSignup.this, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            LoginSignup.this.rewardedAd = rewardedAd;
                            Log.d(TAG, "onAdLoaded");
                            LoginSignup.this.isLoading = false;
                            Toast.makeText(LoginSignup.this, "onAdLoaded", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else
        {
            Log.d(TAG, "rewardedAd == null");
        }
    }

    public void ShowRewardedAd()
    {
        Log.d(TAG, "into ShowRewardedAd()");
        if (rewardedAd != null) {
            Activity activityContext = LoginSignup.this;
            rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                }
            });
            SetContentCallBacks();
            LoadRewardedAd();
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }
    }
    public void SetContentCallBacks()
    {
        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
        @Override
        public void onAdClicked() {
            // Called when a click is recorded for an ad.
            Log.d(TAG, "Ad was clicked.");
        }
        @Override
        public void onAdDismissedFullScreenContent() {
            // Called when ad is dismissed.
            // Set the ad reference to null so you don't show the ad a second time.
            Log.d(TAG, "Ad dismissed fullscreen content.");
            rewardedAd = null;
            LoadRewardedAd(); // Reload the ad here
        }
        @Override
        public void onAdFailedToShowFullScreenContent(AdError adError) {
            // Called when ad fails to show.
            Log.e(TAG, "Ad failed to show fullscreen content.");
            rewardedAd = null;
        }
        @Override
        public void onAdImpression() {
            // Called when an impression is recorded for an ad.
            Log.d(TAG, "Ad recorded an impression.");
        }
        @Override
        public void onAdShowedFullScreenContent() {
            // Called when ad is shown.
            Log.d(TAG, "Ad showed fullscreen content.");
        }
    });
    }

// endregion


}