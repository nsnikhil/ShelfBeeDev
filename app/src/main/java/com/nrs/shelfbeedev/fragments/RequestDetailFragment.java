package com.nrs.shelfbeedev.fragments;


import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.nrs.shelfbeedev.R;
import com.nrs.shelfbeedev.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RequestDetailFragment extends Fragment {


    @BindView(R.id.requestInfoName)
    TextView mRequestName;
    @BindView(R.id.requestInfoPublisher)
    TextView mRequestPublisher;
    @BindView(R.id.requestInfoRequestId)
    TextView mRequestRid;
    @BindView(R.id.requestInfoProgressBar)
    ProgressBar mRequestProgress;
    private Unbinder mUnbinder;

    public RequestDetailFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_request_detail, container, false);
        mUnbinder = ButterKnife.bind(this, v);
        initialize();
        return v;
    }

    private void initialize(){
        if(getActivity().getIntent()!=null){
            if(getActivity().getIntent().getExtras().getInt(getActivity().getResources().getString(R.string.intentRequestId))!=0){
                getRequestData(getActivity().getIntent().getExtras().getInt(getActivity().getResources().getString(R.string.intentRequestId)));
            }
        }
    }

    private String buildUserUri(int uid) {
        String mHostName = getResources().getString(R.string.urlServerLink);
        String mAllUserLink = getResources().getString(R.string.urlRequestQueryRid);
        String url = mHostName + mAllUserLink;
        String ridQuery = "rid";
        return Uri.parse(url).buildUpon().appendQueryParameter(ridQuery, String.valueOf(uid)).toString();
    }

    private void getRequestData(int userId) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, buildUserUri(userId), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    setRequestData(response);
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

    private void setRequestData(JSONArray response) throws JSONException {
        mRequestProgress.setVisibility(View.GONE);
        if (response.length() > 0) {
            for (int i = 0; i < response.length(); i++) {
                JSONObject object = response.getJSONObject(i);
                mRequestName.setText("Book Name : "+object.getString("name"));
                mRequestPublisher.setText("Publisher : "+object.getString("publisher"));
                mRequestRid.setText("Request id : "+object.getInt("rid"));
            }
        }
    }

    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }
}
