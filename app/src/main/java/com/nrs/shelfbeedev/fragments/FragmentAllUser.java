package com.nrs.shelfbeedev.fragments;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.nrs.shelfbeedev.R;
import com.nrs.shelfbeedev.adapter.AdapterList;
import com.nrs.shelfbeedev.network.VolleySingleton;
import com.nrs.shelfbeedev.object.ObjectUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FragmentAllUser extends android.support.v4.app.Fragment {

    @BindView(R.id.allUserList)
    ListView mAllUserList;
    @BindView(R.id.allUserSwipeRefresh)
    SwipeRefreshLayout mSwipeRefresh;
    AdapterList mAdapterList;
    ArrayList<ObjectUser> mUserList;
    private Unbinder mUnbinder;

    public FragmentAllUser() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_all_user, container, false);
        mUnbinder = ButterKnife.bind(this, v);
        mUserList = new ArrayList<>();
        buildAllUserUri();
        mSwipeRefresh.setRefreshing(true);
        listeners();
        return v;
    }

    private void listeners() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                mAllUserList.setAdapter(null);
                mUserList.clear();
                buildAllUserUri();
            }
        });
    }

    private void buildAllUserUri() {
        String mHostName = getActivity().getResources().getString(R.string.urlServerLink);
        String mAllUserLink = getActivity().getResources().getString(R.string.urlUserAll);
        String url = mHostName + mAllUserLink;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    makeList(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefresh.setRefreshing(false);
            }
        });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);
        ;
    }

    private void makeList(JSONArray response) throws JSONException {
        mSwipeRefresh.setRefreshing(false);
        if (response.length() > 0) {
            for (int i = 0; i < response.length(); i++) {
                JSONObject object = response.getJSONObject(i);
                String uid = object.getString("uid");
                String nm = object.getString("name");
                String phn = object.getString("phoneno");
                String adr = object.getString("address");
                String fk = object.getString("fkey");
                String bst = object.getString("bstatus");
                mUserList.add(new ObjectUser(uid, nm, phn, adr, fk, bst));
            }
        }
        mAdapterList = new AdapterList(getActivity(), mUserList);
        mAllUserList.setAdapter(mAdapterList);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
