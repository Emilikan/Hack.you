package com.example.app_for_rightech_iot_cloud;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Notifications extends Fragment {
    List<NotificationsForRecycler> notifications = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        setInitialData();
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        // создаем адаптер
        DataAdapter adapter = new DataAdapter(getContext(), notifications);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);
        return rootView;
    }
    private void setInitialData(){

        notifications.add(new NotificationsForRecycler ("42.2 C", "23.09.2012", R.raw.temperature,R.raw.alert,"Температура СОЖ"));
        notifications.add(new NotificationsForRecycler ("8.76 %", "24.09.2013", R.raw.density,R.raw.alert,"Концентрация эмульсии"));
        notifications.add(new NotificationsForRecycler ("9.3", "21.08.2093", R.raw.rn_indicator,R.raw.alert,"Показатель рН"));
        notifications.add(new NotificationsForRecycler("23.7 %", "30.09.1999", R.raw.density,R.raw.alert,"Что-то"));
    }
}
