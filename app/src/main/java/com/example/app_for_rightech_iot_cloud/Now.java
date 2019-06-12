package com.example.app_for_rightech_iot_cloud;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Now extends Fragment {
    String nowDate;
    String nowTime;
    String RNTemp;
    String indicatorRN;
    String temp;
    String density;
    String level;
    String pumpWork;
    String control;
    String workTime;
    String notWorkTime;
    String difference;
    String fixTime;
    String fixDate;
    String workReset;
    String onTimeH;
    String onTimeM;
    String offTimeH;
    String offTimeM;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_now, container, false);
        TextView textViewNowDate = rootView.findViewById(R.id.text_view_date);
        TextView textViewNowTime = rootView.findViewById(R.id.text_view_time);
        TextView textViewRNTemp = rootView.findViewById(R.id.text_view_temperature);
        TextView textViewIndicatorRN = rootView.findViewById(R.id.text_view_rN);
        TextView textViewTemp = rootView.findViewById(R.id.text_view_SOZ);
        TextView textViewDensity = rootView.findViewById(R.id.text_view_density);
        TextView textViewLevel = rootView.findViewById(R.id.text_view_level);
        TextView textViewPumpWork = rootView.findViewById(R.id.text_view_pump_work);
        TextView textViewControl = rootView.findViewById(R.id.text_view_count);
        TextView textViewWorkTime = rootView.findViewById(R.id.text_view_work_time);
        TextView textViewNotWorkTime = rootView.findViewById(R.id.text_view_notWork_time);
        TextView textViewDifference = rootView.findViewById(R.id.text_view_difference);
        TextView textViewFixTime = rootView.findViewById(R.id.text_view_fix_time);
        TextView textViewFixDate = rootView.findViewById(R.id.text_view_fix_date);
        TextView textViewWorkReset = rootView.findViewById(R.id.text_view_workReset);
        TextView textViewOnTimeH = rootView.findViewById(R.id.text_view_on_timeH);
        TextView textViewOnTimeM = rootView.findViewById(R.id.text_view_on_timeM);
        TextView textViewOffTimeH = rootView.findViewById(R.id.text_view_off_timeH);
        TextView textViewOffTimeM = rootView.findViewById(R.id.text_view_off_timeM);
        nowDate = textViewNowDate.getText().toString();
        nowTime = textViewNowTime.getText().toString();
        RNTemp = textViewRNTemp.getText().toString();
        indicatorRN = textViewIndicatorRN.getText().toString();
        temp = textViewTemp.getText().toString();
        density = textViewDensity.getText().toString();
        level = textViewLevel.getText().toString();
        pumpWork = textViewPumpWork.getText().toString();
        control = textViewControl.getText().toString();
        workTime = textViewWorkTime.getText().toString();
        notWorkTime = textViewNotWorkTime.getText().toString();
        difference = textViewDifference.getText().toString();
        fixTime = textViewFixTime.getText().toString();
        fixDate = textViewFixDate.getText().toString();
        workReset = textViewWorkReset.getText().toString();
        onTimeH = textViewOnTimeH.getText().toString();
        onTimeM = textViewOnTimeM.getText().toString();
        offTimeH = textViewOffTimeH.getText().toString();
        offTimeM = textViewOffTimeM.getText().toString();
        return rootView;
    }

}
