package com.nrs.shelfbeedev.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nrs.shelfbeedev.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FragmentAllUser extends android.support.v4.app.Fragment {

    @BindView(R.id.allUserList) ListView mAllUserList;
    private Unbinder mUnbinder;

    public FragmentAllUser() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_fragment_all_user, container, false);
        mUnbinder = ButterKnife.bind(this,v);
        addFakeList();
        return v;
    }

    private void addFakeList(){
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i=0;i<20;i++){
            arrayList.add("AllUser "+i);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrayList);
        mAllUserList.setAdapter(arrayAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
