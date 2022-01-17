package com.example.chatapp.Adapters;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chatapp.Fragments.selection;
import com.example.chatapp.Fragments.splash1;
import com.example.chatapp.Fragments.splash2;
import com.example.chatapp.Fragments.splash3;

public class SimpleFragment extends FragmentPagerAdapter{

    public SimpleFragment(
            FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        if (position == 0) {
            return new splash1();
        }
        else if (position == 1) {
            return new splash2();
        }
        else if(position ==2){
            return new splash3();
        }
        else{
            return new selection();
        }
    }

    @Override
    public int getCount()
    {
        return 4;
    }
}