package com.example.app_for_rightech_iot_cloud;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.content.DialogInterface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    int leftArrow;
    int notification;
    int artificialIntelligence;
    int settings;
    private ArrayList<String> names;
    private ArrayList<String> ids;

    private String nameOfTitle;

    private static final String BASE_URL = "https://rightech.lab.croc.ru/";

    private TextView title;

    private int mPosition;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.activity_main);
        final TextView title = findViewById(R.id.title);
        final ImageView leftButton = findViewById(R.id.notific);
        final ImageView rightButton = findViewById(R.id.settings);



        if (Objects.equals(preferences.getString("theme", "light"), "dark")){
            setTheme(R.style.DarkTheme);
            findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor("#282E33"));
            title.setTextColor(Color.parseColor("#E9E9E9"));
            leftArrow = R.drawable.left_arrow_white;
            notification = R.drawable.notification_white;
            artificialIntelligence = R.drawable.artifical_intelligence_white;
            settings = R.drawable.settings_white;
            leftButton.setImageResource(notification);
            rightButton.setImageResource(settings);
        }
        else{
            setTheme(R.style.AppTheme);
            findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor("#ffffff"));
            title.setTextColor(Color.parseColor("#000000"));
            leftArrow = R.drawable.left_arrow;
            notification = R.drawable.notification;
            artificialIntelligence = R.drawable.artificial_intelligence;
            settings = R.drawable.settings;
            leftButton.setImageResource(notification);
            rightButton.setImageResource(settings);
        }

        nameOfTitle = preferences.getString("Factory","");
        title.setText(nameOfTitle);


        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(preferences.getString("theme", "light"), "dark")){
                    setTheme(R.style.DarkTheme);
                    leftArrow = R.drawable.left_arrow_white;
                    notification = R.drawable.notification_white;
                    artificialIntelligence = R.drawable.artifical_intelligence_white;
                    settings = R.drawable.settings_white;
                }
                else{
                    setTheme(R.style.AppTheme);
                    leftArrow = R.drawable.left_arrow;
                    notification = R.drawable.notification;
                    artificialIntelligence = R.drawable.artificial_intelligence;
                    settings = R.drawable.settings;
                }
                if (title.getText() == "Уведомления"){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = new MainFragment();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    leftButton.setImageResource(notification);
                    rightButton.setImageResource(settings);
                    title.setText(nameOfTitle);
                }
                else {
                    if (title.getText() == "Настройки") {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        Fragment fragment = new MainFragment();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        leftButton.setImageResource(notification);
                        rightButton.setImageResource(settings);

                        title.setText(nameOfTitle);
                    }
                else{
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = new Notifications();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    leftButton.setImageResource(leftArrow);
                    rightButton.setImageResource(settings);

                    title.setText("Уведомления");
                }
                }
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(preferences.getString("theme", "light"), "dark")){
                    setTheme(R.style.DarkTheme);
                    leftArrow = R.drawable.left_arrow_white;
                    notification = R.drawable.notification_white;
                    artificialIntelligence = R.drawable.artifical_intelligence_white;
                    settings = R.drawable.settings_white;
                }
                else{
                    setTheme(R.style.AppTheme);
                    leftArrow = R.drawable.left_arrow;
                    notification = R.drawable.notification;
                    artificialIntelligence = R.drawable.artificial_intelligence;
                    settings = R.drawable.settings;

                }
                if (title.getText() == "Уведомления"){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = new Settings();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    leftButton.setImageResource(leftArrow);
                    rightButton.setImageResource(notification);

                    title.setText("Настройки");
                }
                else {
                    if (title.getText() == "Настройки") {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        Fragment fragment = new Notifications();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        leftButton.setImageResource(leftArrow);
                        rightButton.setImageResource(settings);

                        title.setText("Уведомления");
                    }
                    else{
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        Fragment fragment = new Settings();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        leftButton.setImageResource(leftArrow);
                        rightButton.setImageResource(notification);

                        title.setText("Настройки");
                    }
                }
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new MainFragment();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }



}
