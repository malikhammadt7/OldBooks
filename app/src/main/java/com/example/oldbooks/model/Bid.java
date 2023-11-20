package com.example.oldbooks.model;

import com.example.oldbooks.AppController;
import com.example.oldbooks.Enums;
import com.example.oldbooks.manager.UserManager;

import java.io.Serializable;

public class Bid implements Serializable {
    private String bidId;
    private String bidderId;
    private int amount;
    private long timestamp;
    private Enums.BidStatus bidStatus;

    public Bid() {
        setBidderId(AppController.getInstance().getManager(UserManager.class).getUserId());
        setAmount(0);
        setTimestamp(System.currentTimeMillis());
        setBidStatus(Enums.BidStatus.PENDING);
    }
    public Bid(String bidId, String bidderId, int amount, long timestamp, Enums.BidStatus bidStatus) {
        this.bidId = bidId;
        this.bidderId = bidderId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.bidStatus = bidStatus;
    }

    public String getBidId() {
        return bidId;
    }
    public void setBidId(String bidId) {
        this.bidId = bidId;
    }
    public String getBidderId() {
        return bidderId;
    }
    public void setBidderId(String bidderId) {
        this.bidderId = bidderId;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public Enums.BidStatus getBidStatus() {
        return bidStatus;
    }
    public void setBidStatus(Enums.BidStatus bidStatus) {
        this.bidStatus = bidStatus;
    }
}
