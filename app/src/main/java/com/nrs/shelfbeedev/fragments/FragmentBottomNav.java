package com.nrs.shelfbeedev.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nrs.shelfbeedev.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FragmentBottomNav extends android.support.v4.app.Fragment {


    @BindView(R.id.fragemntBottomNaviagtion) BottomNavigationView mBottomNaviationView;
    private Unbinder mUnbinder;
    private FragmentAllTransaction mFragmentAllTransaction;
    private FragmentPendingTransaction mFragmentpendingTransaction;


    public FragmentBottomNav() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_fragment_bottom_nav, container, false);
        mUnbinder = ButterKnife.bind(this,v);
        addFragments(savedInstanceState);
        navClickListener();
        return v;
    }

    private void addFragments(Bundle savedInstanceState) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            mFragmentAllTransaction = new FragmentAllTransaction();
            mFragmentpendingTransaction = new FragmentPendingTransaction();
            ft.add(R.id.fragBtmNavConatiner, mFragmentpendingTransaction);
            ft.add(R.id.fragBtmNavConatiner, mFragmentAllTransaction);
            ft.hide(mFragmentAllTransaction);
            ft.show(mFragmentpendingTransaction);
            ft.commit();
        }
    }

    private void navClickListener(){
      mBottomNaviationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              FragmentTransaction ft = getFragmentManager().beginTransaction();
              switch (item.getItemId()){
                  case R.id.bottomMenuAll:
                      if(mFragmentAllTransaction.isHidden()){
                          ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                          ft.show(mFragmentAllTransaction);
                          ft.hide(mFragmentpendingTransaction);
                      }
                      break;
                  case R.id.bottomMenuPending:
                      if(mFragmentpendingTransaction.isHidden()){
                          ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                          ft.hide(mFragmentAllTransaction);
                          ft.show(mFragmentpendingTransaction);
                      }
                      break;
              }
              ft.commit();
              return false;
          }
      });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
