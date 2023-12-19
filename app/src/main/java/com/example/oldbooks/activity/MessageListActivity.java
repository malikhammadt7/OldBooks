package com.example.oldbooks.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

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

public class MessageListActivity extends AppCompatActivity {

    //region Attributes
    //region Class Constant
    private ActivityMessageListBinding actBinding;
    private Activity activity;
    private final String TAG = "MessageListActivity";
    //endregion Class Constant
    private MessageListAdapter messageListAdapter;
    String otherPerson;
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

        Intent intent = getIntent();
        if(intent != null)
        {
            otherPerson = intent.getStringExtra("selectedPerson");
        }

        FirebaseRecyclerOptions<ChatRoom> options =
                new FirebaseRecyclerOptions.Builder<ChatRoom>()
                        .setQuery(AppController.getInstance().getManager(FirebaseManager.class).showChatMessage(otherPerson), ChatRoom.class)
                        .build();

        messageListAdapter = new ChatMessageAdapter(this, AppController.getInstance().getManager(UserManager.class).getUser().getUsername(),options);
        actBinding.recChat.setAdapter(messageListAdapter);

    }
    //endregion Initialization
    //endregion Methods

    //region Extras
    @Override
    protected void onStart() {
        super.onStart();
        messageListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        messageListAdapter.stopListening();
    }
    //endregion Extras

}