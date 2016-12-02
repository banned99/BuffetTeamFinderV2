package com.buffet.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.buffet.adapters.MyCreateDealRecyclerAdapter;
import com.buffet.adapters.MyJoinDealRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import ggwp.caliver.banned.buffetteamfinderv2.R;

public class MyDealActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    CoordinatorLayout rootLayout;
    Toolbar toolbar;
    NestedScrollView nestedScrollView;
    NavigationView navigationView;


    RecyclerView createDealRecyclerView, joinDealRecyclerView;
    MyCreateDealRecyclerAdapter createAdapter;
    MyJoinDealRecyclerAdapter joinAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_deal);

        rootLayout = (CoordinatorLayout) findViewById(R.id.activity_my_deal_root_layout);

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_my_deal);
        drawerToggle = new ActionBarDrawerToggle(MyDealActivity.this, drawerLayout, R.string.profile, R.string.profile);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.setHomeAsUpIndicator(R.drawable.back_button);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setTitle(R.string.my_deal);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nestedScrollView = (NestedScrollView) findViewById(R.id.nest_scroll_view);
        nestedScrollView.setFillViewport(true);

        // Navigation Drawer
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.my_deal:
                        Intent intent = new Intent(getApplicationContext(), MyDealActivity.class);
                        startActivity(intent);
                        break;
//                    case R.id.view_profile:
//                        Intent navIntent = new Intent(getApplicationContext(), ViewProfileActivity.class);
//                        startActivity(navIntent);
//                        break;
                    case R.id.logout:
                        Snackbar.make(navigationView, "Log Out", Snackbar.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        createDealRecyclerView = (RecyclerView) findViewById(R.id.my_create_deal_list);
        createDealRecyclerView.setHasFixedSize(false);
        createDealRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        joinDealRecyclerView = (RecyclerView) findViewById(R.id.my_join_deal_list);
        joinDealRecyclerView.setHasFixedSize(false);
        joinDealRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getCreatedDeal();
        getJoinedDeal();
    }

    public void getCreatedDeal() {

        List<String> list = new ArrayList<>();
        list.add("create1");
        list.add("create2");
        createAdapter = new MyCreateDealRecyclerAdapter(getApplicationContext(), list);
        createDealRecyclerView.setAdapter(createAdapter);
    }

    public void getJoinedDeal() {
        List<String> list = new ArrayList<>();
        list.add("join1");
        list.add("join2");
        joinAdapter = new MyJoinDealRecyclerAdapter(getApplicationContext(), list);
        joinDealRecyclerView.setAdapter(joinAdapter);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

//        if (drawerToggle.onOptionsItemSelected(item))
//            return true;
//
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
    }


    // When pressed back button, switch to promotion tab
//    @Override
//    public void onBackPressed() {
//        if (bottomBar.getCurrentTabPosition() != 0) {
//            bottomBar.selectTabAtPosition(0);
//        } else super.onBackPressed();
//    }
}
