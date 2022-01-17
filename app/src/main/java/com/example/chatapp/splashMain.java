package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.chatapp.Adapters.SimpleFragment;


public class splashMain
        extends AppCompatActivity {

    @Override
    protected void onCreate(
            Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_main);

        // Find the view pager that will
        // allow the user to swipe
        // between fragments
        ViewPager viewPager
                = (ViewPager)findViewById(
                R.id.viewpagermain);

        // Create an adapter that
        // knows which fragment should
        // be shown on each page
        SimpleFragment
                adapter
                = new SimpleFragment(
                getSupportFragmentManager());

        // Set the adapter onto
        // the view pager
        viewPager.setAdapter(adapter);
    }
}