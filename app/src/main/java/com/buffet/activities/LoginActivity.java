package com.buffet.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.buffet.network.ServiceGenerator;
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
    private EditText et_email, et_password;
    private TextView tv_register;
    private ProgressBar progress;
    public static SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Facebook SDK initialized
         * To create callbackmanager.
         */
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        /**
         * Create login and register layout
         */

            setContentView(R.layout.activity_login);
            pref = getPreferences(0);
            btn_login = (AppCompatButton) findViewById(R.id.btn_login);
            tv_register = (TextView) findViewById(R.id.tv_register);
            et_email = (EditText) findViewById(R.id.et_email);
            et_password = (EditText) findViewById(R.id.et_password);
            progress = (ProgressBar) findViewById(R.id.progress);
            LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
            //profile = (ProfilePictureView)findViewById(R.id.picture);
            loginButton.setReadPermissions("public_profile email");

        /**
         *  Normal Login initiallized
         *  To create onClickListener.
         */

            // Click Login
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = et_email.getText().toString();
                    String password = et_password.getText().toString();
                    if (!email.isEmpty() && !password.isEmpty()) {
                        loginProcess(email, password);
                    } else {
                        Snackbar.make(v, "Fields are empty !", Snackbar.LENGTH_LONG).show();
                    }
                }
            });

            // Click Register
            tv_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToRegister();
                }
            });
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

        /**
         * Facebook Callback
         * To handle the result for login.
         */
        if(pref.getBoolean(Constants.IS_LOGGED_IN, false)){
            goToMain();
        }
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    System.out.println("User ID: "
                            + loginResult.getAccessToken().getUserId()
                            + "\n"
                            + "Auth Token: "
                            + loginResult.getAccessToken().getToken());
                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            json = response.getJSONObject();
                            if (Profile.getCurrentProfile() == null) {
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
                public void onCancel() {
                    System.out.println("Facebook login attempt canceled. ");
                }

                @Override
                public void onError(FacebookException exception) {
                    Log.i("FB", "error", exception);
                    if (exception instanceof FacebookAuthorizationException) {
                        if (AccessToken.getCurrentAccessToken() != null) {
                            LoginManager.getInstance().logOut();
                        }
                    }
                }
            });
    }
    @Override
    public void onResume() {
        super.onResume();
        if(pref.getBoolean(Constants.IS_LOGGED_IN, false)){
            goToMain();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void nextActivity(JSONObject profile) {
        if (profile != null) {
            try {
                String name = profile.getString("name");
                String email = profile.getString("email");
                String fbid = profile.getString("id");
                fbProcess(name, email, fbid);
                getUserData(email);
                goToMain();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("Start Main Activity", "Debugging");
        }
    }

    private void goToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void goToRegister() {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
        finish();
    }

    private void loginProcess(String email, String password) {
        ServiceAction service = createService(ServiceAction.class);
        ServerRequest request = new ServerRequest();
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        request.setUser(user);
        request.setOperation("login");
        Call<ServerResponse> call = service.accountProcess(request);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse model = response.body();
                System.out.println("loginProcess: onResponse"
                        + "\nResult : " + model.getResult()
                        + "\nMessage : " + model.getMessage());
                if (model.getResult().equals(Constants.SUCCESS)) {
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
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void fbProcess(String name, String email, String fbid) {
        ServiceAction service = createService(ServiceAction.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("fbProcess");
        User fbuser = new User();
        fbuser.setFbid(fbid);
        fbuser.setName(name);
        fbuser.setEmail(email);
        request.setUser(fbuser);
        Call<ServerResponse> call = service.accountProcess(request);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse model = response.body();
                System.out.println("fbProcess: onResponse" +
                        "\nResult : " + model.getResult()
                        + "\nMessage : " + model.getMessage());
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void getUserData(String email) {
        ServiceAction service = createService(ServiceAction.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("getUserData");
        User user = new User();
        user.setEmail(email);
        request.setUser(user);
        Call<ServerResponse> call = service.accountProcess(request);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse model = response.body();
                System.out.println("getUserData: onResponse" + "\nResult : " + model.getResult()
                        + "\nMessage : " + model.getMessage());
                if (model.getResult().equals(Constants.SUCCESS)) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(Constants.IS_LOGGED_IN, true);
                    editor.putString(Constants.NAME, model.getUser().getName());
                    editor.putString(Constants.EMAIL, model.getUser().getEmail());
                    editor.putString(Constants.TEL, model.getUser().getTel());
                    editor.putString(Constants.MEMBER_ID, model.getUser().getMember_id());
                    editor.apply();
                    System.out.println(Constants.IS_LOGGED_IN);
                    goToMain();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                System.out.println("Debugging......");
                t.printStackTrace();
            }
        });

    }
}