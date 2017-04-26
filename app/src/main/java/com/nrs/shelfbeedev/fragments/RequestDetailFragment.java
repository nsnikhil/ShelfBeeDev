package com.nrs.shelfbeedev.fragments;


import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
    @BindView(R.id.requestInfoDelete)
    Button mRequestDelete;
    private Unbinder mUnbinder;
    LoadingDialogFragment mLoadingDialogFragment;

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
        mRequestDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder delete = new AlertDialog.Builder(getActivity());
                delete.setTitle("Warning").setMessage("Are you sure you want to remove this request from database, this action is irreversible")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mLoadingDialogFragment = new LoadingDialogFragment();
                        mLoadingDialogFragment.show(getFragmentManager(),"wait");
                        deleteRequest(getActivity().getIntent().getExtras().getInt(getActivity().getResources().getString(R.string.intentRequestId)));
                    }
                });
                delete.create().show();
            }
        });
    }

    private String buildDeleteUri(int rid){
        String mHostName = getResources().getString(R.string.urlServerLink);
        String mAllUserLink = getResources().getString(R.string.urlDeleteRequest);
        String url = mHostName + mAllUserLink;
        String requestIdQuery = "rid";
        return Uri.parse(url).buildUpon().appendQueryParameter(requestIdQuery, String.valueOf(rid)).toString();
    }

    private void deleteRequest(int rid){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, buildDeleteUri(rid), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(),response,Toast.LENGTH_SHORT).show();
                mLoadingDialogFragment.dismiss();
                getActivity().finish();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
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
