package com.example.app_for_rightech_iot_cloud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    static final String BASE_URL = "https://rightech.lab.croc.ru/";
    private static ApiAuth apiAuth;
    private boolean resp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        Button btnSignIn = findViewById(R.id.button_sign_in);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("PARAM", 1);
                startActivity(intent);
                */

                auth();


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
                Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_LONG).show();
            }
        });

        /*Call<AuthResponse> call = apiAuth.authResponse(body);
        call.enqueue(new Callback<AuthResponse>() {
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
                Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_LONG).show();
            }
        });
        */
    }
}
