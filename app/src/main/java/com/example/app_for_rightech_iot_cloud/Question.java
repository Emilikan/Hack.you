package com.example.app_for_rightech_iot_cloud;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Question extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_question, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (preferences.getString("theme", "light").equals("dark")){
            getActivity().setTheme(R.style.DarkTheme);
            rootView.findViewById(R.id.send).setBackgroundColor(Color.parseColor("#18191D"));
            rootView.findViewById(R.id.sendFeedback).setBackgroundResource(R.drawable.button_dark);

        }
        else{
            getActivity().setTheme(R.style.AppTheme);
            rootView.findViewById(R.id.send).setBackgroundColor(Color.parseColor("#ffffff"));
            rootView.findViewById(R.id.sendFeedback).setBackgroundResource(R.drawable.button_red);

        }
        return rootView;
    }

}
