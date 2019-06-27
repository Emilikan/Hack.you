package com.example.app_for_rightech_iot_cloud;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.content.DialogInterface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import java.util.concurrent.TimeUnit;

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
    private static int sJobId = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.activity_main);
        final TextView title = findViewById(R.id.title);
        final ImageView leftButton = findViewById(R.id.notific);
        final ImageView rightButton = findViewById(R.id.settings);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("showAlert", null);
        if(preferences.getInt("IdNotif", -1)==-1) {
            editor.putInt("IdNotif", 0);
        }
        editor.apply();


        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            startService(new Intent(this, NotificationsService.class));
        } else {
            ComponentName jobService = new ComponentName(this, NotificationsJobService.class);
            JobInfo.Builder exerciseJobBuilder = new JobInfo.Builder(sJobId++, jobService);
            exerciseJobBuilder.setMinimumLatency(TimeUnit.SECONDS.toMillis(1));
            exerciseJobBuilder.setOverrideDeadline(TimeUnit.SECONDS.toMillis(5));
            exerciseJobBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
            exerciseJobBuilder.setRequiresDeviceIdle(false);
            exerciseJobBuilder.setRequiresCharging(false);
            exerciseJobBuilder.setBackoffCriteria(TimeUnit.SECONDS.toMillis(10), JobInfo.BACKOFF_POLICY_LINEAR);

            JobScheduler jobScheduler = (JobScheduler) this.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(exerciseJobBuilder.build());
        }


        if (preferences.getString("theme", "light").equals("dark")){
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

        names = new ArrayList<>();
        ids = new ArrayList<>();
        nameOfTitle = preferences.getString("name", "Выберите завод (установку)");
        title.setText(nameOfTitle);

        setNamesAndId();

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p;

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.alert, null);
                builder.setView(view);
                builder.setCancelable(true);
                Spinner spinner = view.findViewById(R.id.spinner);
                ArrayAdapter<?> adapter =
                        new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, names);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setGravity(Gravity.CENTER);
                spinner.setAdapter(adapter);

                builder.setNegativeButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(names.size() > 0) {
                            title.setText(names.get(mPosition));

                            FragmentManager fragmentManager = getSupportFragmentManager();
                            Fragment fragment = new MainFragment();
                            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        } else {
                            setNamesAndId();
                        }
                    }
                });

                final AlertDialog dialog = builder.create();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mPosition = position;
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("id", ids.get(position));
                        editor.putString("name", names.get(position));
                        editor.apply();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                });
                dialog.show();


            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new MainFragment();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    // получаем все объекты, затем помещаем их имена в toolbar
    private void setNamesAndId(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiGetAllObjects apiGetAllObjects = retrofit.create(ApiGetAllObjects.class);

        apiGetAllObjects.allObjects().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.body() != null) {
                    Log.i("Request", response.body().toString());
                    responseConversion(response.body(), response.body().size());

                } else {
                    Toast.makeText(MainActivity.this, "Нет ответа от сервера", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error " + t, Toast.LENGTH_SHORT).show();
                Log.i("Request", "error " + t);
            }
        });
    }

    // функция, которая принимает на вход массив ответа сервера и добавляет в ArrayList id и name объектов (для дальнейшей возможности смены объектов)
    private void responseConversion(JsonArray response, int length) {
        for (int i = 0; i < length; i++) {
            JsonElement id = response.get(i).getAsJsonObject().get("_id");
            JsonElement name = response.get(i).getAsJsonObject().get("name");
            if(response.get(i).getAsJsonObject().get("config") != null) {
                if (response.get(i).getAsJsonObject().get("config").getAsJsonObject().get("levels") != null) {
                    setCriticalValues(response.get(i).getAsJsonObject().get("config").getAsJsonObject().get("levels"), response.get(i).getAsJsonObject().get("state").getAsJsonObject().get("_ts").getAsString());
                }
            }

            names.add(name.getAsString());
            ids.add(id.getAsString());
        }

    }

    private void setCriticalValues(JsonElement configLevels, String miliS){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        String isFirst = preferences.getString("isFirst", null);
        if(isFirst == null){
            editor.putString("thisTime", miliS);
            editor.putString("isFirst", "false");
        }
        try {
            editor.putString("EmulsioncalcHigh", configLevels.getAsJsonObject().get("emulsioncalc").getAsJsonObject().get("High").getAsString());
            editor.putString("EmulsioncalcLow", configLevels.getAsJsonObject().get("emulsioncalc").getAsJsonObject().get("Low").getAsString());
            editor.putString("EmulsioncalcMax", configLevels.getAsJsonObject().get("emulsioncalc").getAsJsonObject().get("Max").getAsString());
            editor.putString("EmulsioncalcMin", configLevels.getAsJsonObject().get("emulsioncalc").getAsJsonObject().get("Min").getAsString());

            editor.putString("LevelHigh", configLevels.getAsJsonObject().get("level").getAsJsonObject().get("High").getAsString());
            editor.putString("LevelLow", configLevels.getAsJsonObject().get("level").getAsJsonObject().get("Low").getAsString());
            editor.putString("LevelMax", configLevels.getAsJsonObject().get("level").getAsJsonObject().get("Max").getAsString());
            editor.putString("LevelMin", configLevels.getAsJsonObject().get("level").getAsJsonObject().get("Min").getAsString());

            editor.putString("phHigh", configLevels.getAsJsonObject().get("ph").getAsJsonObject().get("High").getAsString());
            editor.putString("phLow", configLevels.getAsJsonObject().get("ph").getAsJsonObject().get("Low").getAsString());
            editor.putString("phMax", configLevels.getAsJsonObject().get("ph").getAsJsonObject().get("Max").getAsString());
            editor.putString("phMin", configLevels.getAsJsonObject().get("ph").getAsJsonObject().get("Min").getAsString());

            editor.putString("temp_phHigh", configLevels.getAsJsonObject().get("temp_ph").getAsJsonObject().get("High").getAsString());
            editor.putString("temp_phLow", configLevels.getAsJsonObject().get("temp_ph").getAsJsonObject().get("Low").getAsString());
            editor.putString("temp_phMax", configLevels.getAsJsonObject().get("temp_ph").getAsJsonObject().get("Max").getAsString());
            editor.putString("temp_phMin", configLevels.getAsJsonObject().get("temp_ph").getAsJsonObject().get("Min").getAsString());
            editor.apply();
        } catch (Exception e){
            Toast.makeText(this, "Скорее всего, на сервере что-то поменялось. Сообщите в поддержку", Toast.LENGTH_LONG).show();
        }
    }

}
