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
import com.nrs.shelfbeedev.TransDetailActivity;
import com.nrs.shelfbeedev.fragments.dialogFragments.LoadingDialogFragment;
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
    @BindView(R.id.bookInfoDelete) Button mDeleteBook;
    private Unbinder mUnbinder;
    LoadingDialogFragment mLoadingDialogFragment;

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
        mDeleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder delete = new AlertDialog.Builder(getActivity());
                delete.setTitle("Warning").setMessage("Are you sure you want to remove this book from database, this action is irreversible")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mLoadingDialogFragment = new LoadingDialogFragment();
                        mLoadingDialogFragment.show(getFragmentManager(),"wait");
                        deleteBook(getActivity().getIntent().getExtras().getInt(getActivity().getResources().getString(R.string.intentBookId)));
                    }
                });
                delete.create().show();
            }
        });
    }

    private String buildDeleteUri(int bid){
        String mHostName = getResources().getString(R.string.urlServerLink);
        String mAllUserLink = getResources().getString(R.string.urlDeleteBook);
        String url = mHostName + mAllUserLink;
        String bookIdQuery = "id";
        return Uri.parse(url).buildUpon().appendQueryParameter(bookIdQuery, String.valueOf(bid)).toString();
    }

    private void deleteBook(int bid){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, buildDeleteUri(bid), new Response.Listener<String>() {
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
