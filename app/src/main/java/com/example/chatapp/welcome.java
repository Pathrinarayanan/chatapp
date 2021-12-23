package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class welcome extends AppCompatActivity {
    Button wcloginButton ;
    Button wcregisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        wcloginButton = findViewById(R.id.wclogin);
        wcregisterButton = findViewById(R.id.wcregister);
        wcloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(welcome.this, com.example.chatapp.LoginActivity.class);
                startActivity(i);
            }
        });
        wcregisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(welcome.this, com.example.chatapp.RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}