package com.example.chatapp.Adapters;

import static com.example.chatapp.R.*;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Adapters.OnItemClick;
import com.example.chatapp.Model.Chats;
import com.example.chatapp.Model.Requests;
import com.example.chatapp.Model.Users;
import com.example.chatapp.R;
import com.example.chatapp.R.drawable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private List<Requests>mrequests;
    DatabaseReference reqreference,friendreference;
    Button btn_follow,btn_decline;
    private boolean ischat;
    private OnItemClick onItemClick;
    private FirebaseUser firebaseUser;
    public  String current_state = "nothing_happend";


    public SearchAdapter(Context mContext, OnItemClick onItemClick, List<Users> mUsers, boolean ischat){
        this.onItemClick = onItemClick;
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.ischat = ischat;



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(layout.layoutofusers, parent, false);
        reqreference = FirebaseDatabase.getInstance().getReference().child("Requests");
        friendreference = FirebaseDatabase.getInstance().getReference().child("Connections");
        btn_follow = view.findViewById(id.follow);
        btn_decline = view.findViewById(id.decline);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

         Users user = mUsers.get(position);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        holder.username.setText(user.getUsername());
        if (user.getId().equals(firebaseUser.getUid())){
            holder.btn_follow.setVisibility(View.GONE);
        }

        if (user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(drawable.user);
        } else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        }


        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(current_state.equals("nothing_happend")){
                        HashMap hashMap = new HashMap();
                        hashMap.put("status","pending");
                        hashMap.put("username",user.getUsername());
                        hashMap.put("imageURL",user.getImageURL());
                        hashMap.put("id",user.getId());
                        hashMap.put("search",user.getSearch());
                        reqreference.child(firebaseUser.getUid()).child(user.getId()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(mContext.getApplicationContext(), "You have Send Friend Request",Toast.LENGTH_SHORT).show();
                                    btn_follow.setText("Requested");
                                    btn_follow.setBackground(mContext.getDrawable(drawable.bluebutton));
                                    current_state = "I_sent_pending";

                                }
                                else {
                                    Toast.makeText(mContext.getApplicationContext(),""+ task.getException().toString(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    if(current_state.equals("I_sent_pending")|| current_state.equals("I_sent_decline")){
                        reqreference.child(firebaseUser.getUid()).child(user.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(mContext.getApplicationContext(), "You have cancelled Friend Request",Toast.LENGTH_SHORT).show();
                                    current_state = "nothing_happend";
                                    btn_follow.setText("connect+");
                                    btn_follow.setBackground(mContext.getDrawable(drawable.mybutton));

                                }
                                else{
                                    Toast.makeText(mContext.getApplicationContext(),""+ task.getException().toString(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    if(current_state.equals("he_sent_pending")){
                        reqreference.child(user.getId()).child(firebaseUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("status","friend");
                                    hashMap.put("username",user.getUsername());
                                    hashMap.put("imageUrl",user.getImageURL());
                                    friendreference.child(firebaseUser.getUid()).child(user.getId()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if(task.isSuccessful()){
                                                friendreference.child(user.getId()).child(firebaseUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        Toast.makeText(mContext.getApplicationContext(), "You added as a Connection",Toast.LENGTH_SHORT).show();
                                                        current_state = "friend";
                                                        btn_follow.setText("Connected");
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                    if(current_state.equals("friend")){
                        //
                    }
                }
            });checkuserExistance(user.getId());
        }

    private void checkuserExistance(String id) {
        friendreference.child(firebaseUser.getUid()).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    current_state = "friend";
                    btn_follow.setText("connected");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        friendreference.child(id).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    current_state = "friend";
                    btn_follow.setText("connected");
                    btn_decline.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reqreference.child(firebaseUser.getUid()).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("status").getValue().toString().equals("pending")){
                        current_state = "I_sent_pending";
                        btn_follow.setText("Requested");
                    }
                    if(snapshot.child("status").getValue().toString().equals("decline")){
                        current_state = "I_sent_decline";
                        btn_follow.setText("Requested");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reqreference.child(id).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("status").getValue().toString().equals("pending")){
                        current_state = "he_sent_pending";
                        btn_follow.setText("accept");
                        btn_decline.setVisibility(View.VISIBLE);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(current_state.equals("nothing_happend")){
            current_state ="nothing_happend";
            btn_follow.setText("connect+");
            btn_decline.setVisibility(View.GONE);
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

            username = itemView.findViewById(id.username_userfrag);
            profile_image = itemView.findViewById(id.image_user_userfrag);
            img_on = itemView.findViewById(id.image_online);
            img_off = itemView.findViewById(id.image_offline);
            btn_follow = itemView.findViewById(id.follow);

        }
    }



    //check for last message


}
