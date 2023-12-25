package com.example.oldbooks.manager;

import android.content.Context;
import android.util.Log;

import com.example.oldbooks.AppController;
import com.example.oldbooks.Manager;
import com.example.oldbooks.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CoinManager extends Manager{

    //region Attributes
    private final String TAG = "CoinManager";
    private int totalCoins;
    public String userId;
    private DatabaseReference databaseReference;
    //endregion Attributes


    public CoinManager() {
    }
    public CoinManager(String userId) {
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("totalCoins");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Initialize();
    }

    @Override
    public void Initialize() {
        AppController.getInstance().getManager(UserManager.class).getUser().getUsername();
        totalCoins = AppController.getInstance().getManager(UserManager.class).getUser().getCoin();
        setInitialized(true);

        Log.d(TAG, "LoadChatRooms");
        AppController.getInstance().LoadChatRooms();
    }

    public int getTotalCoins()
    {
        return totalCoins;
    }

    public void setTotalCoins(int totalCoins) {
        this.totalCoins = totalCoins;
        databaseReference.child("users").child(userId).child("coin").setValue(totalCoins);
    }

    public void addCoins(int amount) {
        Log.d(TAG, "addCoins: 5 coins");
        totalCoins += amount;
        setTotalCoins(totalCoins);
    }

    public void deductCoins(int amount) {
        if (totalCoins >= amount) {
            totalCoins -= amount;
            setTotalCoins(totalCoins);
        } else {
            // Handle the case where the user doesn't have enough coins.
        }
    }

}
