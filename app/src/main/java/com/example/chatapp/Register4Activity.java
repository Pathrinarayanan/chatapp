package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Register4Activity extends AppCompatActivity {
    Button register4btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register4);
        register4btn = findViewById(R.id.register4);
        register4btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Register4Activity.this, com.example.chatapp.MainActivity.class);
                startActivity(i);
            }
        });
    }
}