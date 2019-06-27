package com.example.app_for_rightech_iot_cloud;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private static final String BASE_URL = "https://rightech.lab.croc.ru/";
    private static ApiAuth apiAuth;
    private boolean resp;
    private EditText loginEt;
    private EditText passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Button btnSignIn = findViewById(R.id.confirm);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (preferences.getString("theme", "light").equals("dark")){
            setTheme(R.style.DarkTheme);
            findViewById(R.id.linearLayout).setBackgroundColor(Color.parseColor("#18191D"));
            loginEt = findViewById(R.id.Login);
            passwordEt = findViewById(R.id.Password);
            loginEt.setBackgroundResource(R.drawable.input_dark);
            passwordEt.setBackgroundResource(R.drawable.input_dark);
            loginEt.setTextColor(Color.parseColor("#B7BEC7"));
            passwordEt.setTextColor(Color.parseColor("#B7BEC7"));
            btnSignIn.setBackgroundResource(R.drawable.button_dark);
            btnSignIn.setTextColor(Color.parseColor("#FFFFFF"));
            ImageView logo =  findViewById(R.id.imageView4);
            logo.setImageResource(R.drawable.logo_dark);
        }
        else{
            setTheme(R.style.AppTheme);
            findViewById(R.id.linearLayout).setBackgroundColor(Color.parseColor("#ed1a3a"));
            loginEt = findViewById(R.id.Login);
            passwordEt = findViewById(R.id.Password);
            loginEt.setBackgroundResource(R.drawable.input_primary);
            passwordEt.setBackgroundResource(R.drawable.input_primary);
            loginEt.setTextColor(Color.parseColor("#000000"));
            passwordEt.setTextColor(Color.parseColor("#000000"));
            btnSignIn.setBackgroundResource(R.drawable.button_background);
            btnSignIn.setTextColor(Color.parseColor("#ed1a3a"));
            ImageView logo =  findViewById(R.id.imageView4);
            logo.setImageResource(R.drawable.logo);
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = loginEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                if(!login.equals("") && !password.equals("")){
                    if(auth(login, password)){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("PARAM", 1);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Авторизация не удалась", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Вы не заполнили поля логин и/или пароль", Toast.LENGTH_LONG).show();
                }
                //auth();
            }
        });
    }

    private boolean auth(String login, String password){
        boolean result = false;
        if(login != null && login.equals("mmsh-mech1") && password != null && password.equals("P@$$w0rd")){
            result = true;
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("login", login);
            editor.putString("password", password);
            editor.apply();
        }
        return result;
    }

    public void authServer(){
        AuthBody body = new AuthBody();
        body.login = "mmsh-mech1";
        body.password = "P@$$w0rd";

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        gson.toJson(body);

        Log.i("GSON", gson.toJson(body));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiAuth apiAuth = retrofit.create(ApiAuth.class);

        apiAuth.authResponse(body).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.body()!=null) {
                    Toast.makeText(LoginActivity.this, response.body() + "", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "error: " + t, Toast.LENGTH_LONG).show();
            }
        });
    }
}
