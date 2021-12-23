package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;

public class Register3Activity extends AppCompatActivity {

    EditText et_username, et_password, et_email;
    Button registerbtn;
    ImageView register3back;
    Toolbar toolbar;


    String username, email, password;

    FirebaseAuth mAuth;
    DatabaseReference reference;
    Bundle value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        email = getIntent().getExtras().getString("emailIntent","guest");
        et_username = findViewById(R.id.Registername);
        et_password = findViewById(R.id.registerpassword);
        registerbtn = findViewById(R.id.register3_next);
        register3back = findViewById(R.id.register3_back_button);

        mAuth = FirebaseAuth.getInstance();

        register3back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Register3Activity.this, com.example.chatapp.RegisterActivity.class);
                startActivity(i);
            }
        });


        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                password = et_password.getText().toString();
                username = et_username.getText().toString();


                if (TextUtils.isEmpty(email)) {
                    et_email.setError("Required");
                } else if (TextUtils.isEmpty(username)) {
                    et_username.setError("Required");

                } else if (TextUtils.isEmpty(password)) {
                    et_password.setError("Required");

                } else if (password.length() < 6) {

                    et_password.setError("Length Must Be 6 or more");
                } else {


                    registerUser(username, password, email);
                }

            }
        });





    }

    private void registerUser(final String username, String password, final String email) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseUser user = mAuth.getCurrentUser();

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());


                    if (user!=null) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("username", username);
                        hashMap.put("email", email);
                        hashMap.put("id", user.getUid());
                        hashMap.put("imageURL", "default");
                        hashMap.put("status", "offline");


                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                if (task.isSuccessful()) {

                                    Toast.makeText(Register3Activity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(Register3Activity.this, com.example.chatapp.Register4Activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK ));
                                }
                            }
                        });
                    }

                }

            }
        });

    }


}