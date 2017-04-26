package com.nrs.shelfbeedev.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.nrs.shelfbeedev.MainActivity;
import com.nrs.shelfbeedev.R;
import com.nrs.shelfbeedev.fragments.dialogFragments.LoadingDialogFragment;
import com.nrs.shelfbeedev.network.VolleySingleton;
import com.nrs.shelfbeedev.object.ObjectUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class UserDetailFragment extends Fragment {

    @BindView(R.id.userInfoId) TextView mUserId;
    @BindView(R.id.userInfoName) TextView mUserName;
    @BindView(R.id.userInfoPhone) TextView mUserPhone;
    @BindView(R.id.userInfoAddress) TextView mUserAdres;
    @BindView(R.id.userInfoFkey) TextView mUserFkey;
    @BindView(R.id.userInfoBan) TextView mUserBan;
    @BindView(R.id.userInfoProgressBar) ProgressBar mUserProgressBar;
    @BindView(R.id.userInfoDelete) Button mUserDelete;
    private Unbinder mUnbinder;
    LoadingDialogFragment mLoadingDialogFragment;

    public UserDetailFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_user_detail, container, false);
        mUnbinder = ButterKnife.bind(this,v);
        initialize();
        return v;
    }

    private void initialize(){
        if(getActivity().getIntent()!=null){
            if(getActivity().getIntent().getExtras().getString(getActivity().getResources().getString(R.string.intentUserId))!=null){
                getUserData(getActivity().getIntent().getExtras().getString(getActivity().getResources().getString(R.string.intentUserId)));
                setVisibility("106976353677630903210");
                setVisibility("107941561608729776156");
            }
        }
        mUserDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder delete = new AlertDialog.Builder(getActivity());
                delete.setTitle("Warning").setMessage("Are you sure you want to remove this user from database" +"\n"+"This will also remove all the books and request from the user" +
                ", this action is irreversible")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAll();
                    }
                });
                delete.create().show();
            }
        });
    }

    private void deleteAll(){
        mLoadingDialogFragment = new LoadingDialogFragment();
        mLoadingDialogFragment.show(getFragmentManager(),"wait");
        String uid = getActivity().getIntent().getExtras().getString(getActivity().getResources().getString(R.string.intentUserId));
        volleyDelete(buildAllDeleteUri(uid,getActivity().getResources().getString(R.string.urlDeleteUserBooks)),0);
        volleyDelete(buildAllDeleteUri(uid,getActivity().getResources().getString(R.string.urlDeleteUserRequest)),0);
        volleyDelete(buildAllDeleteUri(uid,getActivity().getResources().getString(R.string.urlDeleteUser)),1);
    }

    private String buildAllDeleteUri(String uid,String phpfile){
        String mHostName = getResources().getString(R.string.urlServerLink);
        String url = mHostName + phpfile;
        String requestIdQuery = "uid";
        return Uri.parse(url).buildUpon().appendQueryParameter(requestIdQuery, uid).toString();
    }

    private void volleyDelete(String url, final int key){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
                if(key==1){
                    mLoadingDialogFragment.dismiss();
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void setVisibility(String uid){
        if(getActivity().getIntent().getExtras().getString(getActivity().getResources().getString(R.string.intentUserId)).equalsIgnoreCase(uid)){
            mUserDelete.setVisibility(View.GONE);
        }
    }

    private String buildUserUri(String uid){
        String mHostName = getResources().getString(R.string.urlServerLink);
        String mAllUserLink = getResources().getString(R.string.urlUserQueryUid);
        String url = mHostName + mAllUserLink;
        String uidQuery = "uid";
        return Uri.parse(url).buildUpon().appendQueryParameter(uidQuery, String.valueOf(uid)).toString();
    }

    private void getUserData(String userId){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, buildUserUri(userId), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    setUserData(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);
    }

    private void setUserData(JSONArray response) throws JSONException {
        mUserProgressBar.setVisibility(View.GONE);
        if(response.length()>0){
            for(int i=0;i<response.length();i++){
                JSONObject object = response.getJSONObject(i);
                mUserId.setText("User ID : " + object.getString("uid"));
                mUserName.setText("Name : "+object.getString("name"));
                mUserPhone.setText("Phone No : "+object.getString("phoneno"));
                mUserAdres.setText("Address : "+object.getString("address"));
                mUserFkey.setText("Firbase Key : "+object.getString("fkey"));
                mUserBan.setText("Ban Status : "+object.getString("bstatus"));
            }
        }
    }

    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }
}
