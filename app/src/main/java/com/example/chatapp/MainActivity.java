package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.chatapp.Model.Users;
import com.example.chatapp.Fragments.ProfileFragments;
import com.example.chatapp.Fragments.UserFragments;
import com.example.chatapp.Fragments.chatsFragment;
import com.example.chatapp.Model.Users;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imageView;
    TextView username, profilename;
    DatabaseReference databaseReference;
    FirebaseUser firebaseuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolhome);
        imageView = findViewById(R.id.profile_image);
        username = findViewById(R.id.usernameonmainactivity);
        profilename = findViewById(R.id.username_profile_frag);
        TabLayout tabLayout = findViewById(R.id.tablayout);
        ViewPager viewpager = findViewById(R.id.viewPager);

        ViewpageAdapter viewpageAdapter = new ViewpageAdapter(getSupportFragmentManager());
        viewpageAdapter.addFragements(new UserFragments(),"Users");
        viewpageAdapter.addFragements(new chatsFragment(),"Chats");

        viewpageAdapter.addFragements(new ProfileFragments(),"Notification");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewpager.setAdapter(viewpageAdapter);
        tabLayout.setupWithViewPager(viewpager);
        firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseuser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Users users = snapshot.getValue(Users.class);
                    username.setText(users.getUsername());
                    if(users.getImageURL().equals("default")){
                        imageView.setImageResource(R.drawable.user);
                    }else{
                        Glide.with(getApplicationContext()).load(users.getImageURL()).into(imageView);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() ==R.id.logout){
            FirebaseAuth.getInstance().signOut();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}