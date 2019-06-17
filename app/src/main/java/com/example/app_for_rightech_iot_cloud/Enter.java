package com.example.app_for_rightech_iot_cloud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

public class Enter extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_enter);
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Enter.this, LoginActivity.class);
        intent.putExtra("PARAM", 1);
        startActivity(intent);
    }
}
