package com.buffet.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import com.satsuware.usefulviews.LabelledSpinner;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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
    NavigationView navigationView;
    Button viewProfileButton;
    LabelledSpinner branch_spin, current_spin;
    TextView promotionLabel;
    TextView promotionPrice;
    TextView promotionMax;
    TextView promotionExpire;
    TextView promotionCategory;
    TextView promotionDescription;
    ImageView promotionImage;
    ImageButton expandButton;

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
        Toast.makeText(this, promotion_description, Toast.LENGTH_SHORT).show();
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

        expandButton = (ImageButton) findViewById(R.id.expand_button);
        expandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (promotionDescription.getVisibility() == View.GONE) {
                    expandButton.setImageResource(R.drawable.up_arrow);
                    promotionDescription.setVisibility(View.VISIBLE);
                } else {
                    expandButton.setImageResource(R.drawable.down_arrow);
                    promotionDescription.setVisibility(View.GONE);
                }
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
        System.out.println("ID = " +pref.getInt(Constants.MEMBER_ID, 0));
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
}
