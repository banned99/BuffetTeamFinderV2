package com.buffet.activities;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buffet.MySuggestionProvider;
import com.buffet.customs.CustomNestedScrollView;
import com.buffet.fragments.MapFragment;
import com.buffet.fragments.NewProFragment;
import com.buffet.fragments.NotiFragment;
import com.buffet.fragments.PromotionFragment;
import com.buffet.fragments.SearchFragment;
import com.buffet.models.Constants;
import com.buffet.models.Deal;
import com.buffet.models.User;
import com.buffet.network.GCMRegistrationIntentService;
import com.buffet.network.ServerRequest;
import com.buffet.network.ServerResponse;
import com.buffet.network.ServiceAction;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.model.Circle;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import ggwp.caliver.banned.buffetteamfinderv2.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.drawable.ic_menu_search;
import static com.buffet.activities.LoginActivity.pref;
import static com.buffet.network.ServiceGenerator.createService;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    CoordinatorLayout rootLayout;
    Toolbar toolbar;
    CustomNestedScrollView nestedScrollView;
    NavigationView navigationView;
    Button viewProfileButton;
    CircleImageView naviProfileImg;
    TextView viewProfileName;
    TextView usernameLabel;

    ImageView searchIcon;
    //Creating a broadcast receiver for gcm registration
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public static String query;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = getIntent();

        System.out.println("Activity Created");

        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            System.out.println("Search");
            MainActivity.query = intent.getExtras().getString("query");
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY,
                    MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(MainActivity.query, "recent");
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Getting the registration token from the intent
                    String token = intent.getStringExtra("token");
                    //Displaying the token as toast
//                  Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();
                    updateGCMToken(pref.getInt(Constants.MEMBER_ID, 0), token);

                    //if the intent is not with success then displaying error messages
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
//                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if(ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }

        //pref = getPreferences(0);


        rootLayout = (CoordinatorLayout) findViewById(R.id.activity_main_root_layout);

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchIcon = new ImageView(getApplicationContext());
        searchIcon.setImageResource(ic_menu_search);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.abs_layout, null),
                new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER));

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nestedScrollView = (CustomNestedScrollView) findViewById(R.id.nest_scroll_view);
        nestedScrollView.setFillViewport(true);

        NewProFragment newProFragment = NewProFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, newProFragment);
        transaction.commit();


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

    }

    public void updateGCMToken(int member_id, String gcm_token){
        ServiceAction service = createService(ServiceAction.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("updateGcmToken");

        User u = new User();

        u.setMemberId(member_id);
        u.setGcmToken(gcm_token);

        request.setUser(u);

        Call<ServerResponse> call = service.updateGcmToken(request);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse model = response.body();
                if(model.getResult().equals("failure")){
                    System.out.println("Update Token is failure");
                }else {
                    System.out.println("Result : " + model.getResult()
                            + "\nMessage : " + model.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));

        // update profile
        if (pref.getInt(Constants.IS_FACEBOOK_LOGGED_IN, 0) != 0) {
            if (pref.getString(Constants.IMAGE_URL, null) != null){
                System.out.println("have Fb, have image");
                Picasso.with(this).load("http://api.tunacon.com/uploads/" + pref.getString(Constants.IMAGE_URL, "FAIL")).resize(1200, 650).into(naviProfileImg);
            } else {
                // facebook image
                System.out.println("have Fb, no image");
                System.out.println("FBID: " + pref.getString(Constants.FBID, null));
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
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
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

        menu.add(0, 1, 0, "Settings").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(i);
                return false;
            }
        }).setIcon(R.drawable.ic_search)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);

//        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.menuSearch).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//
//        final Intent i = new Intent(MainActivity.this, SearchActivity.class);
//        if( Intent.ACTION_VIEW.equals(i.getAction())){
//            query = getIntent().getStringExtra(SearchManager.QUERY);
//            i.setAction(Intent.ACTION_SEARCH);
//            i.putExtra("query", query);
//            startActivity(i);
//
//            Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
//        }
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // INPUT CODE HERE
//                i.setAction(Intent.ACTION_SEARCH);
//                i.putExtra("query", query);
//                System.out.println("query = " + query);
//                startActivity(i);
//
////                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // INPUT CODE HERE
////                Toast.makeText(MainActivity.this, newText, Toast.LENGTH_SHORT).show();
////                String[] q = {"promotion_name"};
////                String[] t = {newText};
////                MySuggestionProvider suggestion = new MySuggestionProvider();
////                suggestion.query(Uri.parse("database.it.kmitl.ac.th/it_35"), q, "promotion_name LIKE %?%", t, null);
//                return false;
//            }
//
//        });


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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
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


    // When Tab is selected , label will show
//    public void ChangeStyleBottomBarLabel(int index) {
//
//        final ViewGroup mItemContainer = (ViewGroup) findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_item_container);
//
//        for (int i = 0; i < mItemContainer.getChildCount(); i++) {
//            View viewItem = mItemContainer.getChildAt(i);
//            if (i == index){
//                //TITLE
//                TextView titleTab = (TextView) viewItem.findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_title);
//                titleTab.setVisibility(View.VISIBLE);
//                //ICON
//                AppCompatImageView icon = (AppCompatImageView) viewItem.findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_icon);
//                icon.setY(6);
//                //hack for fix the color set in setActiveTabColor
//                icon.setColorFilter(titleTab.getCurrentTextColor());
//            } else {
//                //TITLE
//                TextView titleTab = (TextView) viewItem.findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_title);
//                titleTab.setVisibility(View.GONE);
//                //ICON
//                AppCompatImageView icon = (AppCompatImageView) viewItem.findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_icon);
//                icon.setY(19);
//                //hack for fix the color set in setActiveTabColor
//                icon.setColorFilter(titleTab.getCurrentTextColor());
//            }
//        }

//    }
}
