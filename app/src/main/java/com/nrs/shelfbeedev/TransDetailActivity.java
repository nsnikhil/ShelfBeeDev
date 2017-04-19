package com.nrs.shelfbeedev;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nrs.shelfbeedev.object.ObjectBookTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransDetailActivity extends AppCompatActivity {

    @BindView(R.id.detailToolbar) Toolbar mdetailsToolBar;
    @BindView(R.id.detailBuyerName) TextView mBuyerName;
    @BindView(R.id.detailBuyerPhone) TextView mBuyerPhone;
    @BindView(R.id.detailBuyerAddress) TextView mBuyerAddress;
    @BindView(R.id.detailBuyerDate) TextView mBuyerDate;
    @BindView(R.id.detailSellerName) TextView mSellerrName;
    @BindView(R.id.detailSellerPhone) TextView mSellerPhone;
    @BindView(R.id.detailSellerAddress) TextView mSellerAddress;
    @BindView(R.id.detailSellerDate) TextView mSellerDate;
    @BindView(R.id.detailsDone) Button mDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_detail);
        ButterKnife.bind(this);
        initialize();
        if(getIntent().getExtras()!=null){
            ObjectBookTransaction object = (ObjectBookTransaction) getIntent().getExtras().getSerializable(getResources().getString(R.string.bundleSerialKey));
            Toast.makeText(getApplicationContext(),"Buyer id : "+object.getBuyerId(),Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"Seller id : "+object.getUserSellerId(),Toast.LENGTH_SHORT).show();
        }
    }

    private void setBuyerData(){

    }

    private void setSellerData(){

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initialize() {
        setSupportActionBar(mdetailsToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
