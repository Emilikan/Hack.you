package com.example.app_for_rightech_iot_cloud;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class Enter extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        /**
         * Закоменчено Эмилем, т.к. бесит постоянно ждать загрузки. Да, кст, загрузка, я думаю, должна делаться через что-то другое
         */
        /*try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.getMessage();
        }*/
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.getString("login", null) != null && preferences.getString("password", null) != null){
            try {
                if(auth(preferences.getString("login", ""), preferences.getString("password", ""))){
                    Intent intent = new Intent(Enter.this, MainActivity.class);
                    intent.putExtra("PARAM", 1);
                    startActivity(intent);
                } else {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("login", null);
                    editor.putString("password", null);
                    editor.apply();
                    Intent intent = new Intent(Enter.this, LoginActivity.class);
                    intent.putExtra("PARAM", 1);
                    startActivity(intent);
                }
            } catch (Exception e){
                Intent intent = new Intent(Enter.this, LoginActivity.class);
                intent.putExtra("PARAM", 1);
                startActivity(intent);
                Log.i("error", e + "");
            }
        } else {
            Intent intent = new Intent(Enter.this, LoginActivity.class);
            intent.putExtra("PARAM", 1);
            startActivity(intent);
        }

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
}
