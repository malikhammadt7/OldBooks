package com.example.oldbooks;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;

import com.example.oldbooks.manager.CoinManager;
import com.example.oldbooks.manager.DialogManager;
import com.example.oldbooks.manager.FirebaseManager;
import com.example.oldbooks.manager.UserManager;
import com.example.oldbooks.model.ChatMessage;
import com.example.oldbooks.model.ChatRoom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AppController {

    //region Singleton
    private static AppController instance;
    public static synchronized AppController getInstance() {
        if (instance == null) {
            instance = new AppController();
        }
        return instance;
    }
    //endregion Singleton

    //region Attributes
    private ChatRoom chatRoom;
    private List<String> chatroomIds = new ArrayList<>();
    private List<ChatRoom> chatRooms = new ArrayList<>();
    private Activity currentActivity;
    //endregion Attributes

    //region Initialization
    private AppController() {
        addManager(FirebaseManager.class, new FirebaseManager());
        addManager(CoinManager.class, new CoinManager());
        addManager(UserManager.class, new UserManager());
    }
    public void initialize() {
        getManager(UserManager.class).Initialize();
        getManager(FirebaseManager.class).Initialize();
    }
    //endregion Initialization


    //region Actions
    private Map<Class<?>, Manager> managerMap = new HashMap<>();
    private void addManager(Class<?> managerClass, Manager manager) {
        managerMap.put(managerClass, manager);
    }
    @SuppressWarnings("unchecked")
    public <T extends Manager> T getManager(Class<T> managerClass) {
        return (T) managerMap.get(managerClass);
    }
    //endregion Actions


    //region Methods
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }
    public static boolean IsGuestView(){
        try {
            return AppController.getInstance().getManager(UserManager.class).isInitialized();
        } catch (Exception e) {
            return false;
        }
    }
    public void FillChatRoomsList(){
        for (String chatRoomId : chatroomIds) {
            getManager(FirebaseManager.class).showChatRoom(chatRoomId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@androidx.annotation.NonNull Task<DataSnapshot> task) {
                    Log.d(TAG, "MessageListAdapter: chatRoomIds 3 " + task.getResult().getValue() + " ");
                    if(task.isSuccessful()){
                        ChatRoom chatRoom = fromDataSnapshot(task.getResult());
                        if (chatRoom != null) {
                            chatRooms.add(chatRoom);
                            Log.d(TAG, "MessageListAdapter: task.getResult() " + chatRoom + " " + task.getResult());
                            System.out.println(chatRoom);
                        } else {
                            System.out.println("DataSnapshot does not exist or is invalid.");
                        }
                    }else{
                        Log.d(TAG, "MessageListAdapter: chatRoomIds 4");
                    }
                }
            });
        }
    }
    public void LoadChatRooms(){
        try {
            String username = AppController.getInstance().getManager(UserManager.class).getUser().getUsername();
            Log.d(TAG, "username: " + username);
            AppController.getInstance().getManager(FirebaseManager.class).showChatList(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@androidx.annotation.NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "onCreate: task " + task.getResult());
                        for (DataSnapshot chatRoomSnapshot : task.getResult().getChildren()) {
                            chatroomIds.add(chatRoomSnapshot.getValue().toString());
                        }
                        FillChatRoomsList();
                    }
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "LoadChatRooms " + e);
        }
    }
    public static ChatRoom fromDataSnapshot(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            String chatroomId = dataSnapshot.getKey();

            Map<String, Object> dataMap = (Map<String, Object>) dataSnapshot.getValue();

            List<String> userIds = (List<String>) dataMap.get("userIds");
            long lastMessageTimestamp = (long) dataMap.get("lastMessageTimestamp");
            String postId = (String) dataMap.get("postId");
            String lastSenderId = (String) dataMap.get("lastSenderId");
            String lastMessage = (String) dataMap.get("lastMessage");

            List<Map<String, Object>> messagesData = (List<Map<String, Object>>) dataMap.get("messages");
            List<ChatMessage> messages = new ArrayList<>();
            if (messagesData != null) {
                for (Map<String, Object> messageData : messagesData) {
                    long timestamp = (long) messageData.get("timestamp");
                    String senderId = (String) messageData.get("senderId");
                    boolean delivered = (boolean) messageData.get("delivered");
                    String messageText = (String) messageData.get("message");
                    boolean sent = (boolean) messageData.get("sent");
                    boolean read = (boolean) messageData.get("read");

                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setTimestamp(timestamp);
                    chatMessage.setSenderId(senderId);
                    chatMessage.setDelivered(delivered);
                    chatMessage.setMessage(messageText);
                    chatMessage.setSent(sent);
                    chatMessage.setRead(read);
                    messages.add(chatMessage);
                }
            }

            return new ChatRoom(chatroomId, userIds, messages, lastMessageTimestamp, postId, lastSenderId, lastMessage);
        } else {
            // Handle the case where the DataSnapshot does not exist
            return null;
        }
    }
    //endregion Methods

    //region Extras
    public static String convertTimestampToDateTime(long timestamp) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date(timestamp);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error converting timestamp";
        }
    }
    public static String getRelativeTime(long timestamp) {
        try {
            long now = getCurrentTimestamp();
            long differenceMillis = now - timestamp;

            // Convert milliseconds to minutes, hours, and days
            long differenceMinutes = differenceMillis / (60 * 1000);
            long differenceHours = differenceMillis / (60 * 60 * 1000);
            long differenceDays = differenceMillis / (24 * 60 * 60 * 1000);

            if (differenceMinutes < 1) {
                return "just now";
            } else if (differenceMinutes == 1) {
                return "a minute ago";
            } else if (differenceHours < 1) {
                return differenceMinutes + " minutes ago";
            } else if (differenceHours == 1) {
                return "an hour ago";
            } else if (differenceDays < 1) {
                return differenceHours + " hours ago";
            } else if (differenceDays == 1) {
                return "yesterday";
            } else {
                // Format the date and time for older posts
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                Date date = new Date(timestamp);
                return sdf.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error calculating relative time";
        }
    }
    //endregion Extras

    //region Getter/Setter
    public Activity getCurrentActivity() {
        return currentActivity;
    }
    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }
    public ChatRoom getChatRoom() {
        return chatRoom;
    }
    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
    public List<ChatRoom> getChatRooms() {
        return chatRooms;
    }
    public void setChatRooms(List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }
    public List<String> getChatroomIds() {
        return chatroomIds;
    }
    public void setChatroomIds(List<String> chatroomIds) {
        this.chatroomIds = chatroomIds;
    }
    //endregion Getter/Setter

}
