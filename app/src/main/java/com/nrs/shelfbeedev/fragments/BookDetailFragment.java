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
import com.nrs.shelfbeedev.object.ObjectBook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class BookDetailFragment extends Fragment {

    @BindView(R.id.bookInfoBId) TextView mBookInfoBookId;
    @BindView(R.id.bookInfoName) TextView mBookInfoBookName;
    @BindView(R.id.bookInfoPublisher) TextView mBookInfoBookPublisher;
    @BindView(R.id.bookInfoCp) TextView mBookInfoBookCp;
    @BindView(R.id.bookInfoSp) TextView mBookInfoBookSp;
    @BindView(R.id.bookInfoEdtn) TextView mBookInfoBookEdtn;
    @BindView(R.id.bookInfoCondition) TextView mBookInfoBookCndtn;
    @BindView(R.id.bookInfoCategory) TextView mBookInfoBookCate;
    @BindView(R.id.bookInfoDescription) TextView mBookInfoBookDes;
    @BindView(R.id.bookInfoProgressBar) ProgressBar mBookProgressBar;
    private Unbinder mUnbinder;

    public BookDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_book_detail, container, false);
        mUnbinder = ButterKnife.bind(this, v);
        initialize();
        return v;
    }


    private void initialize() {
        if (getActivity().getIntent() != null) {
            if (getActivity().getIntent().getExtras().getInt(getActivity().getResources().getString(R.string.intentBookId)) != 0) {
                getRequestData(getActivity().getIntent().getExtras().getInt(getActivity().getResources().getString(R.string.intentBookId)));
            }
        }
    }

    private String buildUserUri(int bid) {
        String mHostName = getResources().getString(R.string.urlServerLink);
        String mAllUserLink = getResources().getString(R.string.urlBookQueryBid);
        String url = mHostName + mAllUserLink;
        String ridQuery = "bkid";
        return Uri.parse(url).buildUpon().appendQueryParameter(ridQuery, String.valueOf(bid)).toString();
    }

    private void getRequestData(int userId) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, buildUserUri(userId), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    setBookData(response);
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

    private void setBookData(JSONArray response) throws JSONException {
        mBookProgressBar.setVisibility(View.GONE);
        for(int i=0;i<response.length();i++){
            JSONObject jsonObject = response.getJSONObject(i);
            mBookInfoBookId.setText("Bookd id : "+jsonObject.getInt("id"));
            mBookInfoBookName.setText("Book Name : "+jsonObject.getString("Name"));
            mBookInfoBookPublisher.setText("Publisher : "+jsonObject.getString("Publisher"));
            mBookInfoBookCp.setText("Cost Price : "+jsonObject.getInt("CostPrice"));
            mBookInfoBookSp.setText("Selling Price : "+jsonObject.getInt("SellingPrice"));
            mBookInfoBookEdtn.setText("Edition : "+jsonObject.getInt("Edition"));
            mBookInfoBookDes.setText("Description : "+jsonObject.getString("Description"));
            mBookInfoBookCndtn.setText("Condition : "+jsonObject.getString("Cndtn"));
            mBookInfoBookCate.setText("Category : "+jsonObject.getString("Cateogory"));
        }
    }

    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }


}
