package com.buffet.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
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
import com.buffet.models.Promotion;
import com.buffet.network.ServerRequest;
import com.buffet.network.ServerResponse;
import com.buffet.network.ServiceAction;
import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;
import java.util.List;

import ggwp.caliver.banned.buffetteamfinderv2.Manifest;
import ggwp.caliver.banned.buffetteamfinderv2.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            System.out.println("before testttttt");
        }

        System.out.println("testttttt" + MainActivity.query);


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

        recyclerView = (RecyclerView) findViewById(R.id.search_promotion_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        progressBar = (ProgressBar) findViewById(R.id.progress);

        noeventText = (TextView) findViewById(R.id.noevent_text);

        searchPromotion();

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
}
