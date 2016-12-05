package com.buffet.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.buffet.adapters.DealMemberRecyclerAdapter;
import com.buffet.models.Constants;
import com.buffet.models.DealMember;
import com.buffet.models.Deal;
import com.buffet.models.User;
import com.buffet.network.ServerRequest;
import com.buffet.network.ServerResponse;
import com.buffet.network.ServiceAction;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ggwp.caliver.banned.buffetteamfinderv2.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.buffet.activities.LoginActivity.pref;
import static com.buffet.network.ServiceGenerator.createService;

public class DealMemberActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    CoordinatorLayout rootLayout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    DealMemberRecyclerAdapter adapter;
    ProgressBar progressBar;
    TextView noeventText;

    NavigationView navigationView;
    Button viewProfileButton;
    TextView viewProfileName;
    CircleImageView naviProfileImg;
    TextView usernameLabel;

    String status, member_name;
    int deal_id, member_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_member);
        rootLayout = (CoordinatorLayout) findViewById(R.id.activity_deal_member_root_layout);

        status = getIntent().getExtras().getString("member_status");
        deal_id = getIntent().getExtras().getInt("deal_id");
        member_id = getIntent().getExtras().getInt("deal_owner");
        member_name = getIntent().getExtras().getString("user_name");

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

        progressBar = (ProgressBar) findViewById(R.id.progress);
        noeventText = (TextView) findViewById(R.id.noevent_text);

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
                        logout();
                        Snackbar.make(navigationView, "Log Out", Snackbar.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        View headerLayout = navigationView.getHeaderView(0);
        naviProfileImg = (CircleImageView) headerLayout.findViewById(R.id.navigation_profile_image);
        usernameLabel = (TextView) headerLayout.findViewById(R.id.username_label);
        System.out.println("navi = " + naviProfileImg);

        if (pref.getInt(Constants.IS_FACEBOOK_LOGGED_IN, 0) != 0) {
            if (pref.getString(Constants.IMAGE_URL, null) != null){
                System.out.println("have Fb, have image");
                Picasso.with(this).load("http://api.tunacon.com/uploads/" + pref.getString(Constants.IMAGE_URL, "FAIL")).resize(1200, 650).into(naviProfileImg);
            } else {
                // facebook image
                System.out.println("have Fb, no image");

                Picasso.with(this).load("https://graph.facebook.com/" + pref.getString(Constants.FBID, null) + "/picture?type=large").resize(1200, 650).into(naviProfileImg);
            }
        } else if (pref.getString(Constants.IMAGE_URL, null) != null) {
            System.out.println("no Fb, have image");

            Picasso.with(this).load("http://api.tunacon.com/uploads/" + pref.getString(Constants.IMAGE_URL, "FAIL")).resize(1200, 650).into(naviProfileImg);
        }
        else {
            // default image
            naviProfileImg.setImageResource(R.drawable.user_default_img);
        }
        usernameLabel.setText(pref.getString(Constants.NAME, null));
        System.out.println(pref.getString(Constants.IMAGE_URL, "FAIL"));


        viewProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username_label);
        viewProfileName.setText(pref.getString(Constants.NAME,""));
        viewProfileButton = (Button) navigationView.getHeaderView(0).findViewById(R.id.view_profile_button);

        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                Intent navIntent = new Intent(v.getContext().getApplicationContext(), ViewProfileActivity.class);
                startActivity(navIntent);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.deal_member_list);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        getDealMember();


    }

    private void getDealMember() {
        ServiceAction service = createService(ServiceAction.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("getdealmember");

        Deal deals = new Deal();
        deals.setDealId(deal_id);

        request.setDeal(deals);

        Call<ServerResponse> call = service.getDealMember(request);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse model = response.body();
                List<DealMember> dealMembers = new ArrayList<>();
                List<User> users = new ArrayList<>();

                progressBar.setVisibility(View.GONE);

                if(model.getResult().equals("failure")){
                    System.out.println("Deal IS NULL");
                    noeventText.setVisibility(View.VISIBLE);
                }else {
                    System.out.println("Result : " + model.getResult()
                            + "\nMessage : " + model.getMessage());
                    User owner = new User();
                    owner.setName(member_name);
                    owner.setMemberId(member_id);
                    users.add(owner);
                    DealMember member = new DealMember();
                    member.setStatus(2);
                    dealMembers.add(member);
                    for (int i = 0; i< model.getListUser().size(); i++) {
                        DealMember current = new DealMember();
                        User u = new User();
                        current.setDealId(model.getDealMember().get(i).getDealId());
                        current.setMemberId(model.getDealMember().get(i).getMemberId());
                        current.setStatus(model.getDealMember().get(i).getStatus());
                        u.setMemberId(model.getListUser().get(i).getMemberId());
                        u.setName(model.getListUser().get(i).getName());

                        dealMembers.add(current);
                        users.add(u);
                    }

                    if(getApplicationContext()!=null) {
                        System.out.println("users size = " + users.size());
                        adapter = new DealMemberRecyclerAdapter(getApplicationContext(), dealMembers, users, status);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }



    @Override
    public void onResume() {
        super.onResume();

        // update profile
        if (pref.getInt(Constants.IS_FACEBOOK_LOGGED_IN, 0) != 0) {
            if (pref.getString(Constants.IMAGE_URL, null) != null){
                System.out.println("have Fb, have image");
                Picasso.with(this).load("http://api.tunacon.com/uploads/" + pref.getString(Constants.IMAGE_URL, "FAIL")).resize(1200, 650).into(naviProfileImg);
            } else {
                // facebook image
                System.out.println("have Fb, no image");

                Picasso.with(this).load("https://graph.facebook.com/" + pref.getString(Constants.FBID, null) + "/picture?type=large").resize(1200, 650).into(naviProfileImg);
            }
        } else if (pref.getString(Constants.IMAGE_URL, null) != null) {
            System.out.println("no Fb, have image");

            Picasso.with(this).load("http://api.tunacon.com/uploads/" + pref.getString(Constants.IMAGE_URL, "FAIL")).resize(1200, 650).into(naviProfileImg);
        }
        else {
            // default image
            naviProfileImg.setImageResource(R.drawable.user_default_img);
        }
        usernameLabel.setText(pref.getString(Constants.NAME, null));
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

    private void goToLogin(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void logout() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(Constants.IS_LOGGED_IN,false);
        editor.putString(Constants.EMAIL,"");
        editor.putString(Constants.NAME,"");
        editor.putString(Constants.TEL, "");
        editor.putString(Constants.MEMBER_ID,"");
        editor.apply();
        LoginManager.getInstance().logOut();
        goToLogin();
        finish();
    }

}
