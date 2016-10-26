package com.buffet.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.buffet.models.Constants;
import com.buffet.models.User;
import com.buffet.network.ServerRequest;
import com.buffet.network.ServerResponse;
import com.buffet.network.ServiceAction;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import ggwp.caliver.banned.buffetteamfinderv2.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.buffet.network.ServiceGenerator.createService;

/**
 * Created by Tastomy on 10/27/2016 AD.
 */

public class LoginActivity extends AppCompatActivity {
    private JSONObject json;
    private CallbackManager callbackManager;
    TextView details_txt;
    ProfilePictureView profile;
    private ProfileTracker mProfileTracker;
    private AppCompatButton btn_login;
    private EditText et_email,et_password;
    private TextView tv_register;
    private ProgressBar progress;
    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = this.getPreferences(0);
        btn_login = (AppCompatButton)findViewById(R.id.btn_login);
        tv_register = (TextView)findViewById(R.id.tv_register);
        et_email = (EditText)findViewById(R.id.et_email);
        et_password = (EditText)findViewById(R.id.et_password);
        progress = (ProgressBar)findViewById(R.id.progress);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                if(!email.isEmpty() && !password.isEmpty()) {
                    loginProcess(email, password);
                } else {
                    Toast.makeText(v.getContext(), "Fields are empty !" , Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        callbackManager = CallbackManager.Factory.create();
//        accessToken = AccessToken.getCurrentAccessToken();

        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
//        profile = (ProfilePictureView)findViewById(R.id.picture);
        loginButton.setReadPermissions("public_profile email");

        if(AccessToken.getCurrentAccessToken() != null){
            RequestData();
        }

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(AccessToken.getCurrentAccessToken() != null){
                    RequestData();
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
                if (exception instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                }
            }
        });
    }


    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object,GraphResponse response) {
                json = response.getJSONObject();
                try {
                    Log.e("Name", json.getString("name"));
//                    saveFbAccount(json.getString("name"), json.getString("id"));
                    nextActivity(json);
//                        profile.setProfileId(json.getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            // profile2 is the new profile
                            Log.e("facebook - profile", profile2.getFirstName());
                            nextActivity(json);
                            mProfileTracker.stopTracking();
                        }
                    };
                    mProfileTracker.startTracking();
                } else {
                    nextActivity(json);
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, link, email, picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void nextActivity(JSONObject profile){
        if(profile != null){
//            getFbDataFromDB(profile);
        } else {
            Log.e("Start Main Activity", "Debugging");
        }
    }

    private void goToMain(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void goToRegister(){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    private void loginProcess(String email,String password){
        ServiceAction service = createService(ServiceAction.class);
        ServerRequest request = new ServerRequest();
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        request.setUser(user);
        request.setOperation("login");
        Call<ServerResponse> call = service.login(request);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Response<ServerResponse> response){
                ServerResponse model = response.body();
                System.out.println("Result : " + model.getResult()
                        + "\nMessage : " + model.getMessage());
                if(model.getResult().equals(Constants.SUCCESS)){
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(Constants.IS_LOGGED_IN, true);
                    editor.putString(Constants.NAME, model.getUser().getName());
                    editor.putString(Constants.EMAIL, model.getUser().getEmail());
                    editor.putString(Constants.TEL, model.getUser().getTel());
                    editor.putString(Constants.MEMBER_ID, model.getUser().getMember_id());
                    editor.apply();
                    goToMain();
                }
            }
            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

//    private void saveFbAccount(String name, String fbid){
//        ServiceAction service = createService(ServiceAction.class);
//        ServerRequest request = new ServerRequest();
//        request.setOperation("getUserData");
//        User fbuser = new User();
//        fbuser.setFbid(fbid);
//        fbuser.setName(name);
//
//        request.setUser(fbuser);
//        Call<ServerResponse> call = service.getUserData(request);
//        call.enqueue(new Callback<ServerResponse>(){
//            Intent fbdata = new Intent(LoginActivity.this, MainActivity.class);
//            @Override
//            public void onResponse(Response<ServerResponse> response) {
//                ServerResponse model = response.body();
//
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                t.printStackTrace();
//            }
//        });
//
//    }
//
//    private void getFbDataFromDB(JSONObject profile){
//        ServiceAction service = createService(ServiceAction.class);
//        ServerRequest request = new ServerRequest();
//        request.setOperation("getUserData");
//        User fbuser = new User();
//        try {
//            fbuser.setEmail(profile.getString("email"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        request.setUser(fbuser);
//        Call<ServerResponse> call = service.getUserData(request);
//        call.enqueue(new Callback<ServerResponse>(){
//            Intent fbdata = new Intent(LoginActivity.this, MainActivity.class);
//            @Override
//            public void onResponse(Response<ServerResponse> response) {
//                ServerResponse model = response.body();
//                if(model.getResult().equals("success")){
//                    fbdata.putExtra("name", model.getUser().getName());
//                    fbdata.putExtra("email", model.getUser().getEmail());
//                    fbdata.putExtra("tel", model.getUser().getTel());
////                    if(profile.has("picture")){
////                        fbdata.putExtra("imageUrl", profile.getJSONObject("picture").getJSONObject("data").getString("url"));
////                    }
//                    fbdata.putExtra("memid", model.getUser().getMember_id());
//                    startActivity(fbdata);
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                t.printStackTrace();
//            }
//        });
//    }
}
