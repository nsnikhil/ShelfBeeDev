package com.nrs.shelfbeedev.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.nrs.shelfbeedev.R;
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
    private Unbinder mUnbinder;

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
            }
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
