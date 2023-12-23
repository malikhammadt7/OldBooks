package com.example.oldbooks.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

        username = AppController.getInstance().getManager(UserManager.class).getUser().getUsername();

        FirebaseRecyclerOptions<ChatRoom> options =
                new FirebaseRecyclerOptions.Builder<ChatRoom>()
                        .setQuery(AppController.getInstance().getManager(FirebaseManager.class).showChatList(username), ChatRoom.class)
                        .build();

        messageListAdapter = new MessageListAdapter(this, username, options);
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
    @Override
    protected void onStart() {
        super.onStart();
        if (messageListAdapter != null) {
            messageListAdapter.startListening();
        }
        updateVisibility();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (messageListAdapter != null) {
            messageListAdapter.stopListening();
        }
    }
    //endregion Extras

}