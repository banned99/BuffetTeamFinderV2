package com.buffet.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.buffet.MySuggestionProvider;
import com.buffet.adapters.NewPromotionRecyclerAdapter;
import com.buffet.customs.CustomNestedScrollView;
import com.buffet.models.Constants;
import com.buffet.models.Promotion;
import com.buffet.network.ServerRequest;
import com.buffet.network.ServerResponse;
import com.buffet.network.ServiceAction;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ggwp.caliver.banned.buffetteamfinderv2.Manifest;
import ggwp.caliver.banned.buffetteamfinderv2.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.buffet.activities.LoginActivity.pref;
import static com.buffet.network.ServiceGenerator.createService;

public class SearchActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    CoordinatorLayout rootLayout;
    Toolbar toolbar;
    private ProgressBar progressBar;
    private TextView noeventText;

    private NewPromotionRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    private String query;

    Intent intent;

    NavigationView navigationView;
    Button viewProfileButton;
    TextView viewProfileName;
    CircleImageView naviProfileImg;
    TextView usernameLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Search
        intent = getIntent();


        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            MainActivity.query = intent.getExtras().getString("query");
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY,
                    MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(MainActivity.query, "recent");
        }

        rootLayout = (CoordinatorLayout) findViewById(R.id.activity_search_root_layout);

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.search);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_search);
        drawerToggle = new ActionBarDrawerToggle(SearchActivity.this, drawerLayout, R.string.search, R.string.search);
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.setHomeAsUpIndicator(R.drawable.back_button);

        drawerLayout.addDrawerListener(drawerToggle);

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



        recyclerView = (RecyclerView) findViewById(R.id.search_promotion_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        progressBar = (ProgressBar) findViewById(R.id.progress);

        noeventText = (TextView) findViewById(R.id.noevent_text);

        if (MainActivity.query == null) {
            progressBar.setVisibility(View.GONE);
        } else {
            searchPromotion();
        }
    }
    public void searchPromotion(){
        ServiceAction service = createService(ServiceAction.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("searchpromotion");
        request.setSearch(MainActivity.query);
        Call<ServerResponse> call = service.getSearchPromotion(request);
        call.enqueue(new Callback<ServerResponse>(){
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response){
                ServerResponse model = response.body();
                List<Promotion> promotions = new ArrayList<>();
                if(model.getResult().equals("failure")){
                    System.out.println("PROMOTION IS NULL");
                    noeventText.setVisibility(View.VISIBLE);
                } else {
                    System.out.println("Result : " + model.getResult()
                            + "\nMessage : " + model.getMessage());
                    noeventText.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    for (int i = 0; i< model.getPromotion().size(); i++) {
                        Promotion current = new Promotion();
                        current.setProId(model.getPromotion().get(i).getProId());
                        current.setImage(model.getPromotion().get(i).getImage());
                        current.setProName(model.getPromotion().get(i).getProName());
                        current.setPrice(model.getPromotion().get(i).getPrice());
                        current.setDateStart(model.getPromotion().get(i).getDateStart());
                        current.setExpire(model.getPromotion().get(i).getExpire());
                        current.setMaxPerson(model.getPromotion().get(i).getMaxPerson());
                        current.setCatId(model.getPromotion().get(i).getCatId());
                        current.setCatName(model.getPromotion().get(i).getCatName());
                        current.setDescription(model.getPromotion().get(i).getDescription());
                        promotions.add(current);
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
                if(getApplicationContext()!=null){
                    adapter = new NewPromotionRecyclerAdapter(getApplicationContext(), promotions);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
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

        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menuSearch).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);
        searchView.setIconified(false);



        if( Intent.ACTION_VIEW.equals(getIntent().getAction())){
            Intent i = new Intent(SearchActivity.this, SearchActivity.class);
            query = getIntent().getStringExtra(SearchManager.QUERY);
            i.setAction(Intent.ACTION_SEARCH);
            i.putExtra("query", query);
            startActivity(i);

            Toast.makeText(SearchActivity.this, query, Toast.LENGTH_SHORT).show();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // INPUT CODE HERE
                Toast.makeText(SearchActivity.this, query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // INPUT CODE HERE
//                String[] q = {"promotion_name"};
//                String[] t = {newText};
//                MySuggestionProvider suggestion = new MySuggestionProvider();
//                suggestion.query(Uri.parse("database.it.kmitl.ac.th/it_35"), q, "promotion_name LIKE %?%", t, null);
                return false;
            }
        });
        return true;
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
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
