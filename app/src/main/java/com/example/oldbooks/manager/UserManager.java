package com.example.oldbooks.manager;

import com.example.oldbooks.AppController;
import com.example.oldbooks.Manager;
import com.example.oldbooks.User;

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
    @Override
    public void Initialize() {
        setInitialized(true);
    }
    //endregion Methods
}
