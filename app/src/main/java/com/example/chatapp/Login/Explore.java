package com.example.chatapp.Login;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Explore extends AppCompatActivity {

    TextView welcome_message;
    FirebaseUser user;
    String userid,username;
    FirebaseDatabase mdb;
    DatabaseReference mreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        welcome_message = (TextView) findViewById(R.id.welcomemsg);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mdb = FirebaseDatabase.getInstance();
        mreference = mdb.getReference();

        userid = user.getUid();

        mreference.child("users").child(userid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(Explore.this,"Error on retrieval :"+task.getException(),Toast.LENGTH_LONG);
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().child("username").getValue()));
                    username = task.getResult().child("username").getValue().toString();
                    welcome_message.setText("welcome "+username);
                }
            }
        });


    }
}