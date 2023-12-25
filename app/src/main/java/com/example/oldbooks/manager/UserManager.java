package com.example.oldbooks.manager;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.oldbooks.AppController;
import com.example.oldbooks.Manager;
import com.example.oldbooks.User;
import com.example.oldbooks.extras.StringConst;

public class UserManager extends Manager {

    //region Attributes
    private final String TAG = "UserManager";
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
            SharedPreferences sharedPreferences = AppController.getInstance().getCurrentActivity().getSharedPreferences(StringConst.userPrefs, MODE_PRIVATE);
            Log.d(TAG, "Initialize: 1");
            String a = sharedPreferences.getString(StringConst.userName, "hammad");
            Log.d(TAG, "Initialize: sharedPreferences.getString(StringConst.userName) " + a);
            setUser(a, new FirebaseManager.OnUserFetchListener() {
                @Override
                public void onUserFetchSuccess(User user) {

                    setInitialized(true);
                    AppController.getInstance().getManager(CoinManager.class).Initialize();
                    Log.d(TAG, "Initialize: onUserFetchSuccess " + getUser());
                }

                @Override
                public void onUserFetchFailure(String errorMessage) {

                    Log.d(TAG, "Initialize: onUserFetchFailure " + errorMessage);
                }
            });

            Log.d(TAG, "Initialize: user " + getUser());
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
        editor.putString(StringConst.userName, _loggedUser.getUsername());
        editor.apply();

        setUser(_loggedUser);
        Initialize();
    }
    public void setUser(String username, final FirebaseManager.OnUserFetchListener listener) {
        Log.d(TAG, "setUser: username " + username);
        AppController.getInstance().getManager(FirebaseManager.class).getUserFromDatabase(username, new FirebaseManager.OnUserFetchListener() {
            @Override
            public void onUserFetchSuccess(User user) {

                if (user != null) {
                    setUser(user);
                    listener.onUserFetchSuccess(user);
                } else {
                    listener.onUserFetchFailure("User data is null");
                }
                Log.d(TAG, "Initialize: onUserFetchSuccess" + user);
            }
            @Override
            public void onUserFetchFailure(String errorMessage) {

                Log.d(TAG, "Initialize: onUserFetchFailure " + errorMessage);
                listener.onUserFetchFailure("User data is null");
            }
        });
    }
    //endregion Methods
}
