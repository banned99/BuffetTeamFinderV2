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

import com.buffet.dialogs.CreateDealDialog;
import com.buffet.fragments.ChooseDealFragment;
import com.buffet.models.Constants;
import com.buffet.models.Deal;
import com.buffet.models.Promotion;
import com.buffet.models.User;
import com.buffet.network.ServerRequest;
import com.buffet.network.ServerResponse;
import com.buffet.network.ServiceAction;

import ggwp.caliver.banned.buffetteamfinderv2.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.buffet.activities.ChooseBranchActivity.promotion_id;
import static com.buffet.activities.LoginActivity.pref;
import static com.buffet.network.ServiceGenerator.createService;

public class ChooseDealActivity extends AppCompatActivity implements CreateDealDialog.Communicator{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    CoordinatorLayout rootLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    Button viewProfileButton;
    public int branch_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_deal);

        Bundle bundle = getIntent().getExtras();
        branch_id = bundle.getInt("branch_id");

        Toast.makeText(this, "p:"+ promotion_id+"b:"+branch_id, Toast.LENGTH_LONG).show();

        rootLayout = (CoordinatorLayout) findViewById(R.id.activity_choose_deal_root_layout);

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_choose_deal);
        drawerToggle = new ActionBarDrawerToggle(ChooseDealActivity.this, drawerLayout, R.string.choose_deal, R.string.choose_deal);
        drawerLayout.setDrawerListener(drawerToggle);

        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.setHomeAsUpIndicator(R.drawable.back_button);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setTitle(R.string.choose_deal);

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
//                        startActivity(editProfileIntent);
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




        // Deal Fragment
        ChooseDealFragment chooseDealFragment = ChooseDealFragment.newInstance();
        chooseDealFragment.setArguments(bundle);
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
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
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

    // Get data from create deal form
    @Override
    public void onDialogMessage(String time, String date, String max_ppl) {
        addDeal(date, time, max_ppl, promotion_id, branch_id);
        Toast.makeText(this, date + " " + time + " " + max_ppl, Toast.LENGTH_SHORT).show();
    }

    private void addDeal(String date, String time, String current_person, int pro_id, int branch_id) {
        ServiceAction service = createService(ServiceAction.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("adddeal");
        Deal deals = new Deal();

        deals.setDate(date);
        deals.setTime(time);
        deals.setDealOwner(pref.getString(Constants.NAME, ""));

        deals.setCurrentPerson(Integer.parseInt(current_person));
        request.setDeal(deals);
        request.setPromotionId(pro_id);
        request.setBranchId(branch_id);

        Call<ServerResponse> call = service.getDeal(request);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Response<ServerResponse> response) {
                ServerResponse model = response.body();
                System.out.println("addDeal: onResponse" +
                        "\nResult : " + model.getResult()
                        + "\nMessage : " + model.getMessage());
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
