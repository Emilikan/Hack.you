package com.example.app_for_rightech_iot_cloud;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Now extends Fragment {

    private TextView textViewNowDate;
    private TextView textViewNowTime;
    private TextView textViewRNTemp;
    private TextView textViewIndicatorRN;
    private TextView textViewTemp;
    private TextView textViewDensity;
    private TextView textViewLevel;
    private TextView textViewPumpWork;
    private TextView textViewControl;
    private TextView textViewWorkTime;
    private TextView textViewNotWorkTime;
    private TextView textViewDifference;
    private TextView textViewFixTime;
    private TextView textViewFixDate;
    private TextView textViewWorkReset;
    private TextView textViewOnTimeH;
    private TextView textViewOnTimeM;
    private TextView textViewOffTimeH;
    private TextView textViewOffTimeM;

    private static final String BASE_URL = "https://rightech.lab.croc.ru/";

    private HashMap<String, String> elements = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

        if(!isOnline(Objects.requireNonNull(getContext()))){
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            builder.setTitle("Warning")
                    .setMessage("Нет доступа в интернет. Проверьте наличие связи")
                    .setCancelable(false)
                    .setNegativeButton("Ок, закрыть",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            serverRequest();
        }

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

            String timeObject = getDataFromJson(state, "_ts"); // время объекта
            String tempPh = getDataFromJson(state, "temp_ph"); // температура по данным pH-метра (в градусах по цельсию)
            String tempRef = getDataFromJson(state, "temp_ref"); // температура по данным рефактометра (в градусах по цельсию)
            String level = getDataFromJson(state, "level"); // уровень СОЖ
            String emulsioncalc = getDataFromJson(state, "emulsioncalc"); // концентрация эмульсии
            String ph = getDataFromJson(state, "ph"); // показатель pH
            String active = getDataFromJson(state, "ctrl_wrd_work"); // состояние насоса
            String workTime = getDataFromJson(state, "worktime"); // время работы
            String idleTime = getDataFromJson(state, "idletime"); // время простоя
            String nTonH = getDataFromJson(state, "ntonh"); // время включения (часы)
            String nTonM = getDataFromJson(state, "ntonm"); // время включения (минуты)
            String nTofH = getDataFromJson(state, "ntofh"); // время выключения (часы)
            String nTofM = getDataFromJson(state, "ntofm"); // время выключения (минуты)
            String workReset = getDataFromJson(state, "workreset");
            String timeDiff = getDataFromJson(state, "timediff");
            String prevTime = getDataFromJson(state, "prevtime"); // время фиксации

            if (workReset.equals("true")) {
                workReset = "Да";
            } else if (workReset.equals("false")) {
                workReset = "Нет";
            }
            if (active.equals("true")) {
                active = "Да";
            } else if (active.equals("false")) {
                active = "Нет";
            }

            long timeObj;
            long timePr;
            long timeW;
            long timeIdle;

            SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss");

            try {
                timeObj=Long.parseLong(timeObject);
                Date dateObj = new Date(timeObj / 1000);
                textViewNowDate.setText(formatDate.format(dateObj));
                textViewNowTime.setText(formatTime.format(dateObj));

            } catch (Exception e) {
                textViewNowDate.setText("Null");
                textViewNowTime.setText("Null");
            }

            try {
                timePr=Long.parseLong(prevTime);
                Date datePrev = new Date(timePr / 1000);
                textViewFixTime.setText(formatTime.format(datePrev)); // время фиксации (время)
                textViewFixDate.setText(formatDate.format(datePrev)); // время фиксации (дата)

            } catch (Exception e) {
                textViewFixTime.setText("Null");
                textViewFixDate.setText("Null");
            }

            try {
                timeW=Long.parseLong(workTime);
                Date timeWork = new Date(timeW / 1000);
                textViewWorkTime.setText(workTime);
            } catch (Exception e) {
                textViewFixTime.setText("Null");
                textViewFixDate.setText("Null");
            }

            try {
                timeIdle=Long.parseLong(idleTime);
                Date timeStop = new Date(timeIdle / 1000);
                textViewNotWorkTime.setText(idleTime);
            } catch (Exception e) {
                textViewWorkTime.setText("Null");
                textViewNotWorkTime.setText("Null");
            }

            textViewRNTemp.setText(round(tempPh, 2) + " \u2103"); // температура сож
            textViewIndicatorRN.setText(Double.toString(round(ph, 2)));
            textViewTemp.setText(round(tempRef, 2) + " \u2103"); // температура сож
            textViewDensity.setText(round(emulsioncalc, 2) + " %"); // концентрация эмульсии
            textViewLevel.setText(round(level, 2) + " м"); // уровень сож в м
            textViewPumpWork.setText(active); // работает ли насос
            textViewDifference.setText(timeDiff);
            textViewWorkReset.setText(workReset); // workreset
            textViewOnTimeH.setText(nTonH);
            textViewOnTimeM.setText(nTonM);
            textViewOffTimeH.setText(nTofH);
            textViewOffTimeM.setText(nTofM);
        } else {
            Toast.makeText(getContext(), "Невозможно отобразить информацию, т.к. объект выключен", Toast.LENGTH_LONG).show();
        }
    }

    // получаем значение из json
    private String getDataFromJson(JsonElement state, String id) {
        String result = "null";
        if (state.getAsJsonObject().get(id) != null) {
            result = state.getAsJsonObject().get(id).getAsString();
        }
        return result;
    }

    // округляем до places знаков после запятой (принимает String значения)
    private static double round(String value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private static boolean isOnline (Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
