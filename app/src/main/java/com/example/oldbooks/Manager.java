package com.example.oldbooks;

import android.content.Context;

public abstract class Manager {
    private boolean isInitialized;

    public boolean isInitialized() {
        return isInitialized;
    }

    public void setInitialized(boolean initialized) {
        isInitialized = initialized;
    }

    public Manager() {
    }
    public abstract void Initialize();
}