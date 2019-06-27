package com.example.app_for_rightech_iot_cloud;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Notifications extends Fragment {
    private List<NotificationsForRecycler> notifications = new ArrayList<>();

    private String notificationEmulsioncalc;
    private String notificationEmulsioncalcTime;
    private String showEmulsioncalc;

    private String notificationLevel;
    private String notificationLevelTime;
    private String showLevel;

    private String notificationPh;
    private String notificationPhTime;
    private String showPh;

    private String notificationTemp_ph;
    private String notificationTemp_phTime;
    private String showTemp_ph;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        if (preferences.getString("theme", "light").equals("dark")){
            rootView.findViewById(R.id.layoutNotific).setBackgroundColor(Color.parseColor("#18191D"));
        }
        else{
            rootView.findViewById(R.id.layoutNotific).setBackgroundColor(Color.parseColor("#ffffff"));
        }

        notificationEmulsioncalc = preferences.getString("notificationEmulsioncalc", null);
        notificationEmulsioncalcTime = preferences.getString("notificationEmulsioncalcTime", null);
        showEmulsioncalc = preferences.getString("showEmulsioncalc", "false");

        notificationLevel = preferences.getString("notificationLevel", null);
        notificationLevelTime = preferences.getString("notificationLevelTime", null);
        showLevel = preferences.getString("showLevel", "false");

        notificationPh = preferences.getString("notificationPh", null);
        notificationPhTime = preferences.getString("notificationPhTime", null);
        showPh = preferences.getString("showPh", "false");

        notificationTemp_ph = preferences.getString("notificationTemp_ph", null);
        notificationTemp_phTime = preferences.getString("notificationTemp_phTime", null);
        showTemp_ph = preferences.getString("showTemp_ph", "false");

        Log.i("level", "notificationLevel="+notificationLevel + " notificationLevelTime=" + notificationLevelTime + " showLevel="+showLevel);

        setInitialData();
        RecyclerView recyclerView = rootView.findViewById(R.id.list);
        DataAdapter adapter = new DataAdapter(getContext(), notifications);
        recyclerView.setAdapter(adapter);
        return rootView;
    }


    private void setInitialData() {
        if(notificationEmulsioncalc != null && notificationEmulsioncalcTime != null && !showEmulsioncalc.equals("false")) {
            notifications.add(new NotificationsForRecycler( notificationEmulsioncalc + " %", notificationEmulsioncalcTime, R.raw.density, R.raw.alert, "Концентрация эмульсии"));
        }
        if(notificationLevel != null && notificationLevelTime != null && !showLevel.equals("false")) {
            notifications.add(new NotificationsForRecycler(notificationLevel + " м", notificationLevelTime, R.raw.level, R.raw.alert, "Уровень СОЖ"));
        }
        if(notificationPh != null && notificationPhTime != null && !showPh.equals("false")) {
            notifications.add(new NotificationsForRecycler(notificationPh, notificationPhTime, R.raw.rn_indicator, R.raw.alert, "Показатель рН"));
        }
        if(notificationTemp_ph != null && notificationTemp_phTime != null && !showTemp_ph.equals("false")) {
            notifications.add(new NotificationsForRecycler(notificationTemp_ph + " С", notificationTemp_phTime, R.raw.temperature, R.raw.alert, "Температура СОЖ"));
        }
    }
}
