package com.example.oldbooks.manager;

import com.example.oldbooks.AppController;
import com.example.oldbooks.Manager;
import com.example.oldbooks.User;

public class UserManager extends Manager {


    //region Attributes
    private boolean initialized;
    private User user;
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    //endregion Attributes

    //region Singleton
    //endregion Singleton

    //region Methods
    @Override
    public void Initialize() {
        setInitialized(true);
    }
    //endregion Methods

    public boolean isInitialized() {
        return initialized;
    }
    public void setInitialized(boolean initialize) {
        this.initialized = initialize;
    }
}
