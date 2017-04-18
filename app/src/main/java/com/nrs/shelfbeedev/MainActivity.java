package com.nrs.shelfbeedev;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.claudiodegio.msv.OnSearchViewListener;
import com.nrs.shelfbeedev.fragments.FragmentViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mainToolbar) Toolbar mMainToolbar;
    @BindView(R.id.mainContainer) RelativeLayout mMainContainer;
    @BindView(R.id.mainSearchView) com.claudiodegio.msv.MaterialSearchView mMainSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        setSupportActionBar(mMainToolbar);
        getSupportFragmentManager().beginTransaction().add(R.id.mainContainer,new FragmentViewPager()).commit();
        mMainSearchView.setOnSearchViewListener(new OnSearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public void onQueryTextChange(String s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.mainMenuSearch);
        mMainSearchView.setMenuItem(item);
        return true;
    }
}
