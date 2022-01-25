package com.example.chatapp;

import static com.example.chatapp.R.*;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

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

public class chatReqAdapter extends RecyclerView.Adapter<chatReqAdapter.ViewHolder> {

    private Context mContext;
    private List<Users> mUsers;
    private List<Requests>mrequests;
    DatabaseReference reqreference,friendreference;
    ImageView btn_follow;
    Button btn_decline;
    private boolean ischat;
    private OnItemClick onItemClick;
    DatabaseReference mreference;
    private FirebaseUser firebaseUser;
    String usernames, imageurls;
    TextView say_hi_view;
    int row_index=-1;
    public String current_states = "nothing_happend";


    public chatReqAdapter(Context mContext, OnItemClick onItemClick, List<Users> mUsers, boolean ischat){
        this.onItemClick = onItemClick;
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.ischat = ischat;



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(layout.userlayouthi, parent, false);
        reqreference = FirebaseDatabase.getInstance().getReference().child("ChatRequests");
        friendreference = FirebaseDatabase.getInstance().getReference().child("ChatConnections");
        mreference = FirebaseDatabase.getInstance().getReference();
        btn_follow = view.findViewById(id.say_hi_btn);
        btn_decline = view.findViewById(id.ignore_chat);
        say_hi_view=  view.findViewById(id.say_hi_user);
        return new chatReqAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Users user = mUsers.get(position);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        holder.username.setText(user.getUsername());
        if(user.getStatus().equals("pending")){
            current_states ="I_sent_pending";
        }
        else if(user.getStatus().equals("")){
            current_states ="friend";
        }
        else{
            current_states = "nothing_happend";
        }
        if (user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(drawable.user);
        } else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        }
        mreference.child("Users").child(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(mContext.getApplicationContext(), "Error"+task.getException(),Toast.LENGTH_LONG).show();
                }
                else {

                    usernames= task.getResult().child("username").getValue().toString();
                    imageurls = task.getResult().child("imageURL").getValue().toString();


                }
            }
        });
        if(user.getStatus().equals("pending")){
            current_states ="he_sent_pending";
        }
        reqreference.child(firebaseUser.getUid()).child(user.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("status").getValue().toString().equals("pending")){
                        current_states = "he_sent_pending";

                        holder.say_hi_view.setText("accept");
                        holder.btn_decline.setVisibility(View.VISIBLE);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        friendreference.child(user.getId()).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    current_states = "friend";
                    holder.say_hi_view.setText("connected");
                   holder. btn_decline.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        friendreference.child(firebaseUser.getUid()).child(user.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    current_states = "friend";

                    holder.say_hi_view.setText("connected");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reqreference.child(user.getId()).child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("status").getValue().toString().equals("pending")){
                        current_states = "I_sent_pending";
                        holder.say_hi_view.setText("Requested");
                    }
                    if(snapshot.child("status").getValue().toString().equals("decline")){
                        current_states = "I_sent_decline";
                       holder. say_hi_view.setText("Requested");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        holder.btn_follow.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                row_index = holder.getAdapterPosition();

                //Toast.makeText(mContext.getApplicationContext(),""+current_states,Toast.LENGTH_SHORT).show();
              //  Toast.makeText(mContext.getApplicationContext(),""+row_index,Toast.LENGTH_SHORT).show();
                if(holder.say_hi_view.getText().toString().equals("accept")){
                    current_states = "he_sent_pending";
                }

                if(current_states.equals("nothing_happend")  ){
                    HashMap hashMap = new HashMap();
                    hashMap.put("status","pending");
                    hashMap.put("username",usernames);
                    hashMap.put("imageURL",imageurls);
                    hashMap.put("id",user.getId());
                    hashMap.put("frid",firebaseUser.getUid());
                    hashMap.put("search",user.getUsername().toLowerCase());
                    reqreference.child(user.getId()).child(firebaseUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                    Toast.makeText(mContext.getApplicationContext(), "You have Send Friend Request", Toast.LENGTH_SHORT).show();
                                    for(int i =0;i<mUsers.size();i++){
                                        if(i == row_index){
                                            holder.say_hi_view.setText("Requested");
                                            //  say_hi_view.setBackground(mContext.getDrawable(drawable.bluebutton));
                                            current_states = "I_sent_pending";
                                        }
                                    }


                            }
                            else {
                                Toast.makeText(mContext.getApplicationContext(),""+ task.getException().toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                if(current_states.equals("I_sent_pending")|| current_states.equals("I_sent_decline")){

                    reqreference.child(user.getId()).child(firebaseUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(mContext.getApplicationContext(), "You have cancelled Friend Request",Toast.LENGTH_SHORT).show();
                                for(int i =0;i<mUsers.size();i++){
                                    if(i == row_index){
                                        holder.say_hi_view.setText("Say Hi...");
                                        current_states = "nothing_happend";

                                        //  say_hi_view.setBackground(mContext.getDrawable(drawable.bluebutton));
                                    }
                                }

                                //  btn_follow.setVisibility(View.VISIBLE);

                               // btn_follow.setBackground(mContext.getDrawable(drawable.mybutton));

                            }
                            else{
                                Toast.makeText(mContext.getApplicationContext(),""+ task.getException().toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                if(current_states.equals("he_sent_pending")){
                    reqreference.child(firebaseUser.getUid()).child(user.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                HashMap hashMap = new HashMap();
                                hashMap.put("status","friend");
                                hashMap.put("username",user.getUsername());
                                hashMap.put("imageUrl",user.getImageURL());
                                btn_follow.setVisibility(View.VISIBLE);
                                friendreference.child(firebaseUser.getUid()).child(user.getId()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            friendreference.child(user.getId()).child(firebaseUser.getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    Toast.makeText(mContext.getApplicationContext(), "You added as a Connection",Toast.LENGTH_SHORT).show();
                                                    current_states = "friend";
                                                    say_hi_view.setText("Connected");
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
                if(current_states.equals("friend")){
                    //
                }
            }
        });checkuserExistance(user.getId());
    }

    private void checkuserExistance(String id) {

        if(current_states.equals("nothing_happend")){
            current_states ="nothing_happend";

            say_hi_view.setText("Say Hi...");
            btn_decline.setVisibility(View.GONE);
        }
        if(current_states.equals("friend")){
             say_hi_view.setText("connected");
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
        public ImageView btn_follow;
        public  Button btn_decline;
        public TextView say_hi_view ;

        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(id.username_userfrag);
            profile_image = itemView.findViewById(id.image_user_userfrag);
            img_on = itemView.findViewById(id.image_online);
            img_off = itemView.findViewById(id.image_offline);
            btn_follow = itemView.findViewById(id.say_hi_btn);
            btn_decline= itemView.findViewById(id.ignore_chat);
            say_hi_view = itemView.findViewById(id.say_hi_user);

        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }




    //check for last message


}
