package com.buffet.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.buffet.fragments.ChooseBranchFragment;

import ggwp.caliver.banned.buffetteamfinderv2.R;


public class ChooseBranchActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    CoordinatorLayout rootLayout;
    Toolbar toolbar;
    NestedScrollView nestedScrollView;
    NavigationView navigationView;
    Button viewProfileButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_branch);

        Bundle bundle = getIntent().getExtras();
        int promotion_id = bundle.getInt("promotion_id");
        String promotion_name = bundle.getString("promotion_name");
        Double promotion_price = bundle.getDouble("promotion_price");
        String promotion_date_start = bundle.getString("promotion_date_start");
        String promotion_expire = bundle.getString("promotion_expire");
        int promotion_max_person = bundle.getInt("promotion_max_person");
        Toast.makeText(this, "id: "+ promotion_id+"\nname:"+promotion_name+"\nprice:"+promotion_price+"\ndate_start:"+promotion_date_start+"\nexpire"+ promotion_expire+"\nmax_person"+promotion_max_person, Toast.LENGTH_LONG).show();

        rootLayout = (CoordinatorLayout) findViewById(R.id.activity_choose_branch_root_layout);

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_choose_branch);
        drawerToggle = new ActionBarDrawerToggle(ChooseBranchActivity.this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Navigation Drawer
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.my_deal:
                        Snackbar.make(navigationView, "My Deal", Snackbar.LENGTH_SHORT).show();
                        break;
//                    case R.id.view_profile:
//                        Intent editProfileIntent = new Intent(getApplicationContext(), ViewProfileActivity.class);
//                        startActivityForResult(editProfileIntent, 0);
//                        break;
                    case R.id.logout:
                        Snackbar.make(navigationView, "Log Out", Snackbar.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        viewProfileButton = (Button) navigationView.getHeaderView(0).findViewById(R.id.view_profile_button);
//
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();

                Intent navIntent = new Intent(v.getContext().getApplicationContext(), ViewProfileActivity.class);
                startActivity(navIntent);
            }
        });



        // Branch Fragment
        ChooseBranchFragment chooseBranchFragment = ChooseBranchFragment.newInstance();
        chooseBranchFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.branch_container, chooseBranchFragment);
        transaction.commit();

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // When pressed back button, switch to promotion tab
//    @Override
//    public void onBackPressed() {
//        if (bottomBar.getCurrentTabPosition() != 0) {
//            bottomBar.selectTabAtPosition(0);
//        } else super.onBackPressed();
//    }

}
