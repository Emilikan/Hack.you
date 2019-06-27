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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Button btnSignIn = findViewById(R.id.confirm);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getString("theme", "light").equals("dark")){
            setTheme(R.style.DarkTheme);
            findViewById(R.id.linearLayout).setBackgroundColor(Color.parseColor("#18191D"));
            EditText login = findViewById(R.id.Login);
            EditText pass = findViewById(R.id.Password);
            login.setBackgroundResource(R.drawable.input_dark);
            pass.setBackgroundResource(R.drawable.input_dark);
            login.setTextColor(Color.parseColor("#B7BEC7"));
            pass.setTextColor(Color.parseColor("#B7BEC7"));
            btnSignIn.setBackgroundResource(R.drawable.button_dark);
            btnSignIn.setTextColor(Color.parseColor("#FFFFFF"));
            ImageView logo =  findViewById(R.id.imageView4);
            logo.setImageResource(R.drawable.logo_dark);
        }
        else{
            setTheme(R.style.AppTheme);
            findViewById(R.id.linearLayout).setBackgroundColor(Color.parseColor("#ed1a3a"));
            EditText login = findViewById(R.id.Login);
            EditText pass = findViewById(R.id.Password);
            login.setBackgroundResource(R.drawable.input_primary);
            pass.setBackgroundResource(R.drawable.input_primary);
            login.setTextColor(Color.parseColor("#ffffff"));
            pass.setTextColor(Color.parseColor("#ffffff"));
            btnSignIn.setBackgroundResource(R.drawable.button_background);
            btnSignIn.setTextColor(Color.parseColor("#ed1a3a"));
            ImageView logo =  findViewById(R.id.imageView4);
            logo.setImageResource(R.drawable.logo);
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("PARAM", 1);
                startActivity(intent);

                //auth();
            }
        });
    }

    public void auth(){
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
