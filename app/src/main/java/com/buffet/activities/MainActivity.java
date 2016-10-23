package com.buffet.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.IdRes;
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
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.buffet.fragments.MapFragment;
import com.buffet.fragments.NotiFragment;
import com.buffet.fragments.PromotionFragment;
import com.buffet.fragments.SearchFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;


import org.w3c.dom.Text;

import ggwp.caliver.banned.buffetteamfinderv2.R;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    CoordinatorLayout rootLayout;
    Toolbar toolbar;
    NestedScrollView nestedScrollView;
    BottomBar bottomBar;
    NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = (CoordinatorLayout) findViewById(R.id.activity_main_root_layout);

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nestedScrollView = (NestedScrollView) findViewById(R.id.nest_scroll_view);
        nestedScrollView.setFillViewport(true);



        // Bottombar
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                if (tabId == R.id.promotion_tab) {
                    ChangeStyleBottomBarLabel(0);
                    PromotionFragment promotionFragment = PromotionFragment.newInstance();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, promotionFragment);
                    transaction.commit();
                }

                if (tabId == R.id.search_tab) {
                    ChangeStyleBottomBarLabel(1);
                    SearchFragment searchFragment = SearchFragment.newInstance();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, searchFragment);
                    transaction.commit();
                }

                if (tabId == R.id.map_tab) {
                    ChangeStyleBottomBarLabel(2);
                    MapFragment mapFragment = MapFragment.newInstance();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, mapFragment);
                    transaction.commit();
                }

                if (tabId == R.id.notification_tab) {
                    ChangeStyleBottomBarLabel(3);
                    BottomBarTab notification = bottomBar.getTabWithId(R.id.notification_tab);
                    notification.removeBadge();

                    NotiFragment notiFragment = NotiFragment.newInstance();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, notiFragment);
                    transaction.commit();
                }
            }
        });

        // Add Badge to BottomBar
        BottomBarTab notification = bottomBar.getTabWithId(R.id.notification_tab);
        notification.setBadgeCount(99);

        // Remove Badge
//        notification.removeBadge();

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
                        Intent navIntent = new Intent(getApplicationContext(), EditProfileActivity.class);
                        startActivity(navIntent);
                        break;
                    case R.id.logout:
                        Snackbar.make(navigationView, "Log Out", Snackbar.LENGTH_SHORT).show();
                        break;
                }
                return false;
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
    @Override
    public void onBackPressed() {
        if (bottomBar.getCurrentTabPosition() != 0) {
            bottomBar.selectTabAtPosition(0);
        } else super.onBackPressed();
    }

    // When Tab is selected , label will show
    public void ChangeStyleBottomBarLabel(int index) {

        final ViewGroup mItemContainer = (ViewGroup) findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_item_container);

        for (int i = 0; i < mItemContainer.getChildCount(); i++) {
            View viewItem = mItemContainer.getChildAt(i);
            if (i == index){
                //TITLE
                TextView titleTab = (TextView) viewItem.findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_title);
                titleTab.setVisibility(View.VISIBLE);
                //ICON
                AppCompatImageView icon = (AppCompatImageView) viewItem.findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_icon);
                icon.setY(6);
                //hack for fix the color set in setActiveTabColor
                icon.setColorFilter(titleTab.getCurrentTextColor());
            } else {
                //TITLE
                TextView titleTab = (TextView) viewItem.findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_title);
                titleTab.setVisibility(View.GONE);
                //ICON
                AppCompatImageView icon = (AppCompatImageView) viewItem.findViewById(com.roughike.bottombar.R.id.bb_bottom_bar_icon);
                icon.setY(19);
                //hack for fix the color set in setActiveTabColor
                icon.setColorFilter(titleTab.getCurrentTextColor());
            }
        }

    }
}
