package com.nrs.shelfbeedev;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.nrs.shelfbeedev.fragments.*;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailConatinerActivity extends AppCompatActivity {

    private static final String NULL_VALUE = "N/A";
    @BindView(R.id.detailContainerToolbar) Toolbar mConatinerToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_conatiner);
        ButterKnife.bind(this);
        initialize();
        attachFragments();
    }

    private void initialize(){
        setSupportActionBar(mConatinerToolBar);
    }

    private void attachFragments(){
        if(getIntent()!=null){
            if(getIntent().getExtras().getString(getResources().getString(R.string.intentUserId))!=null){
                getFragmentManager().beginTransaction().add(R.id.containerUser,new UserDetailFragment()).commit();
            }if(getIntent().getExtras().getInt(getResources().getString(R.string.intentRequestId))!=0){
                getFragmentManager().beginTransaction().add(R.id.containerOther,new RequestDetailFragment()).commit();
                if(getIntent().getExtras().getString(getResources().getString(R.string.intentUserId))!=null){
                    getFragmentManager().beginTransaction().add(R.id.containerUser,new UserDetailFragment()).commit();
                }
            }if(getIntent().getExtras().getInt(getResources().getString(R.string.intentBookId))!=0){
                getFragmentManager().beginTransaction().add(R.id.containerBookOther,new BookDetailFragment()).commit();
                if(getIntent().getExtras().getString(getResources().getString(R.string.intentUserId))!=null){
                    getFragmentManager().beginTransaction().add(R.id.containerUser,new UserDetailFragment()).commit();
                }
            }
        }
    }
}
