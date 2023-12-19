package com.example.oldbooks.manager;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.oldbooks.AppController;
import com.example.oldbooks.activity.MainActivity;
import com.example.oldbooks.Manager;
import com.example.oldbooks.User;
import com.example.oldbooks.model.Bid;
import com.example.oldbooks.model.ChatMessage;
import com.example.oldbooks.model.ChatRoom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class FirebaseManager extends Manager {

    public DatabaseReference DBPostPath = FirebaseDatabase.getInstance().getReference("post");
    public DatabaseReference DBUserPath = FirebaseDatabase.getInstance().getReference("user");
    public DatabaseReference DBChatPath = FirebaseDatabase.getInstance().getReference("user");

    @Override
    public void Initialize() {
        DBChatPath = FirebaseDatabase.getInstance().getReference("user").child(
                AppController.getInstance().getManager(UserManager.class).getUser().getUsername()
        ).child("chatroom");
    }

    public Query onlyFeaturedPosts() {
        return DBPostPath.orderByChild("featured").equalTo(true);
    }

    public Query showChatMessage(String OtherPerson) {
        return DBPostPath.child(OtherPerson);
    }

    public Query getPostsFromCountry(String country) {
        return DBPostPath.orderByChild("country").equalTo(country);
    }

    public Query getUsersWithRole(String role) {
        return DBUserPath.orderByChild("role").equalTo(role);
    }
    public void getChatRoom(String chatroomId, final OnChatRoomFetchedListener listener) {
        DBChatPath.child(chatroomId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Retrieve data from the snapshot
                    String chatroomId = snapshot.child("chatroomId").getValue(String.class);
                    List<String> userIds = (List<String>) snapshot.child("userIds").getValue();
                    long lastMessageTimestamp = snapshot.child("lastMessageTimestamp").getValue(Long.class);
                    String lastSenderId = snapshot.child("lastSenderId").getValue(String.class);
                    String lastMessage = snapshot.child("lastMessage").getValue(String.class);

                    ChatRoom chatRoom = new ChatRoom(chatroomId, userIds, lastMessageTimestamp, lastSenderId, lastMessage);

                    // Notify the listener that the chat room has been fetched
                    if (listener != null) {
                        listener.onChatRoomFetched(chatRoom);
                    }
                } else {
                    // The chat room with the specified ID doesn't exist
                    if (listener != null) {
                        listener.onChatRoomNotFound();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (listener != null) {
                    listener.onChatRoomFetchError(error.getMessage());
                }
            }
        });
    }

    public interface OnChatRoomFetchedListener {
        void onChatRoomFetched(ChatRoom chatRoom);
        void onChatRoomNotFound();
        void onChatRoomFetchError(String errorMessage);
    }

    public void sendChatMessage(ChatRoom chatRoom, ChatMessage message) {

    }

    List<String> favPostIds = new ArrayList<>();
    public List<String> getFavPosts() {
        DBUserPath.child("favPostId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    favPostIds = (List<String>) dataSnapshot.getValue();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
        return favPostIds;
    }

    public void setAsFavPost(String postId){
        getFavPosts();
        if (favPostIds == null || !favPostIds.contains(postId)) {
            if (favPostIds == null) {
                favPostIds = new ArrayList<>();
            }
            favPostIds.add(postId);
            DBUserPath.child("favPostId").setValue(favPostIds);
        }
    }

    public void removeFromFavPost(String postId){
        getFavPosts();
        if (favPostIds != null) {
            if (favPostIds.contains(postId)) {
                favPostIds.remove(postId);
                DBUserPath.child("favPostId").setValue(favPostIds);
            }
        }
    }
    public void placeBid(Context context, String postId, List<Bid> updatedBids){
        DBPostPath.child(postId).child("bids").setValue(updatedBids)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Post bid updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to update post bid", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void addNewUser(Context context, @NonNull User newUser){
        DBUserPath.child(newUser.getUsername()).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                Toast.makeText(context, "User Added into the database", Toast.LENGTH_SHORT).show();
                loginUser(context, newUser);
            }
        });
    }
    public void loginUser(Context context, @NonNull User loggedUser){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("user", loggedUser);
        AppController.userId = loggedUser.getUsername();
        AppController.getInstance().getManager(UserManager.class).setUser(loggedUser);
        AppController.getInstance().getManager(UserManager.class).setInitialized(true);
        context.startActivity(intent);
    }
    public boolean verifySignupCredential(Context context, @NonNull String userId) {
        Task<Boolean> doesUserExistTask = doesUserExist(userId);
        return Tasks.whenAllComplete(doesUserExist(userId),getUserFromDatabase(userId))
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        boolean userExists = doesUserExistTask.getResult();
                        Toast.makeText(context, "UserName already exist", Toast.LENGTH_SHORT).show();
                        return userExists;
                    } else {
                        Toast.makeText(context, "UserName Credential not exist", Toast.LENGTH_SHORT).show();
                        throw task.getException();
                    }
                }).getResult();
    }
    public boolean verifyLoginCredential(Context context, @NonNull String username) {
        Task<User> getUserTask = getUserFromDatabase(username);
        Task<Boolean> doesUserExistTask = doesUserExist(username);
        return Tasks.whenAllComplete(doesUserExist(username),getUserFromDatabase(username))
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        User user = getUserTask.getResult();
                        boolean userExists = doesUserExistTask.getResult();
                        Toast.makeText(context, "Login Credential Verified", Toast.LENGTH_SHORT).show();
                        return userExists && user != null;
                    } else {
                        Toast.makeText(context, "Login Credential not Verified", Toast.LENGTH_SHORT).show();
                        throw task.getException();
                    }
                }).getResult();
    }
    public Task<User> getUserFromDatabase(String username) {
        return DBUserPath.child(username).get().continueWith(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    return dataSnapshot.getValue(User.class);
                } else {
                    return null;
                }
            } else {
                throw task.getException();
            }
        });
    }
    public Task<Boolean> doesUserExist(String userId) {
        return DBUserPath.child(userId).get().continueWith(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                return dataSnapshot.exists();
            } else {
                throw task.getException();
            }
        });
    }
}
