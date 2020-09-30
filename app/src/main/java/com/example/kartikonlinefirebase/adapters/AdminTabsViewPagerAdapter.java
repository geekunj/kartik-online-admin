package com.example.kartikonlinefirebase.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class AdminTabsViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<Fragment> fragments;

    public AdminTabsViewPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragments) {
        super(fm);

        this.context = context;
        this.fragments = fragments;

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
