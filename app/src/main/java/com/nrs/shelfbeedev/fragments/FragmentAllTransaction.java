package com.nrs.shelfbeedev.fragments;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.nrs.shelfbeedev.R;
import com.nrs.shelfbeedev.adapter.AdapterList;
import com.nrs.shelfbeedev.adapter.AdapterTransaction;
import com.nrs.shelfbeedev.network.VolleySingleton;
import com.nrs.shelfbeedev.object.ObjectTransaction;
import com.nrs.shelfbeedev.object.ObjectUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FragmentAllTransaction extends android.support.v4.app.Fragment {

    @BindView(R.id.allTransactionList) RecyclerView mAllTransactions;
    @BindView(R.id.allTransactionSwipeRefresh) SwipeRefreshLayout mSwipeRefresh;
    private Unbinder mUnbinder;
    private ArrayList<ObjectTransaction> mList;
    ArrayList<Integer> mBookIds;
    private AdapterTransaction adapterTransaction;


    public FragmentAllTransaction() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_all_transaction, container, false);
        mUnbinder = ButterKnife.bind(this, v);
        mAllTransactions.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList = new ArrayList<>();
        listeners();
        buildAllTransactionUri();
        mSwipeRefresh.setRefreshing(true);
        return v;
    }

    private void listeners(){
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                mAllTransactions.setAdapter(null);
                mList.clear();
                buildAllTransactionUri();
            }
        });
    }


    private void buildAllTransactionUri(){
        String mHostName = getActivity().getResources().getString(R.string.urlServerLink);
        String mAllUserLink = getActivity().getResources().getString(R.string.urlTransactionAll);
        String url = mHostName+mAllUserLink;
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
        ;   }

    private void makeList(JSONArray response) throws JSONException {
        mSwipeRefresh.setRefreshing(false);
        if(response.length()>0){
            for(int i=0;i<response.length();i++){
                JSONObject object = response.getJSONObject(i);
                String slId = object.getString("sellerUid");
                String byId =object.getString("buyerUid");
                String pyB =object.getString("paybuyer");
                String pyS =object.getString("payseller");
                String bTm =object.getString("buytime");
                String tnS =object.getString("tranStatus");
                String bkid =object.getString("bid");
                mList.add(new ObjectTransaction(slId,byId,pyB,pyS,bTm,tnS,bkid));
            }
            adapterTransaction = new AdapterTransaction(getActivity(),mList);
            mAllTransactions.setAdapter(adapterTransaction);
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation = super.onCreateAnimation(transit, enter, nextAnim);
        if (animation == null && nextAnim != 0) {
            animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        }
        if (animation != null) {
            getView().setLayerType(View.LAYER_TYPE_HARDWARE, null);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                public void onAnimationEnd(Animation animation) {
                    getView().setLayerType(View.LAYER_TYPE_NONE, null);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }
        return animation;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
