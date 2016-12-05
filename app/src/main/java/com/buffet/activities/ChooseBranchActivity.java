package com.buffet.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.buffet.dialogs.CreateDealDialog;
import com.buffet.fragments.ChooseDealFragment;
import com.buffet.models.Branch;
import com.buffet.models.Constants;
import com.buffet.models.Deal;
import com.buffet.network.ServerRequest;
import com.buffet.network.ServerResponse;
import com.buffet.network.ServiceAction;
import com.facebook.login.LoginManager;
import com.satsuware.usefulviews.LabelledSpinner;
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


public class ChooseBranchActivity extends AppCompatActivity implements CreateDealDialog.Communicator{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    CoordinatorLayout rootLayout;
    Toolbar toolbar;
    NestedScrollView nestedScrollView;
    LabelledSpinner branch_spin;
    TextView promotionLabel;
    TextView promotionPrice;
    TextView promotionMax;
    TextView promotionExpire;
    TextView promotionCategory;
    TextView promotionDescription;
    ImageView promotionImage;
    Button expandButton;
    ProgressBar progressBar;

    NavigationView navigationView;
    Button viewProfileButton;
    TextView viewProfileName;
    CircleImageView naviProfileImg;
    TextView usernameLabel;

    List<Branch> branches;

    ArrayList<String> branch = new ArrayList<>();

    public static int promotion_id;
    public static int branch_id;
    public static int promotion_max_person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_branch);

        final Bundle bundle = getIntent().getExtras();
        promotion_id = bundle.getInt("promotion_id");
        String promotion_name = bundle.getString("promotion_name");
        Double promotion_price = bundle.getDouble("promotion_price");
        String promotion_date_start = bundle.getString("promotion_date_start");
        String promotion_expire = bundle.getString("promotion_expire");
        String promotion_image = bundle.getString("promotion_image");
        String promotion_catname = bundle.getString("promotion_catname");
        String promotion_description = bundle.getString("promotion_description");
        promotion_max_person = bundle.getInt("promotion_max_person");
//        Toast.makeText(this, "id: "+ promotion_id+"\nname:"+promotion_name+"\nprice:"+promotion_price+"\ndate_start:"+promotion_date_start+"\nexpire"+ promotion_expire+"\nmax_person"+promotion_max_person, Toast.LENGTH_LONG).show();
        rootLayout = (CoordinatorLayout) findViewById(R.id.activity_choose_branch_root_layout);

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_choose_branch);
        drawerToggle = new ActionBarDrawerToggle(ChooseBranchActivity.this, drawerLayout, R.string.join_events, R.string.join_events);
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.setHomeAsUpIndicator(R.drawable.back_button);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setTitle(R.string.join_events);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        promotionLabel = (TextView) findViewById(R.id.promotionLabel);
        promotionPrice = (TextView) findViewById(R.id.promotionPrice);
        promotionMax = (TextView) findViewById(R.id.promotionMax);
        promotionExpire = (TextView) findViewById(R.id.promotionExpire);
        promotionImage = (ImageView) findViewById(R.id.promotionImage);
        promotionDescription = (TextView) findViewById(R.id.promotionDescription);
        promotionCategory = (TextView) findViewById(R.id.promotionCategory);

        promotionLabel.setText(promotion_name);

        promotionPrice.setText("ราคา " + Double.toString(promotion_price) + " บาท");
        promotionMax.setText("จำนวน " + Integer.toString(promotion_max_person) + " คน");
        promotionExpire.setText("ถึง " + promotion_expire);
        Picasso.with(getApplicationContext()).load("http://api.tunacon.com/images/"+promotion_image).resize(1200, 650).into(promotionImage);
        promotionDescription.setText(promotion_description);
        promotionCategory.setText(promotion_catname);

        branch_spin = (LabelledSpinner) findViewById(R.id.branch_spinner);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        getData(promotion_id);

        branch_spin.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                branch_id =  branches.get(position).getBranchId();
                bundle.putInt("branch_id", branch_id);
                ChooseDealFragment chooseDealFragment = ChooseDealFragment.newInstance();
                chooseDealFragment.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.branch_container, chooseDealFragment);
                transaction.commit();
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });


        nestedScrollView = (NestedScrollView) findViewById(R.id.nest);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                CreateDealDialog createDealDialog = new CreateDealDialog();
                createDealDialog.show(manager, "dialog");
            }
        });
        // fab will hide when scroll down
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY && fab.isShown())
                {
                    fab.hide();
                } else fab.show();
            }
        });

        expandButton = (Button) findViewById(R.id.expand_button);
        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (promotionDescription.getVisibility() == View.GONE) {
                    expandButton.setText("Show less");
                    promotionDescription.setVisibility(View.VISIBLE);
                } else {
                    expandButton.setText("More detail");
                    promotionDescription.setVisibility(View.GONE);
                }
            }
        });

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

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();



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


    public void getData(int pro_id) {
        ServiceAction service = createService(ServiceAction.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("getbranch");
        request.setPromotionId(pro_id);
        Call<ServerResponse> call = service.getBranch(request);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response){
                branches = new ArrayList<>();
                ServerResponse model = response.body();
                if(model.getResult().equals("failure")){
                    System.out.println("BRANCH IS NULL");
                } else {
                    System.out.println("Result : " + model.getResult()
                            + "\nMessage : " + model.getMessage());
                    for (int i = 0; i<model.getBranch().size(); i++) {
                        Branch current = new Branch();
                        current.setBranchId(model.getBranch().get(i).getBranchId());
                        current.setBranchName(model.getBranch().get(i).getBranchName());
                        current.setLatitude(model.getBranch().get(i).getLatitude());
                        current.setLongitude(model.getBranch().get(i).getLongitude());
                        branches.add(current);

                        branch.add(current.getBranchName()+"");
                    }

                }

                branch_spin.setItemsArray(branch);

                progressBar.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onDialogMessage(String time, String date, String max_ppl) {
        addDeal(date, time, max_ppl, promotion_id, branch_id);
        Intent intent = new Intent(getApplicationContext(), MyDealActivity.class);
        startActivity(intent);
    }
    private void addDeal(String date, String time, String current_person, int pro_id, int branch_id) {
        ServiceAction service = createService(ServiceAction.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("adddeal");
        Deal deals = new Deal();

        deals.setDate(date);
        deals.setTime(time);
        deals.setDealOwner(pref.getInt(Constants.MEMBER_ID, 0));
        deals.setName(pref.getString(Constants.NAME, ""));
        deals.setCurrentPerson(Integer.parseInt(current_person));

        request.setDeal(deals);
        request.setPromotionId(pro_id);
        request.setBranchId(branch_id);

        Call<ServerResponse> call = service.getDeal(request);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse model = response.body();
                System.out.println("addDeal: onResponse" +
                        "\nResult : " + model.getResult()
                        + "\nMessage : " + model.getMessage());
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

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
