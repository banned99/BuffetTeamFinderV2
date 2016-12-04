package com.buffet.activities;

import android.content.res.Configuration;
import android.support.design.widget.CoordinatorLayout;
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

import com.buffet.adapters.DealMemberRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import ggwp.caliver.banned.buffetteamfinderv2.R;

public class DealMemberActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    CoordinatorLayout rootLayout;
    Toolbar toolbar;
    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    DealMemberRecyclerAdapter adapter;

    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_member);
        rootLayout = (CoordinatorLayout) findViewById(R.id.activity_deal_member_root_layout);

        status = getIntent().getExtras().getString("member_status");
        System.out.println(status);

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_deal_member);
        drawerToggle = new ActionBarDrawerToggle(DealMemberActivity.this, drawerLayout, R.string.member, R.string.member);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.setHomeAsUpIndicator(R.drawable.back_button);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setTitle(R.string.member);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nestedScrollView = (NestedScrollView) findViewById(R.id.nest_scroll_view);
        nestedScrollView.setFillViewport(true);

        recyclerView = (RecyclerView) findViewById(R.id.deal_member_list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        getDealMember();
    }

    private void getDealMember() {
        List<String> dealMember = new ArrayList<>();
        dealMember.add("member 1");
        dealMember.add("member 2");
        dealMember.add("member 3");

        adapter = new DealMemberRecyclerAdapter(getApplicationContext(), dealMember, status);
        recyclerView.setAdapter(adapter);
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

}
