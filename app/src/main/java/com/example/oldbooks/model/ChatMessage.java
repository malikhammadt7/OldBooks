package com.example.oldbooks.model;

public class ChatMessage {

    //region Attributes
    private String messageId;
    private String senderId;
    private String message;
    private long timestamp;
    private boolean isRead;
    private boolean isDelivered;
    private boolean isSent;
    //endregion Attributes

    //region Methods
    public ChatMessage() {}
    public ChatMessage(String messageId, String senderId, String message, long timestamp, boolean isRead, boolean isDelivered, boolean isSent) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
        this.isRead = isRead;
        this.isDelivered = isDelivered;
        this.isSent = isSent;
    }
    //endregion Methods

    //region Getter/Setter
    public String getMessageId() {
        return messageId;
    }
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    public String getSenderId() {
        return senderId;
    }
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public boolean isRead() {
        return isRead;
    }
    public void setRead(boolean read) {
        isRead = read;
    }
    public boolean isDelivered() {
        return isDelivered;
    }
    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }
    public boolean isSent() {
        return isSent;
    }
    public void setSent(boolean sent) {
        isSent = sent;
    }
    //endregion Getter/Setter
}
