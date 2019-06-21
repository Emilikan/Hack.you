package com.example.app_for_rightech_iot_cloud;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

public class Settings extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        final Button changeTheme = rootView.findViewById(R.id.button);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final SharedPreferences.Editor editor = preferences.edit();
        if (Objects.equals(preferences.getString("theme", "light"), "dark")){
            rootView.findViewById(R.id.settingsLayout).setBackgroundColor(Color.parseColor("#18191D"));
            changeTheme.setText("Светлая тема");
        }
        else{
            rootView.findViewById(R.id.settingsLayout).setBackgroundColor(Color.parseColor("#ffffff"));
            changeTheme.setText("Темная тема");
        }
        changeTheme.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (Objects.equals(preferences.getString("theme", "light"), "dark")){
                    changeTheme.setText("Темная тема");
                    editor.putString("theme","light");
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
                else{
                    changeTheme.setText("Светлая тема");
                    editor.putString("theme","dark");
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }

                editor.apply();
            }
        });
        return rootView;
    }

}
