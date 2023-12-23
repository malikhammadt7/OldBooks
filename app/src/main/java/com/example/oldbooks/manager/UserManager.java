package com.example.oldbooks.manager;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;

import com.example.oldbooks.AppController;
import com.example.oldbooks.Manager;
import com.example.oldbooks.User;
import com.example.oldbooks.extras.StringConst;

public class UserManager extends Manager {

    //region Attributes
    private User user;
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    //endregion Attributes

    //region Methods

    //region Initialization
    @Override
    public void Initialize() {
        if (isUserLoggedIn()){
            setInitialized(true);
            AppController.getInstance().getManager(CoinManager.class).Initialize();
        } else {
            setInitialized(false);
        }
    }
    //endregion Initialization

    public boolean isUserLoggedIn(){
        SharedPreferences sharedPreferences = AppController.getInstance().getCurrentActivity().getSharedPreferences(StringConst.userPrefs, MODE_PRIVATE);
        return sharedPreferences.getBoolean(StringConst.userLoggedIn, false);
    }
    public void setUserLoggedIn(User _loggedUser){
        SharedPreferences sharedPreferences = AppController.getInstance().getCurrentActivity().getSharedPreferences(StringConst.userPrefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(StringConst.userLoggedIn, true);
        editor.apply();

        setUser(_loggedUser);
        Initialize();
    }
    //endregion Methods
}
