package com.example.oldbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.oldbooks.databinding.ActivityDailyRewardsBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DailyRewards extends AppCompatActivity {

    private ActivityDailyRewardsBinding actBinding;

    private static final String PREF_NAME = "DailyRewardPrefs";
    private static final String KEY_LAST_LOGIN_DATE = "lastLoginDate";
    private static final String KEY_CONSECUTIVE_LOGINS = "consecutiveLogins";

    private static final int MAX_CONSECUTIVE_DAYS = 7;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actBinding = ActivityDailyRewardsBinding.inflate(getLayoutInflater());
        setContentView(actBinding.getRoot());

        initialize();

    }

    private void resetConsecutiveLogins() {
        sharedPreferences.edit().putInt(KEY_CONSECUTIVE_LOGINS, 1).apply();
        RedrawRewards(sharedPreferences.getInt(KEY_CONSECUTIVE_LOGINS, 0));
    }

    public void initialize(){
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayDate = sdf.format(currentDate);

        String lastLoginDate = sharedPreferences.getString(KEY_LAST_LOGIN_DATE, "");

        if (!todayDate.equals(lastLoginDate)) {
            int consecutiveLogins = sharedPreferences.getInt(KEY_CONSECUTIVE_LOGINS, 0);

            if (!lastLoginDate.isEmpty()) {
                try {
                    Date lastLogin = sdf.parse(lastLoginDate);
                    long dayDifference = TimeUnit.DAYS.convert(currentDate.getTime() - lastLogin.getTime(), TimeUnit.MILLISECONDS);

                    if (dayDifference > 1) {
                        resetConsecutiveLogins();
                    }else{
                        if (consecutiveLogins == MAX_CONSECUTIVE_DAYS) {
                            resetConsecutiveLogins();
                        } else {
                            sharedPreferences.edit().putInt(KEY_CONSECUTIVE_LOGINS, consecutiveLogins + 1).apply();
                            RedrawRewards(sharedPreferences.getInt(KEY_CONSECUTIVE_LOGINS, 0));
                        }
                        sharedPreferences.edit().putString(KEY_LAST_LOGIN_DATE, todayDate).apply();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }else {
            startActivity(new Intent(this,MainActivity.class));
        }

    }

    public void RedrawRewards(int day){

    }
}