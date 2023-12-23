package com.example.oldbooks.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.customadspackage.GoogleAdMobManager;
import com.example.oldbooks.AppController;
import com.example.oldbooks.DailyRewards;
import com.example.oldbooks.PostList;
import com.example.oldbooks.PublishPost;
import com.example.oldbooks.R;
import com.example.oldbooks.User;
import com.example.oldbooks.databinding.ActivityMainBinding;
import com.example.oldbooks.extras.Enums;
import com.example.oldbooks.manager.CoinManager;
import com.example.oldbooks.manager.FirebaseManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;

public class MainActivity extends AppCompatActivity {

    //region Attributes

    //endregion Attributes

    //region Attributes
    private ActivityMainBinding act_binding;
    private Activity activity;
    private final String TAG = "MainActivity";
    private FirebaseManager firebaseManager;
    Runnable addCoinsCallback;
    //endregion Attributes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act_binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(act_binding.getRoot());
        activity = this;
        AppController.getInstance().setCurrentActivity(activity);

        firebaseManager = AppController.getInstance().getManager(FirebaseManager.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("user")) {
            User user = (User) intent.getSerializableExtra("user");
        }

        act_binding.btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "AddPost Clicked");
                startActivity(new Intent(activity, PublishPost.class));
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
                Log.d(TAG, "BtnSignup Clicked");
                btnSignUp();
            }
        });
        act_binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogin();
            }
        });

        act_binding.btnMessageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppController.IsGuestView()){
                    Toast.makeText(activity, "Activity Changing to: MessageList", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(activity, MessageListActivity.class));
                }else{
                    Toast.makeText(activity, "Cannot go to message list because user is not logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        act_binding.btnExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogin();
            }
        });

        SetupBottomNavbar();
    }

// endregion

    //region Bottom Navigation Bar
    public void SetupBottomNavbar(){

        // Set a listener to respond to item clicks
        act_binding.navbarBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuoption_home) {

                    return true;
                } else if (item.getItemId() == R.id.menuoption_dashboard) {

                    return true;
                } else if (item.getItemId() == R.id.menuoption_notification) {

                    return true;
                }
                return false;
            }
        });

        act_binding.navbarBottom.inflateMenu(R.menu.menu_bottom_navbar);
    }
    //endregion Bottom Navigation Bar

// region Login/Signup

//region Signup
    public void btnSignUp(){
        User user = new User();
        user.setUsername(act_binding.edtUserName.getText().toString());
        user.setPassword(act_binding.edtSignupPswd.getText().toString());
        user.setPhoneNumber(act_binding.edtPhoneNumber.getText().toString());
        Log.d(TAG, "BtnSignup");
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