package com.nrs.shelfbeedev.fragments.dialogFragments;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nrs.shelfbeedev.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class VerifcationDialogFragment extends android.app.DialogFragment {

    @BindView(R.id.verifyDialogKey) TextInputEditText mVerifyKey;
    @BindView(R.id.verifyDialogSubmit) Button mSubmit;
    private Unbinder mUbinder;

    public VerifcationDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_verifcation_dialog, container, false);
        mUbinder = ButterKnife.bind(this,v);
        listeners();
        return v;
    }

    private void listeners(){
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mVerifyKey.getText().toString().length()<=0||mVerifyKey.getText().toString().isEmpty()){
                    mVerifyKey.setFocusable(true);
                    mVerifyKey.setError("Enter the key to continue");
                }else {
                    if(mVerifyKey.getText().toString().equalsIgnoreCase("1954")){
                        dismiss();
                    }else {
                        mVerifyKey.setText("");
                        mVerifyKey.setFocusable(true);
                        mVerifyKey.setError("Invalid key");
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        mUbinder.unbind();
        super.onDestroy();
    }
}
