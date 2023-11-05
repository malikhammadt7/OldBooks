package com.example.customadspackage;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

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

public class GoogleAdMobManager {

    private final String TAG = "GoogleAdMobManager";

    //region AdMobIds
    private static final boolean TESTING_MODE = true;
    private static String REWARDED_ID;
    private static final String REWARDED_TEST_ID = "ca-app-pub-3940256099942544/5224354917";
    private static final String ADMOB_REWARDED_ID = "here type app id provided by google admob";
    //endregion AdMobIds

    //region Singleton Pattern
    private static GoogleAdMobManager instance;
    private GoogleAdMobManager(){}
    public static synchronized GoogleAdMobManager getInstance()
    {
        if (instance == null) {
            instance = new GoogleAdMobManager();
        }
        return instance;
    }
    //endregion Singleton Pattern

    //region Attributes
    private Context context;
    private RewardedAd rewardedAd;
    boolean isLoading;
    boolean isInitialized;
    //endregion Attributes

    public void Initialize(Context c) {
        context = c;
        Log.d(TAG, "Google Mobile Ads SDK Version: " + MobileAds.getVersion());
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                isInitialized = true;
                if(TESTING_MODE)
                {
                    REWARDED_ID = REWARDED_TEST_ID;
                }
                else
                {
                    REWARDED_ID = ADMOB_REWARDED_ID;
                }
                Log.d(TAG, "GoogleAdMobManager: Initialize:\nTESTING_MODE: " + TESTING_MODE + "\nREWARDED_ID: " + REWARDED_ID);
                LoadRewardedAd();
            }
        });
    }
    public boolean IsAdMobInitialized() {
        return isInitialized;
    }
    //region Rewarded Ad
    public boolean IsRewardedAdLoaded() {
        return rewardedAd != null;
    }
    public void LoadRewardedAd() {
        if (IsRewardedAdLoaded()) {
            Log.d(TAG, "RewardedAd is already Loaded");
        }
        else
        {
            isLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            Log.d(TAG, "context: " + context + "\nREWARDED_ID: " + REWARDED_ID + "\nadRequest: " + adRequest);
            RewardedAd.load(context, REWARDED_ID, adRequest,
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.d(TAG, loadAdError.getMessage());
                            rewardedAd = null;
                            isLoading = false;
                        }
                        @Override
                        public void onAdLoaded(@NonNull RewardedAd ad) {
                            Log.d(TAG, "Rewarded Ad got Loaded");
                            rewardedAd = ad;
                            isLoading = false;
                        }
                    });
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
    public void ShowRewardedAd(Activity c, Runnable successCallback) {
        Log.d(c.toString().toUpperCase(), "ShowRewardedAd");
        if (IsRewardedAdLoaded()) {
            Activity activityContext = c;
            SetContentCallBacks();
            rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    Log.d(TAG, "The user earned the reward.");
                    if (successCallback != null) {
                        successCallback.run();
                    }
                }
            });
            LoadRewardedAd();
        } else {
            Log.d(TAG, "The rewarded ad isn't ready yet.");
        }
    }
    //endregion Rewarded Ad
}
