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
import com.example.oldbooks.extras.Preconditions;
import com.example.oldbooks.manager.CoinManager;
import com.example.oldbooks.manager.FirebaseManager;
import com.example.oldbooks.manager.UserManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding act_binding;
    private Activity activity;
    private final String TAG = "MainActivity";
    private FirebaseManager firebaseManager;
    Runnable addCoinsCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act_binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(act_binding.getRoot());
        activity = this;

        GoogleAdMobManager.getInstance().Initialize(activity);
        AppController.getInstance();
        firebaseManager = AppController.getInstance().getManager(FirebaseManager.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("user")) {
            User user = (User) intent.getSerializableExtra("user");
        }

        act_binding.btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "AddPost Clicked");
                startActivity(new Intent(activity,PublishPost.class));
            }
        });
        act_binding.btnViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "AddPost Clicked");
                startActivity(new Intent(activity, PostList.class));
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
        act_binding.btnDailyReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "btnDailyReward clicked");
                startActivity(new Intent(activity, DailyRewards.class));
            }
        });
        addCoinsCallback = new Runnable() {
            @Override
            public void run() {
                AppController.getInstance().getManager(CoinManager.class).addCoins(10);
                Log.d(TAG, "give addCoins(10)");
                Toast.makeText(activity, "Reward Given: +10 coins", Toast.LENGTH_SHORT).show();
            }
        };
        act_binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSignUp();
            }
        });
        act_binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogin();
            }
        });
    }

// endregion

// region Login/Signup

//region Signup
    public void btnSignUp(){
        User user = new User();
        user.setUsername(act_binding.edtUserName.getText().toString());
        user.setPassword(act_binding.edtSignupPswd.getText().toString());
        user.setPhoneNumber(act_binding.edtPhoneNumber.getText().toString());
        if (!user.validateUsername(user.getUsername()) && !user.validatePassword(user.getPassword()) && !user.validatePhoneNumber(user.getPhoneNumber()))
        {
            return;
        }
//        if (!verifySignup(user))
//        {
//            return;
//        }
        user.setJoinedDate(AppController.getCurrentTimestamp());
        firebaseManager.addNewUser(activity, user);
    }
    public boolean verifySignup(User user){
        return firebaseManager.verifySignupCredential(activity, user.getUsername());
    }
// endregion Signup

//region Login
    public void btnLogin(){
        User user = new User();
        user.setUsername(act_binding.edtId.getText().toString());
        user.setPassword(act_binding.edtId.getText().toString());
        if (!user.validateUsername(user.getUsername()) && !user.validatePassword(user.getPassword()))
        {
            return;
        }
//        if (!verifyLogin(user))
//        {
//            return;
//        }
        user.setUserStatus(Enums.UserStatus.ACTIVE);
        firebaseManager.loginUser(activity,user);
    }
    public boolean verifyLogin(User user){
        return firebaseManager.verifyLoginCredential(activity, user.getUsername());
    }
// endregion Login


// endregion Login/Signup
}