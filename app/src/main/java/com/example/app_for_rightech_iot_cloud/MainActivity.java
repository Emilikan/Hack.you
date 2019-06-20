package com.example.app_for_rightech_iot_cloud;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_main);

        names = new ArrayList<>();
        ids = new ArrayList<>();

        title = findViewById(R.id.title);
        final ImageView leftButton = findViewById(R.id.notific);
        final ImageView rightButton = findViewById(R.id.neuronet);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
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
                    leftButton.setImageResource(R.drawable.notification);
                    rightButton.setImageResource(R.drawable.artificial_intelligence);
                    title.setText(nameOfTitle);
                }
                else {
                    if (title.getText() == "Нейросеть") {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        Fragment fragment = new MainFragment();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        leftButton.setImageResource(R.drawable.notification);
                        rightButton.setImageResource(R.drawable.artificial_intelligence);

                        title.setText(nameOfTitle);
                    }
                else{
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = new Notifications();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    leftButton.setImageResource(R.drawable.left_arrow);
                    rightButton.setImageResource(R.drawable.artificial_intelligence);

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
                    Fragment fragment = new Neuronet();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    leftButton.setImageResource(R.drawable.left_arrow);
                    rightButton.setImageResource(R.drawable.notification);

                    title.setText("Нейросеть");
                }
                else {
                    if (title.getText() == "Нейросеть") {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        Fragment fragment = new Notifications();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        leftButton.setImageResource(R.drawable.left_arrow);
                        rightButton.setImageResource(R.drawable.artificial_intelligence);

                        title.setText("Уведомления");
                    }
                    else{
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        Fragment fragment = new Neuronet();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        leftButton.setImageResource(R.drawable.left_arrow);
                        rightButton.setImageResource(R.drawable.notification);

                        title.setText("Нейросеть");
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

            names.add(name.getAsString());
            ids.add(id.getAsString());
        }

    }

}
