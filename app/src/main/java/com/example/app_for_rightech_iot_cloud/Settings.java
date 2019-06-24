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
import android.widget.Switch;
import android.widget.TextView;

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
        final Switch changeTheme = rootView.findViewById(R.id.switch1);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final SharedPreferences.Editor editor = preferences.edit();
        if (Objects.equals(preferences.getString("theme", "light"), "dark")){
            rootView.findViewById(R.id.settingsLayout).setBackgroundColor(Color.parseColor("#18191D"));
            rootView.findViewById(R.id.layout1).setBackgroundResource(R.drawable.dark_recycler_view_frame);
            rootView.findViewById(R.id.layout2).setBackgroundResource(R.drawable.dark_recycler_view_frame);
            rootView.findViewById(R.id.layout3).setBackgroundResource(R.drawable.dark_recycler_view_frame);
            TextView text = rootView.findViewById(R.id.textView);
            text.setTextColor(Color.parseColor("#E9E9E9"));
            TextView text2 = rootView.findViewById(R.id.textView5);
            text2.setTextColor(Color.parseColor("#E9E9E9"));
            TextView text4 = rootView.findViewById(R.id.textView7);
            text4.setTextColor(Color.parseColor("#E9E9E9"));
            TextView text5 = rootView.findViewById(R.id.textView2);
            text5.setTextColor(Color.parseColor("#E9E9E9"));
            TextView text6 = rootView.findViewById(R.id.textView3);
            text6.setTextColor(Color.parseColor("#E9E9E9"));
            TextView text7 = rootView.findViewById(R.id.textView12);
            text7.setTextColor(Color.parseColor("#E9E9E9"));
            TextView text9 = rootView.findViewById(R.id.textView14);
            text9.setTextColor(Color.parseColor("#E9E9E9"));
            changeTheme.setChecked(true);
        }
        else{
            rootView.findViewById(R.id.settingsLayout).setBackgroundColor(Color.parseColor("#ffffff"));
            rootView.findViewById(R.id.layout1).setBackgroundResource(R.drawable.recycler_view_frame);
            rootView.findViewById(R.id.layout2).setBackgroundResource(R.drawable.recycler_view_frame);
            rootView.findViewById(R.id.layout3).setBackgroundResource(R.drawable.recycler_view_frame);
            TextView text = rootView.findViewById(R.id.textView);
            text.setTextColor(Color.parseColor("#18191D"));
            TextView text2 = rootView.findViewById(R.id.textView5);
            text2.setTextColor(Color.parseColor("#000000"));
            TextView text4 = rootView.findViewById(R.id.textView7);
            text4.setTextColor(Color.parseColor("#000000"));
            TextView text5 = rootView.findViewById(R.id.textView2);
            text5.setTextColor(Color.parseColor("#18191D"));
            TextView text6 = rootView.findViewById(R.id.textView3);
            text6.setTextColor(Color.parseColor("#18191D"));
            TextView text7 = rootView.findViewById(R.id.textView12);
            text7.setTextColor(Color.parseColor("#000000"));
            TextView text9 = rootView.findViewById(R.id.textView14);
            text9.setTextColor(Color.parseColor("#000000"));
            changeTheme.setChecked(false);
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
