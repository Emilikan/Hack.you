package com.example.app_for_rightech_iot_cloud;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Метровагонмаш");

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new MainFragment();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_notification) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new Notifications();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            getSupportActionBar().setTitle("Уведомления");
        } else if (item.getItemId() == R.id.action_ai) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new Neuronet();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            getSupportActionBar().setTitle("Нейросеть");
        }
        return true;
    }
}
