package com.nrs.shelfbeedev;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.nrs.shelfbeedev.network.VolleySingleton;
import com.nrs.shelfbeedev.object.ObjectBookTransaction;
import com.nrs.shelfbeedev.object.ObjectUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
    @BindView(R.id.buyerProgressBar) ProgressBar mBuyerProgressBar;
    @BindView(R.id.sellerProgressBar) ProgressBar mSellerProgressBar;
    ObjectBookTransaction mObjectBookTransaction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_detail);
        ButterKnife.bind(this);
        initialize();
        if(getIntent().getExtras()!=null){
            mObjectBookTransaction = (ObjectBookTransaction) getIntent().getExtras().getSerializable(getResources().getString(R.string.bundleSerialKey));
            getUserData(mObjectBookTransaction.getBuyerId().trim(),0);
            getUserData(mObjectBookTransaction.getUserSellerId().trim(),1);
            mSellerDate.setText("Sold on : " +makeDate(mObjectBookTransaction.getBuytime()));
            mBuyerDate.setText("Delivery Date : " +makeDeliveryDate(mObjectBookTransaction.getBuytime()));
            if(Integer.parseInt(mObjectBookTransaction.getTransStatus())==1){
                mDone.setEnabled(false);
                mDone.setText("Book Delivered");
            }
        }
    }

    private String makeDate(String time){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(time));
        return formatter.format(calendar.getTime());
    }

    private String makeDeliveryDate(String time){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(time));
        calendar.add(Calendar.DATE,7);
        return formatter.format(calendar.getTime());
    }

    private String buildUserUri(String userId){
        String mHostName = getResources().getString(R.string.urlServerLink);
        String mAllUserLink = getResources().getString(R.string.urlUserSingle);
        String url = mHostName+mAllUserLink;
        String userIdQuery = "uid";
        return Uri.parse(url).buildUpon().appendQueryParameter(userIdQuery,userId).toString();
    }

    private void getUserData(String userId, final int key){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,buildUserUri(userId), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(key==0){
                    try {
                        setBuyerData(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        setSellerData(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

    private void setBuyerData(JSONArray jsonArray) throws JSONException {
        mBuyerProgressBar.setVisibility(View.GONE);
        if(jsonArray.length()>0){
            for(int i=0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                String uid = object.getString("uid").trim();
                String nm =object.getString("name").trim();
                String phn =object.getString("phoneno").trim();
                String adr =object.getString("address").trim();
                String fk =object.getString("fkey").trim();
                String bst =object.getString("bstatus").trim();
                mBuyerName.setText(nm);
                mBuyerPhone.setText(phn);
                mBuyerAddress.setText(adr);
            }
        }
    }

    private void setSellerData(JSONArray jsonArray) throws JSONException {
        mSellerProgressBar.setVisibility(View.GONE);
        if(jsonArray.length()>0){
            for(int i=0;i<jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                String uid = object.getString("uid").trim();
                String nm =object.getString("name").trim();
                String phn =object.getString("phoneno").trim();
                String adr =object.getString("address").trim();
                String fk =object.getString("fkey").trim();
                String bst =object.getString("bstatus").trim();
                mSellerrName.setText(nm);
                mSellerPhone.setText(phn);
                mSellerAddress.setText(adr);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void makeUpdateStatusId(){
        String mHostName = getResources().getString(R.string.urlServerLink);
        String mAllUserLink = getResources().getString(R.string.urlTransactionDone);
        String url = mHostName+mAllUserLink;
        String bookIdQuery = "bid";
        String finalUrl = Uri.parse(url).buildUpon().appendQueryParameter(bookIdQuery,String.valueOf(mObjectBookTransaction.getBookid())).toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                finish();
                startActivity(new Intent(TransDetailActivity.this,MainActivity.class));
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void initialize() {
        setSupportActionBar(mdetailsToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder done = new AlertDialog.Builder(TransDetailActivity.this);
                done.setMessage("Is the book delivered successfully");
                done.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        makeUpdateStatusId();
                    }
                });
                done.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                done.create().show();
            }
        });
    }
}
