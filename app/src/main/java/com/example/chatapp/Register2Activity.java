package com.example.chatapp;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Register2Activity extends AppCompatActivity {
    Button register2nextBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
    register2nextBtn= findViewById(R.id.register_2_next);
     register2nextBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent i=new Intent(Register2Activity.this, com.example.chatapp.Register3Activity.class);
             startActivity(i);
         }
     });

                }

        }

        //start thread


