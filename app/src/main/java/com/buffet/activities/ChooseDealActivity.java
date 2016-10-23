package com.buffet.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
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
import android.widget.Toast;

import com.buffet.dialogs.CreateDealDialog;
import com.buffet.fragments.ChooseDealFragment;

import ggwp.caliver.banned.buffetteamfinderv2.R;

public class ChooseDealActivity extends AppCompatActivity implements CreateDealDialog.Communicator{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    CoordinatorLayout rootLayout;
    Toolbar toolbar;
    NestedScrollView nestedScrollView;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_deal);

        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("branch_id");

        Toast.makeText(this, ""+ id, Toast.LENGTH_LONG).show();

        rootLayout = (CoordinatorLayout) findViewById(R.id.activity_choose_deal_root_layout);

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_choose_deal);
        drawerToggle = new ActionBarDrawerToggle(ChooseDealActivity.this, drawerLayout, R.string.app_name, R.string.app_name);
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
                    case R.id.favorite_deal:
                        Snackbar.make(navigationView, "Favorite Deal", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.edit_profile:
                        Intent editProfileIntent = new Intent(getApplicationContext(), EditProfileActivity.class);
                        startActivity(editProfileIntent);
                        break;
                    case R.id.logout:
                        Snackbar.make(navigationView, "Log Out", Snackbar.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        // Fab button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                CreateDealDialog createDealDialog = new CreateDealDialog();
                createDealDialog.show(manager, "dialog");
            }
        });

        // Deal Fragment
        ChooseDealFragment chooseDealFragment = ChooseDealFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.deal_container, chooseDealFragment);
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

    // Get data from create deal form
    @Override
    public void onDialogMessage(String restaurant, String branch, String time, String promotion, int amount) {

        Toast.makeText(this, restaurant + "\n" + branch + "\n" + time + "\n" + promotion + "\n" + Integer.toString(amount) + "\n", Toast.LENGTH_LONG).show();
    }

    public void onDialogMessage(int available, String restaurant, String time, String name){
        Toast.makeText(this, available + "\n" + restaurant + "\n" + time + "\n" + name, Toast.LENGTH_LONG).show();
    }
}
