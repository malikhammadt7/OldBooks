package com.example.oldbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.oldbooks.databinding.ActivityLoginSignupBinding;

public class LoginSignup extends AppCompatActivity {

//region Attributes
    private ActivityLoginSignupBinding act_binding;


// endregion

//region Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act_binding = ActivityLoginSignupBinding.inflate(getLayoutInflater());
        setContentView(act_binding.getRoot());
    }
// endregion


}