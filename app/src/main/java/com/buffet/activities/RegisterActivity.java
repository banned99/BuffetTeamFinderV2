package com.buffet.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
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

import ggwp.caliver.banned.buffetteamfinderv2.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.buffet.network.ServiceGenerator.createService;

public class RegisterActivity extends AppCompatActivity {
    private AppCompatButton btn_register;
    private EditText et_email,et_password,et_name;
    private TextView tv_login;
    private ProgressBar progress;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_register = (AppCompatButton)findViewById(R.id.btn_register);
        tv_login = (TextView)findViewById(R.id.tv_login);
        et_name = (EditText)findViewById(R.id.et_name);
        et_email = (EditText)findViewById(R.id.et_email);
        et_password = (EditText)findViewById(R.id.et_password);
        progress = (ProgressBar)findViewById(R.id.progress);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();

                if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    progress.setVisibility(View.VISIBLE);
                    tv_login.setEnabled(false);
                    et_email.setEnabled(false);
                    et_name.setEnabled(false);
                    et_password.setEnabled(false);
                    registerProcess(name,email,password, v);
                } else {
                    Snackbar.make(v, "Fields are empty !", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });
    }

    private void goToLogin(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void registerProcess(String name, String email, String password, final View v){
        ServiceAction service = createService(ServiceAction.class);
        ServerRequest request = new ServerRequest();
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        request.setUser(user);
        request.setOperation("register");
        Call<ServerResponse> call = service.accountProcess(request);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response){
                ServerResponse model = response.body();
                System.out.println("Result : " + model.getResult()
                        + "\nMessage : " + model.getMessage());
                if(model.getResult().equals(Constants.SUCCESS)){
                    progress.setVisibility(View.INVISIBLE);
                    Toast.makeText(v.getContext(), model.getMessage(), Toast.LENGTH_LONG).show();
                    goToLogin();
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
