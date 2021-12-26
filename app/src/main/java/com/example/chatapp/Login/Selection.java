package com.example.chatapp.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.R;

public class Selection extends AppCompatActivity {

    Button login,signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        login = (Button) findViewById(R.id.navigatelogin);
        signup = (Button) findViewById(R.id.navigatesignup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Selection.this,Login.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Selection.this, com.example.chatapp.Login.Register.class));
            }
        });
    }
}