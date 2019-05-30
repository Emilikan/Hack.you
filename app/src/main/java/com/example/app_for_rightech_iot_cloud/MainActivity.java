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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        final TextView title = findViewById(R.id.title);

        final ImageView leftButton = findViewById(R.id.notific);
        final ImageView rightButton = findViewById(R.id.neuronet);
        Toast.makeText(this,leftButton.getDrawable()+"",Toast.LENGTH_LONG).show();
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText() == "Уведомления"){
                    leftButton.setImageResource(R.drawable.notification);
                    rightButton.setImageResource(R.drawable.artificial_intelligence);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = new MainFragment();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    title.setText("Метровагонмаш");
                }
                else {
                    if (title.getText() == "Нейросеть") {
                        leftButton.setImageResource(R.drawable.notification);
                        rightButton.setImageResource(R.drawable.artificial_intelligence);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        Fragment fragment = new MainFragment();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        title.setText("Метровагонмаш");
                    }
                else{
                    leftButton.setImageResource(R.drawable.ic_left_arrow);
                    rightButton.setImageResource(R.drawable.artificial_intelligence);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = new Notifications();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    title.setText("Уведомления");
                }
                }
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText() == "Уведомления"){
                    leftButton.setImageResource(R.drawable.ic_left_arrow);
                    rightButton.setImageResource(R.drawable.notification);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = new Neuronet();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    title.setText("Нейросеть");
                }
                else {
                    if (title.getText() == "Нейросеть") {
                        leftButton.setImageResource(R.drawable.ic_left_arrow);
                        rightButton.setImageResource(R.drawable.artificial_intelligence);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        Fragment fragment = new Notifications();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        title.setText("Уведомления");
                    }
                    else{
                        leftButton.setImageResource(R.drawable.ic_left_arrow);
                        rightButton.setImageResource(R.drawable.notification);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        Fragment fragment = new Neuronet();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        title.setText("Нейросеть");
                    }
                }
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new MainFragment();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }



    /*@Override
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
    }*/
}
