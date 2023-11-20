package com.example.oldbooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.oldbooks.databinding.ActivityPostDetailsBinding;
import com.example.oldbooks.manager.FirebaseManager;
import com.example.oldbooks.model.Bid;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostDetails extends AppCompatActivity {

    private ActivityPostDetailsBinding actBinding;
    private HashMap<String,Object> postData;
    private Post post = new Post();
    private List<String> imgLinks = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actBinding = ActivityPostDetailsBinding.inflate(getLayoutInflater());
        setContentView(actBinding.getRoot());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("post")) {
            Post post = (Post) intent.getSerializableExtra("post");
        }

        AppController.getInstance().getManager(FirebaseManager.class).DBPostPath.child(post.getPostId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    postData = (HashMap<String, Object>) snapshot.getValue();

                    post.setPostTitle(postData.get("postTitle").toString());
                    actBinding.txtposttitle.setText(post.getPostTitle());
                    snapshot.child("imageURLs").getChildren();

                    for (DataSnapshot childSnapshot : snapshot.child("imageURLs").getChildren()) {
                        imgLinks.add(childSnapshot.toString());
                    }
                    Picasso.get().load(imgLinks.get(0)).into(actBinding.imgbookpost);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //region Method
    public void offerBid() {
        Bid newBid = new Bid();
        if (verifyBid(newBid)) {
            return;
        }
        List<Bid> existingBids = post.getBids();
        int indexOfLowestBid = findIndexOfLowestBid(existingBids);
        if (indexOfLowestBid != -1) {
            existingBids.set(indexOfLowestBid, newBid);
        } else {
            existingBids.add(newBid);
        }
        AppController.getInstance().getManager(FirebaseManager.class).placeBid(this, post.getPostId(), existingBids);
    }

    private int findIndexOfLowestBid(List<Bid> bids) {
        if (!bids.isEmpty()) {
            int lowestBidAmount = bids.get(0).getAmount();
            long earliestTimestamp = bids.get(0).getTimestamp();
            int indexOfLowestBid = 0;
            for (int i = 1; i < bids.size(); i++) {
                int currentBidAmount = bids.get(i).getAmount();
                long currentTimestamp = bids.get(i).getTimestamp();

                if (currentBidAmount < lowestBidAmount || (currentBidAmount == lowestBidAmount && currentTimestamp < earliestTimestamp)) {
                    lowestBidAmount = currentBidAmount;
                    earliestTimestamp = currentTimestamp;
                    indexOfLowestBid = i;
                }
            }
            return indexOfLowestBid;
        }
        return -1;
    }
    public boolean verifyBid(Bid newBid){
        return checkIfAmountValid(newBid.getAmount()) &&
                isWithinThreshold(newBid.getAmount()) &&
                isBidHigher(newBid.getAmount());
    }
    private boolean isWithinThreshold(int bidAmount) {
        int lowerThreshold = (int) (post.getPrice() - (post.getPrice() * 0.5));
        int upperThreshold = (int) (post.getPrice() + (post.getPrice() * 0.5));
        return bidAmount >= lowerThreshold && bidAmount <= upperThreshold;
    }
    private boolean isBidHigher(int bidAmount) {
        for (Bid existingBid : post.getBids()) {
            if (bidAmount > existingBid.getAmount()) {
                return true;
            }
        }
        return false;
    }
    private boolean checkIfAmountValid(int bidAmount) {
        try {
            return bidAmount >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    //endregion Method
}