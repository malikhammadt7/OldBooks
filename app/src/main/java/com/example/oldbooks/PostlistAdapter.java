package com.example.oldbooks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oldbooks.databinding.RowPostlistBinding;
import com.example.oldbooks.manager.FirebaseManager;
import com.example.oldbooks.manager.UserManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class PostlistAdapter extends FirebaseRecyclerAdapter<Post,PostlistAdapter.CustomViewHolder> {

    public List<String> favposts = new ArrayList<>();
    public PostlistAdapter(@NonNull FirebaseRecyclerOptions<Post> options) {
        super(options);
        favposts = (List<String>) AppController.getInstance().getManager(FirebaseManager.class).onlyFeaturedPosts();

    }
    @Override
    protected void onBindViewHolder(@NonNull CustomViewHolder holder, int position, @NonNull Post model) {
        RedrawView(holder,position,model);
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(RowPostlistBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }
    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public void RedrawView(@NonNull CustomViewHolder holder, int position, @NonNull Post model){
        holder.rowBinding.posttitle.setText(model.getPostTitle());
        holder.rowBinding.location.setText(model.getLocation());
        holder.rowBinding.datetime.setText(model.getDate());
        holder.rowBinding.price.setText(model.getPrice());
        Picasso.get().load(model.getImageURLs().get(0)).into(holder.rowBinding.imgpost);
        holder.rowBinding.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.getInstance().getManager(FirebaseManager.class).removeFromFavPost(model.getPostId());
                holder.rowBinding.favorite.setVisibility(View.INVISIBLE);
                holder.rowBinding.notfavorite.setVisibility(View.VISIBLE);
            }
        });
        holder.rowBinding.notfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppController.getInstance().getManager(FirebaseManager.class).removeFromFavPost(model.getPostId());
                holder.rowBinding.notfavorite.setVisibility(View.INVISIBLE);
                holder.rowBinding.favorite.setVisibility(View.VISIBLE);
            }
        });
        if(isFavPost(model.getPostId()))
        {
            holder.rowBinding.notfavorite.setVisibility(View.INVISIBLE);
            holder.rowBinding.favorite.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.rowBinding.favorite.setVisibility(View.INVISIBLE);
            holder.rowBinding.notfavorite.setVisibility(View.VISIBLE);
        }
    }

    public boolean isFavPost(@NonNull String postId){
        for(String favId: favposts){
            if(postId.equals(favId))
                return true;
        }
        return false;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private RowPostlistBinding rowBinding;
        public CustomViewHolder(RowPostlistBinding rowBinding) {
            super(rowBinding.getRoot());
            this.rowBinding = rowBinding;
        }
    }
}
