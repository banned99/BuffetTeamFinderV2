package com.buffet.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.buffet.adapters.MyCreateDealRecyclerAdapter;
import com.buffet.models.Branch;
import com.buffet.models.Constants;
import com.buffet.models.Deal;
import com.buffet.models.Promotion;
import com.buffet.models.User;
import com.buffet.network.ServerRequest;
import com.buffet.network.ServerResponse;
import com.buffet.network.ServiceAction;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ggwp.caliver.banned.buffetteamfinderv2.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.buffet.activities.LoginActivity.pref;
import static com.buffet.network.ServiceGenerator.createService;
import static com.facebook.FacebookSdk.getApplicationContext;
import static ggwp.caliver.banned.buffetteamfinderv2.R.id.imageView;
import static ggwp.caliver.banned.buffetteamfinderv2.R.id.wide;


public class ViewProfileActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    CoordinatorLayout rootLayout;
    Toolbar toolbar;
    NestedScrollView nestedScrollView;
    Button editPhotoButton, updateProfileButton;
    CircleImageView photo;
    EditText username, email;

    NavigationView navigationView;
    Button viewProfileButton;
    TextView viewProfileName;
    CircleImageView naviProfileImg;
    TextView usernameLabel;


    InputStream inputStream = null;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        rootLayout = (CoordinatorLayout) findViewById(R.id.activity_view_profile_root_layout);

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_view_profile);
        drawerToggle = new ActionBarDrawerToggle(ViewProfileActivity.this, drawerLayout, R.string.profile, R.string.profile);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.setHomeAsUpIndicator(R.drawable.back_button);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setTitle(R.string.profile);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nestedScrollView = (NestedScrollView) findViewById(R.id.nest_scroll_view);
        nestedScrollView.setFillViewport(true);

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
        photo = (CircleImageView) findViewById(R.id.user_photo);
        System.out.println("navi = " + naviProfileImg);

        if (pref.getString(Constants.IS_FACEBOOK_LOGGED_IN, null) != null) {
            if (pref.getString(Constants.IMAGE_URL, null) != null){
                System.out.println("have Fb, have image");
                Picasso.with(this).load("http://api.tunacon.com/uploads/" + pref.getString(Constants.IMAGE_URL, "FAIL")).resize(1200, 650).into(naviProfileImg);
                Picasso.with(this).load("http://api.tunacon.com/uploads/" + pref.getString(Constants.IMAGE_URL, "FAIL")).resize(1200, 650).into(photo);
            } else {
                // facebook image
                System.out.println("have Fb, no image");

                Picasso.with(this).load("https://graph.facebook.com/" + pref.getString(Constants.FBID, null) + "/picture?type=large").resize(1200, 650).into(naviProfileImg);
                Picasso.with(this).load("https://graph.facebook.com/" + pref.getString(Constants.FBID, null) + "/picture?type=large").resize(1200, 650).into(photo);

            }
        } else if (pref.getString(Constants.IMAGE_URL, null) != null) {
            System.out.println("no Fb, have image");

            Picasso.with(this).load("http://api.tunacon.com/uploads/" + pref.getString(Constants.IMAGE_URL, "FAIL")).resize(1200, 650).into(naviProfileImg);
            Picasso.with(this).load("http://api.tunacon.com/uploads/" + pref.getString(Constants.IMAGE_URL, "FAIL")).resize(1200, 650).into(photo);

        }
        else {
            // default image
            naviProfileImg.setImageResource(R.drawable.user_default_img);
            photo.setImageResource(R.drawable.user_default_img);
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

        username = (EditText) findViewById(R.id.edit_name);
        username.setText(pref.getString(Constants.NAME, ""));
        email = (EditText) findViewById(R.id.edit_email);
        email.setText(pref.getString(Constants.EMAIL, ""));
        email.setEnabled(false);

        editPhotoButton = (Button) findViewById(R.id.edit_photo);
        editPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        updateProfileButton = (Button) findViewById(R.id.edit_profile_button);
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String name = username.getText().toString();
                String mail = email.getText().toString();

                if(imagePath==null){
                    ServiceAction service = createService(ServiceAction.class);
                    ServerRequest request = new ServerRequest();
                    User users = new User();
                    users.setMemberId(pref.getInt(Constants.MEMBER_ID,0));
                    users.setName(name);
                    request.setOperation("editProfile");
                    request.setUser(users);

                    Call<ServerResponse> call = service.editProfileNoImage(request);
                    call.enqueue(new Callback<ServerResponse>() {
                        @Override
                        public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                            ServerResponse model = response.body();
                            if(model.getResult().equals("failure")){
                                System.out.println("Result : " + model.getResult()
                                        + "\nMessage : " + model.getMessage());
                            }else {
                                System.out.println("Result : " + model.getResult()
                                        + "\nMessage : " + model.getMessage());
                                for(int i=0; i<model.getListUser().size(); i++){
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString(Constants.NAME, model.getListUser().get(i).getName());
                                    editor.putString(Constants.IMAGE_URL, model.getListUser().get(i).getImageUrl());
                                    editor.apply();
                                }

                                Snackbar.make(v, "Update Successful", Snackbar.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ServerResponse> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }else{
                    File file = new File(imagePath);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part imageFileBody = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

                    ServiceAction service = createService(ServiceAction.class);

                    Call<ServerResponse> call = service.editProfile(imageFileBody, pref.getInt(Constants.MEMBER_ID,0), name);
                    call.enqueue(new Callback<ServerResponse>() {
                        @Override
                        public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                            ServerResponse model = response.body();
                            if(model.getResult().equals("failure")){
                                System.out.println("Result : " + model.getResult()
                                        + "\nMessage : " + model.getMessage());
                            }else {
                                System.out.println("Result : " + model.getResult()
                                        + "\nMessage : " + model.getMessage());
                                for(int i=0; i<model.getListUser().size(); i++){
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString(Constants.NAME, model.getListUser().get(i).getName());
                                    editor.putString(Constants.IMAGE_URL, model.getListUser().get(i).getImageUrl());
                                    editor.apply();
                                    System.out.println("test:  "+model.getListUser().get(i).getImageUrl());
                                }

                                Snackbar.make(v, "Update Successful", Snackbar.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ServerResponse> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private static final int PICK_PHOTO_FOR_AVATAR = 0;

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(getApplicationContext(),"Unable to pick image",Toast.LENGTH_LONG).show();
                return;
            }
            Uri selectedImageUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = cursor.getString(columnIndex);

                photo.setImageBitmap(BitmapFactory.decodeFile(imagePath));

                cursor.close();

            } else {

                Toast.makeText(getApplicationContext(),"Unable to load image",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // update profile
        if (pref.getString(Constants.IS_FACEBOOK_LOGGED_IN, null) != null) {
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
 

    // When pressed back button, switch to promotion tab
//    @Override
//    public void onBackPressed() {
//        if (bottomBar.getCurrentTabPosition() != 0) {
//            bottomBar.selectTabAtPosition(0);
//        } else super.onBackPressed();
//    }

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

