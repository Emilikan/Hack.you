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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Notifications extends Fragment {
    private List<NotificationsForRecycler> notifications = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        if (Objects.equals(preferences.getString("theme", "light"), "dark")){
            rootView.findViewById(R.id.layoutNotific).setBackgroundColor(Color.parseColor("#18191D"));
        }
        else{
            rootView.findViewById(R.id.layoutNotific).setBackgroundColor(Color.parseColor("#ffffff"));
        }

        setInitialData();
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        DataAdapter adapter = new DataAdapter(getContext(), notifications);
        recyclerView.setAdapter(adapter);
        return rootView;
    }


    private void setInitialData() {
        notifications.add(new NotificationsForRecycler ("42.2 C", "23.09.2012", R.raw.temperature,R.raw.alert,"Температура СОЖ"));
        notifications.add(new NotificationsForRecycler ("8.76 %", "24.09.2013", R.raw.density,R.raw.alert,"Концентрация эмульсии"));
        notifications.add(new NotificationsForRecycler ("9.3", "21.08.2093", R.raw.rn_indicator,R.raw.alert,"Показатель рН"));
        notifications.add(new NotificationsForRecycler("23.7 %", "30.09.1999", R.raw.density,R.raw.alert,"Что-то"));
    }
}
