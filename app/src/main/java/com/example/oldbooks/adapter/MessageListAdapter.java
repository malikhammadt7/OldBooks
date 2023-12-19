package com.example.oldbooks.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oldbooks.databinding.ItemmodelChatlistBinding;
import com.example.oldbooks.databinding.ItemmodelChatmessageBinding;
import com.example.oldbooks.model.ChatMessage;
import com.example.oldbooks.model.ChatRoom;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MessageListAdapter extends FirebaseRecyclerAdapter<ChatRoom,MessageListAdapter.CustomViewHolder> {

    public Context context;
    String username;

    public MessageListAdapter(Context context, String username, @NonNull FirebaseRecyclerOptions<ChatRoom> options) {
        super(options);
        this.context = context;
        this.username = username;
    }

    @Override
    protected void onBindViewHolder(@NonNull CustomViewHolder holder, int position, @NonNull ChatRoom model) {
        for (String userid : model.getUserIds()) {
            if(userid != username){
                holder.binding.txtUsername.setText(userid);
            }
        }
        holder.binding.txtLastmessage.setText(model.getLastMessage());

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



