package com.example.chatapp;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {
    Button register1nextBtn;
    EditText registeremail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register1nextBtn= findViewById(R.id.register_1_next);
        registeremail = findViewById(R.id.RegisterEmail);
        register1nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(RegisterActivity.this, com.example.chatapp.Register3Activity.class);
                i.putExtra("emailIntent",registeremail.getText().toString());
                startActivity(i);
            }
        });

    }

}

//start thread


