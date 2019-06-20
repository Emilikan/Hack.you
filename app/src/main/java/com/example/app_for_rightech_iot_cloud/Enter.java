package com.example.app_for_rightech_iot_cloud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        Intent intent = new Intent(Enter.this, LoginActivity.class);
        intent.putExtra("PARAM", 1);
        startActivity(intent);
    }
}
