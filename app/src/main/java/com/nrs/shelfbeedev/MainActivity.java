package com.nrs.shelfbeedev;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nrs.shelfbeedev.fragments.FragmentAllUser;
import com.nrs.shelfbeedev.fragments.FragmentViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mainToolbar) Toolbar mMainToolbar;
    @BindView(R.id.mainContainer) RelativeLayout mMainContainer;
    @BindView(R.id.drawerTopContainer) RelativeLayout mDrawerTopContainer;
    @BindView(R.id.mainNoInternet) ImageView mNoInternet;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentViewPager mFragmentViewPager;
    FragmentAllUser mFragmentAllUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.transparentStatusBar);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initialize();
        addOnConnection(savedInstanceState);
    }

    private void initialize() {
        setSupportActionBar(mMainToolbar);
    }

    private boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void addOnConnection(Bundle savedInstanceState) {
        if (checkConnection()) {
            mNoInternet.setVisibility(View.GONE);
            initializeDrawer();
            addFragments(savedInstanceState);
        } else {
            removeOffConnection(savedInstanceState);
        }
    }

    private void removeOffConnection(final Bundle savedInstanceState) {
        mNoInternet.setVisibility(View.VISIBLE);
        Snackbar.make(mDrawerTopContainer, getResources().getString(R.string.noInternet), BaseTransientBottomBar.LENGTH_INDEFINITE)
                .setActionTextColor(getResources().getColor(R.color.white))
                .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addOnConnection(savedInstanceState);
                    }
                }).show();
    }


    private void addFragments(Bundle savedInstanceState) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            mFragmentViewPager = new FragmentViewPager();
            mFragmentAllUsers = new FragmentAllUser();
            ft.add(R.id.mainContainer, mFragmentViewPager);
            ft.add(R.id.mainContainer, mFragmentAllUsers);
            ft.show(mFragmentViewPager);
            ft.hide(mFragmentAllUsers);
            ft.commit();
        }
    }

    private void initializeDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mainDrawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.mainNavigationView);
        mNavigationView.getMenu().getItem(0).setChecked(true);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mMainToolbar, R.string.drawerOpen, R.string.drawerClose) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navItemTransactions:
                        if (mFragmentViewPager.isHidden()) {
                            ft.show(mFragmentViewPager);
                            ft.hide(mFragmentAllUsers);
                            drawerAction(0);
                        }
                        break;
                    case R.id.navItemUsers:
                        if (mFragmentAllUsers.isHidden()) {
                            ft.hide(mFragmentViewPager);
                            ft.show(mFragmentAllUsers);
                            drawerAction(1);
                        }
                        break;
                    case R.id.navItemFinance:
                        Toast.makeText(getApplicationContext(), "Finance", Toast.LENGTH_SHORT).show();
                }
                ft.commit();
                return false;
            }
        });
    }


    private void drawerAction(int key) {
        invalidateOptionsMenu();
        MenuItem menuPager = mNavigationView.getMenu().getItem(0).setChecked(false);
        MenuItem menuUsers = mNavigationView.getMenu().getItem(1).setChecked(false);
        MenuItem menuFinance = mNavigationView.getMenu().getItem(2).setChecked(false);
        mDrawerLayout.closeDrawers();
        switch (key) {
            case 0:
                mMainToolbar.setElevation(0);
                menuPager.setChecked(true);
                getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                break;
            case 1:
                mMainToolbar.setElevation(4);
                menuUsers.setChecked(true);
                getSupportActionBar().setTitle(getResources().getString(R.string.navUsers));
                break;
            case 2:
                menuFinance.setChecked(true);
                getSupportActionBar().setTitle(getResources().getString(R.string.navFinance));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainMenuSearch:
                makeSearchDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeSearchDialog(){
        AlertDialog.Builder searchOptions = new AlertDialog.Builder(MainActivity.this);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_selectable_list_item);
        arrayAdapter.add("Search Books");
        arrayAdapter.add("Search Request");
        arrayAdapter.add("Search User by name");
        searchOptions.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    Toast.makeText(getApplicationContext(), "Books", Toast.LENGTH_SHORT).show();
                }else if(which==1){
                    Toast.makeText(getApplicationContext(), "Request", Toast.LENGTH_SHORT).show();
                }else if(which==2){
                    Toast.makeText(getApplicationContext(), "User", Toast.LENGTH_SHORT).show();
                }
            }
        });
        searchOptions.create().show();
    }
}
