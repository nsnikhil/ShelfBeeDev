package com.nrs.shelfbeedev.fragments.dialogFragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nrs.shelfbeedev.R;


public class LoadingDialogFragment extends android.app.DialogFragment {


    public LoadingDialogFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading_dialog, container, false);
    }

}
