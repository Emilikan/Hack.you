package com.example.app_for_rightech_iot_cloud;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class History extends Fragment {
    private TimePicker mTimePicker;
    private TextView lastDay;
    private TextView now;
    Calendar calendar = Calendar.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_hisrory, container, false);


        mTimePicker = (TimePicker) rootView.findViewById(R.id.timePicker);


        mTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        mTimePicker.setIs24HourView(true);
        now = rootView.findViewById(R.id.now);
        lastDay = rootView.findViewById(R.id.lastDay);

        now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment fragment = new MainFragment();
                assert fragmentManager != null;
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            }
        });
        lastDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(getView());

            }
        });
        return rootView;
    }

    public void setDate(View v) {
        new DatePickerDialog(getContext(), d,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
            Toast.makeText(getContext(),setInitialDateTime(),Toast.LENGTH_SHORT).show();
        }
    };
    private String setInitialDateTime() {
        return(DateUtils.formatDateTime(getContext(),
                calendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

}
