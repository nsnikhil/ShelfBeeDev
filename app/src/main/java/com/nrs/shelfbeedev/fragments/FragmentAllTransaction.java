package com.nrs.shelfbeedev.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.nrs.shelfbeedev.R;
import com.nrs.shelfbeedev.TransDetailActivity;
import com.nrs.shelfbeedev.adapter.AdapterTransaction;
import com.nrs.shelfbeedev.network.VolleySingleton;
import com.nrs.shelfbeedev.object.ObjectBookTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class FragmentAllTransaction extends android.support.v4.app.Fragment {

    @BindView(R.id.allTransactionList)
    ListView mAllTransactions;
    @BindView(R.id.allTransactionSwipeRefresh)
    SwipeRefreshLayout mSwipeRefresh;
    private Unbinder mUnbinder;
    private ArrayList<ObjectBookTransaction> mList;
    private AdapterTransaction adapterTransaction;


    public FragmentAllTransaction() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragment_all_transaction, container, false);
        mUnbinder = ButterKnife.bind(this, v);
        mList = new ArrayList<>();
        listeners();
        buildAllTransactionUri();
        mSwipeRefresh.setRefreshing(true);
        return v;
    }

    private void listeners() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                mAllTransactions.setAdapter(null);
                mList.clear();
                buildAllTransactionUri();
            }
        });
        mAllTransactions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ObjectBookTransaction objectTransaction = (ObjectBookTransaction) parent.getItemAtPosition(position);
                Intent detail = new Intent(getActivity(), TransDetailActivity.class);
                Bundle b = new Bundle();
                b.putSerializable(getActivity().getResources().getString(R.string.bundleSerialKey), objectTransaction);
                detail.putExtras(b);
                startActivity(detail);
            }
        });
    }

    private void buildAllTransactionUri() {
        String mHostName = getActivity().getResources().getString(R.string.urlServerLink);
        String mAllUserLink = getActivity().getResources().getString(R.string.urlTransactionAllItems);
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
    }

    private void makeList(JSONArray response) throws JSONException {
        mSwipeRefresh.setRefreshing(false);
        if (response.length() > 0) {
            for (int i = 0; i < response.length(); i++) {
                JSONObject object = response.getJSONObject(i);
                int id = object.getInt("id");
                String nm = object.getString("Name");
                String pb = object.getString("Publisher");
                int cp = object.getInt("CostPrice");
                int sp = object.getInt("SellingPrice");
                int ed = object.getInt("Edition");
                String condt = object.getString("Cndtn");
                String cat = object.getString("Cateogory");
                String des = object.getString("Description");
                String usrd = object.getString("userId");
                String pic0 = object.getString("pic0");
                String pic1 = object.getString("pic1");
                String pic2 = object.getString("pic2");
                String pic3 = object.getString("pic3");
                String pic4 = object.getString("pic4");
                String pic5 = object.getString("pic5");
                String pic6 = object.getString("pic6");
                String pic7 = object.getString("pic7");
                int bookstatus = object.getInt("status");
                String bId = object.getString("buyerUid");
                String pBy = object.getString("paybuyer");
                String pSl = object.getString("payseller");
                String bTm = object.getString("buytime");
                String tSts = object.getString("tranStatus");
                mList.add(new ObjectBookTransaction(id, nm, pb, cp, sp, ed, condt, cat, des, usrd, pic0, pic1, pic2, pic3, pic4, pic5, pic6, pic7, bookstatus, bId, pBy, pSl, bTm, tSts));
            }
        }
        adapterTransaction = new AdapterTransaction(getActivity(), mList);
        mAllTransactions.setAdapter(adapterTransaction);
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
