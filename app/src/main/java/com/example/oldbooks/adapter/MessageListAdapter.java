package com.example.oldbooks.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oldbooks.AppController;
import com.example.oldbooks.activity.ChatActivity;
import com.example.oldbooks.databinding.ItemmodelChatlistBinding;
import com.example.oldbooks.databinding.ItemmodelChatmessageBinding;
import com.example.oldbooks.manager.FirebaseManager;
import com.example.oldbooks.model.ChatMessage;
import com.example.oldbooks.model.ChatRoom;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseException;
import com.squareup.picasso.Picasso;

public class MessageListAdapter extends FirebaseRecyclerAdapter<ChatRoom,MessageListAdapter.CustomViewHolder> {

    public Context context;
    String username;
    private AppController appController;

    public MessageListAdapter(Context context, String username, @NonNull FirebaseRecyclerOptions<ChatRoom> options) {
        super(options);
        this.context = context;
        this.username = username;
        appController = AppController.getInstance();
    }

    @Override
    protected void onBindViewHolder(@NonNull CustomViewHolder holder, int position, @NonNull ChatRoom model) {
        for (String userid : model.getUserIds()) {
            if(userid != username){
                holder.binding.txtUsername.setText(userid);
            }
        }
        try {
            String profileImageUrl = appController.getManager(FirebaseManager.class).getUserProfileImg(holder.binding.txtUsername.getText().toString());
            Picasso.get().load(profileImageUrl).into(holder.binding.imgUser);
            Log.d("ProfileImage", "Received: " + profileImageUrl);
        } catch (DatabaseException e) {
            // Handle errors
            Log.e("ProfileImage", "Error: " + e.getMessage());
        }
        holder.binding.txtLastmessage.setText(model.getLastMessage());
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.getInstance().setChatRoom(model);
                context.startActivity(new Intent(AppController.getInstance().getCurrentActivity(), ChatActivity.class));
            }
        });
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(ItemmodelChatlistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private ItemmodelChatlistBinding binding;
        public CustomViewHolder(ItemmodelChatlistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}



