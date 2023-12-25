package com.example.oldbooks.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.oldbooks.AppController;
import com.example.oldbooks.R;
import com.example.oldbooks.adapter.ChatMessageAdapter;
import com.example.oldbooks.adapter.MessageListAdapter;
import com.example.oldbooks.databinding.ActivityChatBinding;
import com.example.oldbooks.databinding.ActivityMessageListBinding;
import com.example.oldbooks.manager.FirebaseManager;
import com.example.oldbooks.manager.UserManager;
import com.example.oldbooks.model.ChatMessage;
import com.example.oldbooks.model.ChatRoom;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {

    //region Attributes
    //region Class Constant
    private ActivityMessageListBinding actBinding;
    private Activity activity;
    private final String TAG = "MessageListActivity";
    //endregion Class Constant
    private MessageListAdapter messageListAdapter;
    private String username;
    //endregion Attributes

    //region Methods
    //region Initialization
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actBinding = ActivityMessageListBinding.inflate(getLayoutInflater());
        setContentView(actBinding.getRoot());
        activity = this;
        AppController.getInstance().setCurrentActivity(activity);

        List<ChatRoom> chatRooms = new ArrayList<>();
        chatRooms.addAll(AppController.getInstance().getChatRooms());
        username = AppController.getInstance().getManager(UserManager.class).getUser().getUsername();
        Log.d(TAG, "username: " + username);
        Log.d(TAG, "chatRooms: " + chatRooms);
//        AppController.getInstance().getManager(FirebaseManager.class).showChatList(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@androidx.annotation.NonNull Task<DataSnapshot> task) {
//                if(task.isSuccessful()){
//                    Log.d(TAG, "onCreate: task " + task.getResult());
//                    for (DataSnapshot chatRoomSnapshot : task.getResult().getChildren()) {
//                        chatroomIds.add(chatRoomSnapshot.toString());
//                    }
//
//                    Log.d(TAG, "onCreate: chatroomIds " + chatroomIds);
//                }
//            }
//        });
        messageListAdapter = new MessageListAdapter(this, username, chatRooms);
        actBinding.recChat.setLayoutManager(new LinearLayoutManager(this));
        actBinding.recChat.setAdapter(messageListAdapter);
        updateVisibility();
    }
    //endregion Initialization

    private void updateVisibility() {
        if (messageListAdapter.getItemCount() > 0) {
            actBinding.recChat.setVisibility(View.VISIBLE);
            actBinding.recEmpty.setVisibility(View.GONE);
        } else {
            actBinding.recChat.setVisibility(View.GONE);
            actBinding.recEmpty.setVisibility(View.VISIBLE);
        }
    }
    //endregion Methods

    //region Extras
    //endregion Extras

}