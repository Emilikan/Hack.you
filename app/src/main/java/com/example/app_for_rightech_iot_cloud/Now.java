package com.example.app_for_rightech_iot_cloud;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Now extends Fragment {
    /*private String nowDate;
    private String nowTime;
    private String RNTemp;
    private String indicatorRN;
    private String temp;
    private String density;
    private String level;
    private String pumpWork;
    private String control;
    private String workTime;
    private String notWorkTime;
    private String difference;
    private String fixTime;
    private String fixDate;
    private String workReset;
    private String onTimeH;
    private String onTimeM;
    private String offTimeH;
    private String offTimeM;
    */

    TextView textViewNowDate;
    TextView textViewNowTime;
    TextView textViewRNTemp;
    TextView textViewIndicatorRN;
    TextView textViewTemp;
    TextView textViewDensity;
    TextView textViewLevel;
    TextView textViewPumpWork;
    TextView textViewControl;
    TextView textViewWorkTime;
    TextView textViewNotWorkTime;
    TextView textViewDifference;
    TextView textViewFixTime;
    TextView textViewFixDate;
    TextView textViewWorkReset;
    TextView textViewOnTimeH;
    TextView textViewOnTimeM;
    TextView textViewOffTimeH;
    TextView textViewOffTimeM;

    private static final String BASE_URL = "https://rightech.lab.croc.ru/";

    private HashMap<String, String> elements = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_now, container, false);
        textViewNowDate = rootView.findViewById(R.id.text_view_date);
        textViewNowTime = rootView.findViewById(R.id.text_view_time);
        textViewRNTemp = rootView.findViewById(R.id.text_view_temperature);
        textViewIndicatorRN = rootView.findViewById(R.id.text_view_rN);
        textViewTemp = rootView.findViewById(R.id.text_view_SOZ);
        textViewDensity = rootView.findViewById(R.id.text_view_density);
        textViewLevel = rootView.findViewById(R.id.text_view_level);
        textViewPumpWork = rootView.findViewById(R.id.text_view_pump_work);
        textViewControl = rootView.findViewById(R.id.text_view_count);
        textViewWorkTime = rootView.findViewById(R.id.text_view_work_time);
        textViewNotWorkTime = rootView.findViewById(R.id.text_view_notWork_time);
        textViewDifference = rootView.findViewById(R.id.text_view_difference);
        textViewFixTime = rootView.findViewById(R.id.text_view_fix_time);
        textViewFixDate = rootView.findViewById(R.id.text_view_fix_date);
        textViewWorkReset = rootView.findViewById(R.id.text_view_workReset);
        textViewOnTimeH = rootView.findViewById(R.id.text_view_on_timeH);
        textViewOnTimeM = rootView.findViewById(R.id.text_view_on_timeM);
        textViewOffTimeH = rootView.findViewById(R.id.text_view_off_timeH);
        textViewOffTimeM = rootView.findViewById(R.id.text_view_off_timeM);

        /*
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
        */

        serverRequest();

        return rootView;
    }

    private void serverRequest() {
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
                    findElement("5c65c98449cc586cdfa0fc26", "Метровагонмаш", response.body());
                } else {
                    // сделать обработку
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getContext(), "error " + t, Toast.LENGTH_SHORT).show();
                Log.i("Request", "error " + t);
            }
        });


    }

    // функция, которая принимает на вход массив ответа сервера и добавляет в HashMap всю известную информацию (для дальнейшей возможности смены объектов)
    private void responseConversion(JsonArray response, int length) {
        for (int i = 0; i < length; i++) {
            JsonElement id = response.get(i).getAsJsonObject().get("_id");
            JsonElement name = response.get(i).getAsJsonObject().get("name");

            elements.put(id.toString(), name.toString());
        }

    }

    // функция, которая принимаем id и name выбранного пользователем объекта и по этой информации ищет необходимый объект
    private void findElement(String id, String name, JsonArray response) {
        JsonElement nowElement;
        for (int i = 0; i < response.size(); i++) {
            // находим необходимый нам объект
            String newId = response.get(i).getAsJsonObject().get("_id").getAsString();
            String newName = response.get(i).getAsJsonObject().get("name").getAsString();
            if ((newId.equals(id)) && (newName.equals(name))) {
                nowElement = response.get(i);
                setValues(nowElement);
                break;
            }
        }

    }

    // устанавливаем значения
    private void setValues(JsonElement nowElement) {
        // смотрим, активен ли объект
        if (nowElement.getAsJsonObject().get("active").toString().equals("true")) {
            JsonElement state = nowElement.getAsJsonObject().get("state");


            String timeObject = state.getAsJsonObject().get("_ts").getAsString(); // время объекта
            String tempPh = state.getAsJsonObject().get("temp_ph").getAsString(); // температура по данным pH-метра (в градусах по цельсию)
            String tempRef = state.getAsJsonObject().get("temp_ref").getAsString(); // температура по данным рефактометра (в градусах по цельсию)
            String level = state.getAsJsonObject().get("level").getAsString(); // уровень СОЖ
            String emulsioncalc = state.getAsJsonObject().get("emulsioncalc").getAsString(); // концентрация эмульсии
            String ph = state.getAsJsonObject().get("ph").getAsString(); // показатель pH
            String active = state.getAsJsonObject().get("ctrl_wrd_work").getAsString(); // состояние насоса
            String workTime = state.getAsJsonObject().get("worktime").getAsString(); // время работы
            String idleTime = state.getAsJsonObject().get("idletime").getAsString(); // время простоя
            String nTonH = state.getAsJsonObject().get("ntonh").getAsString(); // время включения (часы)
            String nTonM = state.getAsJsonObject().get("ntonm").getAsString(); // время включения (минуты)
            String nTofH = state.getAsJsonObject().get("ntofh").getAsString(); // время выключения (часы)
            String nTofM = state.getAsJsonObject().get("ntofm").getAsString(); // время выключения (минуты)
            String workReset = state.getAsJsonObject().get("workreset").getAsString();
            String timeDiff = state.getAsJsonObject().get("timediff").getAsString();
            String prevTime = state.getAsJsonObject().get("prevtime").getAsString(); // время фиксации

            if(workReset.equals("true")){
                workReset = "Да";
            } else if(workReset.equals("false")){
                workReset = "Нет";
            }
            if(active.equals("true")){
                active = "Да";
            } else if(active.equals("false")){
                active = "Нет";
            }

            Date dateObj = new Date(Long.parseLong(timeObject));
            Date datePrev = new Date(Long.parseLong(prevTime));

            SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss");

            textViewNowDate.setText(formatDate.format(dateObj));
            textViewNowTime.setText(formatTime.format(dateObj));
            textViewRNTemp.setText(tempPh); // температура сож
            textViewIndicatorRN.setText(ph);
            textViewTemp.setText(tempRef); // температура сож
            textViewDensity.setText(emulsioncalc); // концентрация эмульсии
            textViewLevel.setText(level); // уровень сож в м
            textViewPumpWork.setText(active); // работает ли насос
            textViewWorkTime.setText(workTime);
            textViewNotWorkTime.setText(idleTime);
            textViewDifference.setText(timeDiff);
            textViewFixTime.setText(formatTime.format(datePrev)); // время фиксации (время)
            textViewFixDate.setText(formatDate.format(datePrev)); // время фиксации (дата)
            textViewWorkReset.setText(workReset); // workreset
            textViewOnTimeH.setText(nTonH);
            textViewOnTimeM.setText(nTonM);
            textViewOffTimeH.setText(nTofH);
            textViewOffTimeM.setText(nTofM);
        } else {
            Toast.makeText(getContext(), "Невозможно отобразить информацию, т.к. объект выключен", Toast.LENGTH_SHORT).show();
        }
    }


}
