package com.example.app_for_rightech_iot_cloud;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
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

public class Settings extends Fragment {
    int leftArrow;
    int notification;
    int artificialIntelligence;
    int settings;
    private static final String BASE_URL = "https://rightech.lab.croc.ru/";
    private ArrayList<String> names;
    private ArrayList<String> ids;
    private int mPosition;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final TextView title = getActivity().findViewById(R.id.title);
        final ImageView leftButton = getActivity().findViewById(R.id.notific);
        final ImageView rightButton = getActivity().findViewById(R.id.settings);
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        final Switch changeTheme = rootView.findViewById(R.id.switch1);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final SharedPreferences.Editor editor = preferences.edit();
        TextView aboutUs = rootView.findViewById(R.id.know);
        TextView question = rootView.findViewById(R.id.question);
        final Switch notifications = rootView.findViewById(R.id.switch2);
        names = new ArrayList<>();
        ids = new ArrayList<>();
        final TextView factory = rootView.findViewById(R.id.factory);
        if (preferences.getString("Notifications", "TRUE").equals("TRUE")){
            notifications.setChecked(true);
        }
        else {
            notifications.setChecked(false);
        }
      
        if (preferences.getString("theme", "light").equals("dark")){
            getActivity().setTheme(R.style.DarkTheme);
            getActivity().findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor("#282E33"));
            title.setTextColor(Color.parseColor("#E9E9E9"));
            leftArrow = R.drawable.left_arrow_white;
            notification = R.drawable.notification_white;
            artificialIntelligence = R.drawable.artifical_intelligence_white;
            settings = R.drawable.settings_white;
            leftButton.setImageResource(leftArrow);
            rightButton.setImageResource(notification);
            rootView.findViewById(R.id.settingsLayout).setBackgroundColor(Color.parseColor("#18191D"));
            rootView.findViewById(R.id.layout1).setBackgroundResource(R.drawable.dark_recycler_view_frame);
            rootView.findViewById(R.id.layout2).setBackgroundResource(R.drawable.dark_recycler_view_frame);
            rootView.findViewById(R.id.layout5).setBackgroundResource(R.drawable.dark_recycler_view_frame);
            rootView.findViewById(R.id.layout3).setBackgroundResource(R.drawable.dark_recycler_view_frame);
            TextView text = rootView.findViewById(R.id.textView);
            text.setTextColor(Color.parseColor("#E9E9E9"));
            TextView text2 = rootView.findViewById(R.id.textView5);
            text2.setTextColor(Color.parseColor("#E9E9E9"));
            TextView text5 = rootView.findViewById(R.id.textView2);
            text5.setTextColor(Color.parseColor("#E9E9E9"));
            TextView text6 = rootView.findViewById(R.id.textView3);
            text6.setTextColor(Color.parseColor("#E9E9E9"));
            TextView text7 = rootView.findViewById(R.id.textView12);
            text7.setTextColor(Color.parseColor("#E9E9E9"));
            TextView text9 = rootView.findViewById(R.id.textView14);
            text9.setTextColor(Color.parseColor("#E9E9E9"));
            changeTheme.setTextColor(Color.parseColor("#E9E9E9"));
            notifications.setTextColor(Color.parseColor("#E9E9E9"));
            changeTheme.setChecked(true);
        }
        else{
            getActivity().setTheme(R.style.AppTheme);
            getActivity().findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor("#ffffff"));
            title.setTextColor(Color.parseColor("#000000"));
            leftArrow = R.drawable.left_arrow;
            notification = R.drawable.notification;
            artificialIntelligence = R.drawable.artificial_intelligence;
            settings = R.drawable.settings;
            leftButton.setImageResource(leftArrow);
            rightButton.setImageResource(notification);
            rootView.findViewById(R.id.settingsLayout).setBackgroundColor(Color.parseColor("#ffffff"));
            rootView.findViewById(R.id.layout1).setBackgroundResource(R.drawable.recycler_view_frame);
            rootView.findViewById(R.id.layout5).setBackgroundResource(R.drawable.recycler_view_frame);
            rootView.findViewById(R.id.layout2).setBackgroundResource(R.drawable.recycler_view_frame);
            rootView.findViewById(R.id.layout3).setBackgroundResource(R.drawable.recycler_view_frame);
            TextView text = rootView.findViewById(R.id.textView);
            text.setTextColor(Color.parseColor("#18191D"));
            TextView text2 = rootView.findViewById(R.id.textView5);
            text2.setTextColor(Color.parseColor("#000000"));
            TextView text5 = rootView.findViewById(R.id.textView2);
            text5.setTextColor(Color.parseColor("#18191D"));
            TextView text6 = rootView.findViewById(R.id.textView3);
            text6.setTextColor(Color.parseColor("#18191D"));
            TextView text7 = rootView.findViewById(R.id.textView12);
            text7.setTextColor(Color.parseColor("#000000"));
            TextView text9 = rootView.findViewById(R.id.textView14);
            text9.setTextColor(Color.parseColor("#000000"));
            changeTheme.setTextColor(Color.parseColor("#000000"));
            notifications.setTextColor(Color.parseColor("#000000"));
            changeTheme.setChecked(false);
        }
        setNamesAndId();

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().findViewById(R.id.title);
                title.setText("О нас");
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new AboutUs();
                assert fragmentManager != null;
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            }
        });

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().findViewById(R.id.title);
                title.setText("Вопрос");
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new Question();
                assert fragmentManager != null;
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            }
        });

        factory.setText(preferences.getString("Factory","Не выбрано"));
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences.getString("Notifications", "").equals("TRUE")){
                    editor.putString("Notifications","FALSE");
                }
                else {
                    editor.putString("Notifications","TRUE");
                }
                editor.apply();

            }
        });

        TextView exit = rootView.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("login", null);
                editor.putString("password", null);
                editor.apply();

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        changeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (preferences.getString("theme", "light").equals("dark")){
                    changeTheme.setText("Темная тема");
                    editor.putString("theme","light");
                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment fragment = new Settings();
                    assert fragmentManager != null;
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                }
                else{
                    editor.putString("theme","dark");
                    FragmentManager fragmentManager =getFragmentManager();
                    Fragment fragment = new Settings();
                    assert fragmentManager != null;
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                }

                editor.apply();
            }
        });
        factory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int p;

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = LayoutInflater.from(getContext()).inflate(R.layout.alert, null);
                builder.setView(view);
                builder.setCancelable(true);
                Spinner spinner = view.findViewById(R.id.spinner);
                ArrayAdapter<?> adapter =
                        new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, names);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setGravity(Gravity.CENTER);
                spinner.setAdapter(adapter);

                builder.setNegativeButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(names.size() > 0) {
                            editor.putString("Factory",names.get(mPosition));
                            editor.apply();
                            factory.setText(names.get(mPosition));
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);
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
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
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
        return rootView;
    }
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
                    Toast.makeText(getContext(), "Нет ответа от сервера", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getContext(), "error " + t, Toast.LENGTH_SHORT).show();
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
