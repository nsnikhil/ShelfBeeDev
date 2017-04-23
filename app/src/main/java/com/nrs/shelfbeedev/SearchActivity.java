package com.nrs.shelfbeedev;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.claudiodegio.msv.MaterialSearchView;
import com.claudiodegio.msv.OnSearchViewListener;
import com.nrs.shelfbeedev.adapter.AdapterUserList;
import com.nrs.shelfbeedev.network.VolleySingleton;
import com.nrs.shelfbeedev.object.ObjectBook;
import com.nrs.shelfbeedev.object.ObjectRequest;
import com.nrs.shelfbeedev.object.ObjectUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {


    @BindView(R.id.searchToolbar) Toolbar mSearchToolbar;
    @BindView(R.id.sSearchView) MaterialSearchView mSearchView;
    @BindView(R.id.searchListView) ListView mSearchList;
    @BindView(R.id.searchImage) ImageView mSearchImage;
    private static final String NULL_VALUE = "N/A";
    private static String mUrl;
    ArrayList<ObjectBook> mBookObjectList;
    ArrayList<ObjectUser> mUserList;
    ArrayList<ObjectRequest> mRequestList;
    AdapterUserList mUserListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initialize();
        listeners();
    }

    private void listeners() {
        mSearchView.setOnSearchViewListener(new OnSearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }

            @Override
            public boolean onQueryTextSubmit(String s) {
                getData(s);
                return false;
            }

            @Override
            public void onQueryTextChange(String s) {

            }
        });
    }

    private String buildSearchUri(String search){
        return Uri.parse(mUrl).buildUpon().appendQueryParameter("nm",search).build().toString();
    }

    private void getList(JSONArray response) throws JSONException {
        if(getIntent().getExtras().getString(getResources().getString(R.string.intentSearchUrl),NULL_VALUE).equalsIgnoreCase(getResources().getString(R.string.urlSearchBooks))){
            mBookObjectList = new ArrayList<>();
            makeBookList(response);
        }else if(getIntent().getExtras().getString(getResources().getString(R.string.intentSearchUrl),NULL_VALUE).equalsIgnoreCase(getResources().getString(R.string.urlSearchRequests))){
            mRequestList = new ArrayList<>();
            makeRequestList(response);
        }else if(getIntent().getExtras().getString(getResources().getString(R.string.intentSearchUrl),NULL_VALUE).equalsIgnoreCase(getResources().getString(R.string.urlSearchUsers))){
            mUserList = new ArrayList<>();
            makeUserList(response);
        }
    }

    private void makeBookList(JSONArray response) throws JSONException {
        Toast.makeText(getApplicationContext(),"booklist",Toast.LENGTH_SHORT).show();
        if(response.length()>0){
            mSearchImage.setVisibility(View.GONE);
            for(int i=0;i<response.length();i++){
                JSONObject jsonObject = response.getJSONObject(i);
                int bid = jsonObject.getInt("id");
                String name = jsonObject.getString("Name");
                String publisher = jsonObject.getString("Publisher");
                int costPrice = jsonObject.getInt("CostPrice");
                int sellingPrice = jsonObject.getInt("SellingPrice");
                int edition = jsonObject.getInt("Edition");
                String description = jsonObject.getString("Description");
                String condtn = jsonObject.getString("Cndtn");
                String cateogory = jsonObject.getString("Cateogory");
                String userId = jsonObject.getString("userId");
                String photoUrlName0 = jsonObject.getString("pic0");
                String photoUrlName1 = jsonObject.getString("pic1");
                String photoUrlName2 = jsonObject.getString("pic2");
                String photoUrlName3 = jsonObject.getString("pic3");
                String photoUrlName4 = jsonObject.getString("pic4");
                String photoUrlName5 = jsonObject.getString("pic5");
                String photoUrlName6 = jsonObject.getString("pic6");
                String photoUrlName7 = jsonObject.getString("pic7");
                int status = jsonObject.getInt("status");
                mBookObjectList.add(new ObjectBook(bid, name, publisher, costPrice, sellingPrice, edition, description, condtn, cateogory, userId
                        , photoUrlName0, photoUrlName1, photoUrlName2, photoUrlName3, photoUrlName4, photoUrlName5, photoUrlName6, photoUrlName7, status));
            }
            mUserListAdapter = new AdapterUserList(getApplicationContext(), mBookObjectList,"abc");
            mSearchList.setAdapter(mUserListAdapter);
        }else {
            mSearchImage.setVisibility(View.VISIBLE);
        }
    }

    private void makeRequestList(JSONArray response) throws JSONException {
        Toast.makeText(getApplicationContext(),"requestlist",Toast.LENGTH_SHORT).show();
        if(response.length()>0){
            mSearchImage.setVisibility(View.GONE);
            for(int i=0;i<response.length();i++){
                JSONObject object = response.getJSONObject(i);
                String name = object.getString("name");
                String uid = object.getString("id");
                String publisher = object.getString("publisher");
                int rid = object.getInt("rid");
                mRequestList.add(new ObjectRequest(name, publisher, uid, rid));
            }
            mUserListAdapter = new AdapterUserList(getApplicationContext(), mRequestList,1);
            mSearchList.setAdapter(mUserListAdapter);
        }else {
            mSearchImage.setVisibility(View.VISIBLE);
        }
    }

    private void makeUserList(JSONArray response) throws JSONException {
        Toast.makeText(getApplicationContext(),"userlist",Toast.LENGTH_SHORT).show();
        if(response.length()>0){
            mSearchImage.setVisibility(View.GONE);
            for(int i=0;i<response.length();i++){
                JSONObject object = response.getJSONObject(i);
                String uid = object.getString("uid");
                String nm = object.getString("name");
                String phn = object.getString("phoneno");
                String adr = object.getString("address");
                String fk = object.getString("fkey");
                String bst = object.getString("bstatus");
                mUserList.add(new ObjectUser(uid, nm, phn, adr, fk, bst));
            }
            mUserListAdapter = new AdapterUserList(getApplicationContext(), mUserList);
            mSearchList.setAdapter(mUserListAdapter);
        }else {
            mSearchImage.setVisibility(View.VISIBLE);
        }
    }

    private void getData(String search){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, buildSearchUri(search), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    getList(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);
    }

    private void initialize() {
        setSupportActionBar(mSearchToolbar);
        mSearchView.showSearch();
        if(getIntent()!=null){
            mUrl = getResources().getString(R.string.urlServerLink)+getIntent().getExtras().getString(getResources().getString(R.string.intentSearchUrl),NULL_VALUE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.serach_menu, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        mSearchView.setMenuItem(item);
        return true;
    }
}
