package com.example.chatapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Adapters.OnItemClick;
import com.example.chatapp.Model.Chats;
import com.example.chatapp.Model.Users;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private Context mContext;
    private List<Users> mUsers;
    private boolean ischat;
    private OnItemClick onItemClick;
    private FirebaseUser firebaseUser;

    Typeface MR,MRR;
    String theLastMessage;

    public SearchAdapter(Context mContext, OnItemClick onItemClick, List<Users> mUsers, boolean ischat){
        this.onItemClick = onItemClick;
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.ischat = ischat;



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layoutofusers, parent, false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Users user = mUsers.get(position);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        holder.username.setText(user.getUsername());
        if (user.getId().equals(firebaseUser.getUid())){
            holder.btn_follow.setVisibility(View.GONE);
        }

        if (user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.drawable.user);
        } else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        }


    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        private TextView last_msg;
        public Button btn_follow;

        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username_userfrag);
            profile_image = itemView.findViewById(R.id.image_user_userfrag);
            img_on = itemView.findViewById(R.id.image_online);
            img_off = itemView.findViewById(R.id.image_offline);
            last_msg = itemView.findViewById(R.id.lastMessage);
            btn_follow = itemView.findViewById(R.id.follow);
        }
    }

    //check for last message


}
