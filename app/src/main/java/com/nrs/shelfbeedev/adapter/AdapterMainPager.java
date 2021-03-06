package com.nrs.shelfbeedev.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nrs.shelfbeedev.fragments.FragmentAllTransactions;
import com.nrs.shelfbeedev.fragments.FragmentPendingTransactions;


public class AdapterMainPager extends FragmentStatePagerAdapter {

    private static final CharSequence[] mPageTitle = {"Pending", "All"};

    public AdapterMainPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
       if(position==0){
           return new FragmentPendingTransactions();
       }else if(position==1){
           return new FragmentAllTransactions();
       }
       return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence s = null;
        if (position == 0) {
            s = mPageTitle[0];
        }
        if (position == 1) {
            s = mPageTitle[1];
        }
        return s;
    }
}
