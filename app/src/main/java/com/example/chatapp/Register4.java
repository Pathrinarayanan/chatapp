package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.chatapp.Login.UserActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register4 extends AppCompatActivity {
    Button getstartedbtn;
    TextInputEditText about;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register4_layout);
        getstartedbtn = findViewById(R.id.getstarted);
        about = findViewById(R.id.profileaddabout);

        getstartedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (about.getText().toString().trim() != null) {
                    reference = FirebaseDatabase.getInstance().getReference();

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                    if (user != null) {

                        Intent intent = getIntent();
                        HashMap<String, Object> newHashMap = (HashMap<String, Object>)       intent.getSerializableExtra("hashMapKey");
                        HashMap<String, Object> hashMap=(HashMap<String, Object>)newHashMap.get("key");
                        hashMap.put("about", about.getText().toString());
                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                if (task.isSuccessful()) {

                                    Toast.makeText(Register4.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(Register4.this, com.example.chatapp.MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                }
                            }
                        });
                    } else {
                        Toast.makeText(Register4.this, "About is not empty!!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }
}