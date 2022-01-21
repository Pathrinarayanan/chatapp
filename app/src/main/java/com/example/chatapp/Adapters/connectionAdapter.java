package com.example.chatapp.Adapters;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chatapp.Fragments.Connection;
import com.example.chatapp.Fragments.selection;
import com.example.chatapp.Fragments.splash1;
import com.example.chatapp.Fragments.splash2;
import com.example.chatapp.Fragments.splash3;

public class connectionAdapter extends FragmentPagerAdapter{

    public connectionAdapter(
            FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

            return new Connection();



    }

    @Override
    public int getCount()
    {
        return 1;
    }
}