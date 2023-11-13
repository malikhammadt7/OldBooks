package com.example.oldbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.customadspackage.GoogleAdMobManager;
import com.example.oldbooks.databinding.ActivityMainBinding;
import com.example.oldbooks.manager.CoinManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding act_binding;
    private Activity activity;
    private final String TAG = "MainActivity";
    Runnable addCoinsCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act_binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(act_binding.getRoot());
        activity = this;

        GoogleAdMobManager.getInstance().Initialize(activity);

        act_binding.btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "AddPost Clicked");
                startActivity(new Intent(activity,PublishPost.class));
            }
        });
        act_binding.btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "AddPost Clicked");
                startActivity(new Intent(activity,PublishPost.class));
            }
        });
        act_binding.btnShowRewardAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "btnShowRewardAd clicked");
                GoogleAdMobManager.getInstance().ShowRewardedAd(MainActivity.this, addCoinsCallback);
            }
        });
        act_binding.btnShowRewardInterAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "btnShowRewardInterAd clicked");
                GoogleAdMobManager.getInstance().ShowRewardedInterstitialAd(MainActivity.this, addCoinsCallback);
            }
        });
        addCoinsCallback = new Runnable() {
            @Override
            public void run() {
                //AppController.getInstance().getManager(CoinManager.class).addCoins(10);
                Log.d(TAG, "give addCoins(10)");
                Toast.makeText(activity, "Reward Given: +10 coins", Toast.LENGTH_SHORT).show();
            }
        };
    }
}