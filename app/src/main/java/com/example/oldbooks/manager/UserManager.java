package com.example.oldbooks.manager;

import com.example.oldbooks.Manager;

public class UserManager extends Manager {


    //region Attributes
    private String UserId = "dummyuserid";
    public String getUserId() {
        return UserId;
    }
    public void setUserId(String userId) {
        UserId = userId;
    }
    //endregion Attributes

    //region Singleton
    //endregion Singleton

    //region Methods
    @Override
    public void Initialize() {

    }
    //endregion Methods
}
