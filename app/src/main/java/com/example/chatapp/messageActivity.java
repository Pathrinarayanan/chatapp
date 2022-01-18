package com.example.chatapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatapp.Adapters.HappyAdapter;
import com.example.chatapp.Adapters.MessageAdapter;
import com.example.chatapp.Adapters.angryAdapter;
import com.example.chatapp.Adapters.attachedAdapter;
import com.example.chatapp.Adapters.brokenAdapter;
import com.example.chatapp.Adapters.confusedAdapter;
import com.example.chatapp.Adapters.excitedAdapter;
import com.example.chatapp.Adapters.highAdapter;
import com.example.chatapp.Adapters.romanticAdapter;
import com.example.chatapp.Adapters.sadAdapter;
import com.example.chatapp.Fragments.APIService;
import com.example.chatapp.Model.Chats;
import com.example.chatapp.Model.Users;
import com.example.chatapp.Notifications.Client;
import com.example.chatapp.Notifications.Data;
import com.example.chatapp.Notifications.MyResponse;
import com.example.chatapp.Notifications.Sender;
import com.example.chatapp.Notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class messageActivity extends AppCompatActivity {


    String friendid, message, myid;
    ImageView imageViewOnToolbar;
    TextView usernameonToolbar;
    Toolbar toolbar;
    FirebaseUser firebaseUser;

    EditText et_message;
    ImageButton send;
    ImageView moodsetterBtn;

    DatabaseReference reference;

    List<Chats> chatsList;
    MessageAdapter messageAdapter;
    HappyAdapter happyAdapter;
    RecyclerView recyclerView;
    ValueEventListener seenlistener;
    APIService apiService;
    RelativeLayout leftchats;
    Boolean notify = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        toolbar = findViewById(R.id.toolbar_message);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // receivermessage = findViewById(R.id.show_messagel);
        imageViewOnToolbar = findViewById(R.id.profile_image_toolbar_message);
        usernameonToolbar = findViewById(R.id.username_ontoolbar_message);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        recyclerView = findViewById(R.id.recyclerview_messages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        send = findViewById(R.id.send_messsage_btn);
        et_message = findViewById(R.id.edit_message_text);
        moodsetterBtn = findViewById(R.id.moodsetter);
        leftchats = findViewById(R.id.leftchat);



        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myid = firebaseUser.getUid(); // my id or the one who is loggedin

        friendid = getIntent().getStringExtra("friendid"); // retreive the friendid when we click on the item

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(friendid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users users = snapshot.getValue(Users.class);

                usernameonToolbar.setText(users.getUsername()); // set the text of the user on textivew in toolbar

                if (users.getImageURL().equals("default")) {

                    imageViewOnToolbar.setImageResource(R.drawable.user);
                } else {

                    Glide.with(getApplicationContext()).load(users.getImageURL()).into(imageViewOnToolbar);
                }readMessages(myid, friendid, users.getImageURL());
                moodsetterBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //Creating the instance of PopupMenu
                        PopupMenu popup = new PopupMenu(messageActivity.this, moodsetterBtn);
                        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                Toast.makeText(messageActivity.this, ""+ item.getTitle()+" mood is setted!!", Toast.LENGTH_SHORT).show();
                                switch (item.getItemId()){
                                    case R.id.happy:
                                    {
                                        HappyReadMessages(myid, friendid, users.getImageURL());
                                        break;
                                    }
                                    case R.id.sad:{
                                        sadreadMessages(myid, friendid, users.getImageURL());
                                        break;
                                    }
                                    case R.id.romantic:{
                                        RomanticreadMessages(myid, friendid, users.getImageURL());
                                        break;
                                    }
                                    case R.id.high:{
                                        highreadMessages(myid,friendid,users.getImageURL());
                                        break;
                                    }
                                    case R.id.excited:{
                                        excitedreadMessages(myid, friendid, users.getImageURL());
                                        break;

                                    }
                                    case R.id.angry:{
                                        angryreadMessages(myid, friendid, users.getImageURL());
                                        break;

                                    }
                                    case R.id.normalmood:{
                                        readMessages(myid, friendid, users.getImageURL());
                                        break;

                                    }
                                    case R.id.confused:{
                                        confusedreadMessages(myid, friendid, users.getImageURL());
                                        break;

                                    }
                                    case R.id.attached:{
                                        attachedreadMessages(myid, friendid, users.getImageURL());
                                        break;

                                    }
                                    case R.id.broken:{
                                        brokenreadMessages(myid, friendid, users.getImageURL());
                                        break;

                                    }









                                }
                                return true;
                            }
                        });

                        popup.show();//showing popup menu
                    }
                });





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        seenMessage(friendid);


        et_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (s.toString().length() > 0 && s.toString().trim()!="") {

                    send.setEnabled(true);

                } else {

                    send.setEnabled(false);


                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = et_message.getText().toString();

                if (!text.startsWith(" ")) {
                    et_message.getText().insert(0, " ");

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });






        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notify = true;
                message = et_message.getText().toString();
                if(message.trim().equals("")){
                    Toast.makeText(messageActivity.this,"You cannot send the empty message",Toast.LENGTH_SHORT).show();
                }
                else{
                    sendMessage(myid, friendid, message);
                }



                et_message.setText("");


            }
        });




    }



    private void seenMessage(final String friendid) {

        reference = FirebaseDatabase.getInstance().getReference("Chats");


        seenlistener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds: snapshot.getChildren()) {

                    Chats chats = ds.getValue(Chats.class);
                    if(chats.getReciever() !=null && myid !=null && chats.getSender() !=null  && friendid !=null) {
                        if (chats.getReciever().equals(myid) && chats.getSender().equals(friendid)) {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("isseen", true);
                            ds.getRef().updateChildren(hashMap);

                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readMessages(final String myid, final String friendid, final String imageURL) {

        chatsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {

                    Chats chats = ds.getValue(Chats.class);
                    if (chats.getReciever() != null && myid != null && chats.getSender() != null && friendid != null) {
                        if (chats.getSender().equals(myid) && chats.getReciever().equals(friendid) ||
                                chats.getSender().equals(friendid) && chats.getReciever().equals(myid)) {

                            chatsList.add(chats);
                        }

                        messageAdapter = new MessageAdapter(messageActivity.this, chatsList, imageURL);
                        recyclerView.setAdapter(messageAdapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void attachedreadMessages(final String myid, final String friendid, final String imageURL) {

        chatsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {

                    Chats chats = ds.getValue(Chats.class);
                    if (chats.getReciever() != null && myid != null && chats.getSender() != null && friendid != null) {
                        if (chats.getSender().equals(myid) && chats.getReciever().equals(friendid) ||
                                chats.getSender().equals(friendid) && chats.getReciever().equals(myid)) {

                            chatsList.add(chats);
                        }

                       attachedAdapter attachedadapter = new attachedAdapter(messageActivity.this, chatsList, imageURL);
                        recyclerView.setAdapter(attachedadapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void highreadMessages(final String myid, final String friendid, final String imageURL) {

        chatsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {

                    Chats chats = ds.getValue(Chats.class);
                    if (chats.getReciever() != null && myid != null && chats.getSender() != null && friendid != null) {
                        if (chats.getSender().equals(myid) && chats.getReciever().equals(friendid) ||
                                chats.getSender().equals(friendid) && chats.getReciever().equals(myid)) {

                            chatsList.add(chats);
                        }

                        highAdapter highadapter = new highAdapter(messageActivity.this, chatsList, imageURL);
                        recyclerView.setAdapter(highadapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void brokenreadMessages(final String myid, final String friendid, final String imageURL) {

        chatsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {

                    Chats chats = ds.getValue(Chats.class);
                    if (chats.getReciever() != null && myid != null && chats.getSender() != null && friendid != null) {
                        if (chats.getSender().equals(myid) && chats.getReciever().equals(friendid) ||
                                chats.getSender().equals(friendid) && chats.getReciever().equals(myid)) {

                            chatsList.add(chats);
                        }

                        brokenAdapter brokenadapter = new brokenAdapter(messageActivity.this, chatsList, imageURL);
                        recyclerView.setAdapter(brokenadapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void confusedreadMessages(final String myid, final String friendid, final String imageURL) {

        chatsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {

                    Chats chats = ds.getValue(Chats.class);
                    if (chats.getReciever() != null && myid != null && chats.getSender() != null && friendid != null) {
                        if (chats.getSender().equals(myid) && chats.getReciever().equals(friendid) ||
                                chats.getSender().equals(friendid) && chats.getReciever().equals(myid)) {

                            chatsList.add(chats);
                        }

                        confusedAdapter confusedadapter = new confusedAdapter(messageActivity.this, chatsList, imageURL);
                        recyclerView.setAdapter(confusedadapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void excitedreadMessages(final String myid, final String friendid, final String imageURL) {

        chatsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {

                    Chats chats = ds.getValue(Chats.class);
                    if (chats.getReciever() != null && myid != null && chats.getSender() != null && friendid != null) {
                        if (chats.getSender().equals(myid) && chats.getReciever().equals(friendid) ||
                                chats.getSender().equals(friendid) && chats.getReciever().equals(myid)) {

                            chatsList.add(chats);
                        }

                        excitedAdapter excitedadapter = new excitedAdapter(messageActivity.this, chatsList, imageURL);
                        recyclerView.setAdapter(excitedadapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void RomanticreadMessages(final String myid, final String friendid, final String imageURL) {

        chatsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {

                    Chats chats = ds.getValue(Chats.class);
                    if (chats.getReciever() != null && myid != null && chats.getSender() != null && friendid != null) {
                        if (chats.getSender().equals(myid) && chats.getReciever().equals(friendid) ||
                                chats.getSender().equals(friendid) && chats.getReciever().equals(myid)) {

                            chatsList.add(chats);
                        }

                        romanticAdapter romanticadapter = new romanticAdapter(messageActivity.this, chatsList, imageURL);
                        recyclerView.setAdapter(romanticadapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void sadreadMessages(final String myid, final String friendid, final String imageURL) {

        chatsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {

                    Chats chats = ds.getValue(Chats.class);
                    if (chats.getReciever() != null && myid != null && chats.getSender() != null && friendid != null) {
                        if (chats.getSender().equals(myid) && chats.getReciever().equals(friendid) ||
                                chats.getSender().equals(friendid) && chats.getReciever().equals(myid)) {

                            chatsList.add(chats);
                        }

                        sadAdapter sadadapter = new sadAdapter(messageActivity.this, chatsList, imageURL);
                        recyclerView.setAdapter(sadadapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void angryreadMessages(final String myid, final String friendid, final String imageURL) {

        chatsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {

                    Chats chats = ds.getValue(Chats.class);
                    if (chats.getReciever() != null && myid != null && chats.getSender() != null && friendid != null) {
                        if (chats.getSender().equals(myid) && chats.getReciever().equals(friendid) ||
                                chats.getSender().equals(friendid) && chats.getReciever().equals(myid)) {

                            chatsList.add(chats);
                        }

                        angryAdapter angryadapter = new angryAdapter(messageActivity.this, chatsList, imageURL);
                        recyclerView.setAdapter(angryadapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void HappyReadMessages(final String myid, final String friendid, final String imageURL) {

        chatsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {

                    Chats chats = ds.getValue(Chats.class);
                    if (chats.getReciever() != null && myid != null && chats.getSender() != null && friendid != null) {
                        if (chats.getSender().equals(myid) && chats.getReciever().equals(friendid) ||
                                chats.getSender().equals(friendid) && chats.getReciever().equals(myid)) {

                            chatsList.add(chats);
                        }

                        happyAdapter = new HappyAdapter(messageActivity.this, chatsList, imageURL);
                        recyclerView.setAdapter(happyAdapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendMessage(final String myid, final String friendid, final String message) {


         DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myid);
        hashMap.put("reciever", friendid);
        hashMap.put("message", message);
        hashMap.put("isseen", false);

        reference.child("Chats").push().setValue(hashMap);


        final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Chatslist").child(myid).child(friendid);

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if (!snapshot.exists()) {
                    reference1.child("id").setValue(friendid);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final String msg = message;
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                sendNotification(friendid,user.getUsername(),msg);
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendNotification(String receiver, final String username, final String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(), R.drawable.app_symbol, username+": "+message, "New Message",
                            friendid);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            //Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void Status (final String status) {


        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete: {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure you want to delete this chat? \nAll the chat will be cleared for both");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Toast.makeText(messageActivity.this,"Message deleted",Toast.LENGTH_LONG).show();
                                        deleteMsg();
                                        deleteFriendMsg();
                                    }
                                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            }
            case R.id.block: {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure you want to block this chat?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(messageActivity.this,"Blocked",Toast.LENGTH_LONG).show();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

                return true;


            }
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        Status("online");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Status("offline");
        reference.removeEventListener(seenlistener);
    }
    private void deleteMsg() {
        final String myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //String msgtimestmp = list.get(position).getTimestamp();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Chats");
        Query query = dbref.orderByChild("reciever").equalTo(friendid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.child("sender").getValue().equals(myuid)) {
                        dataSnapshot1.getRef().removeValue();
}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void deleteFriendMsg() {
        final String myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Chats");
        Query query = dbref.orderByChild("reciever").equalTo(myuid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    if (dataSnapshot2.child("sender").getValue().equals(friendid)) {
                        dataSnapshot2.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}