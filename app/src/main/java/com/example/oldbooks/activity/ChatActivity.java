package com.example.oldbooks.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.oldbooks.AppController;
import com.example.oldbooks.Post;
import com.example.oldbooks.R;
import com.example.oldbooks.adapter.ChatMessageAdapter;
import com.example.oldbooks.adapter.PostlistAdapter;
import com.example.oldbooks.databinding.ActivityChatBinding;
import com.example.oldbooks.manager.FirebaseManager;
import com.example.oldbooks.manager.UserManager;
import com.example.oldbooks.model.ChatMessage;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ChatActivity extends AppCompatActivity {

    //region Attributes
    //region Class Constant
    private ActivityChatBinding actBinding;
    private Activity activity;
    private final String TAG = "ChatActivity";
    //endregion Class Constant
    private ChatMessageAdapter chatMessageAdapter;
    String otherPerson;
    //endregion Attributes

    //region Methods
    //region Initialization
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(actBinding.getRoot());
        activity = this;
        AppController.getInstance().setCurrentActivity(activity);

        Intent intent = getIntent();
        if(intent != null)
        {
            otherPerson = intent.getStringExtra("selectedPerson");
        }

        FirebaseRecyclerOptions<ChatMessage> options =
                new FirebaseRecyclerOptions.Builder<ChatMessage>()
                        .setQuery(AppController.getInstance().getManager(FirebaseManager.class).showChatMessage(otherPerson), ChatMessage.class)
                        .build();

        chatMessageAdapter = new ChatMessageAdapter(this, AppController.getInstance().getManager(UserManager.class).getUser().getUsername(),options);
        actBinding.recChat.setAdapter(chatMessageAdapter);
    }
    //endregion Initialization
    //endregion Methods

    //region Extras
    @Override
    protected void onStart() {
        super.onStart();
        chatMessageAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatMessageAdapter.stopListening();
    }
    //endregion Extras

}