package com.nrs.shelfbeedev.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nrs.shelfbeedev.R;
import com.nrs.shelfbeedev.adapter.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FragmentViewPager extends android.support.v4.app.Fragment {

    @BindView(R.id.fragmentViewPager) ViewPager mViewPager;
    @BindView(R.id.fragmentTabLayout) TabLayout mTabLayout;
    private Unbinder mUnbinder;
    AdapterMainPager mPagerAdapter;

    public FragmentViewPager() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_view_pager,container,false);
        mUnbinder = ButterKnife.bind(this, v);
        initialize();
        return v;
    }

    private void initialize() {
        mPagerAdapter = new AdapterMainPager(getFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
