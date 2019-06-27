package com.example.app_for_rightech_iot_cloud;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private TextView textViewWorkTime;
    private TextView textViewNotWorkTime;
    private TextView textViewDifference;
    private TextView textViewOnTimeH;
    private TextView textViewOnTimeM;
    private TextView textViewOffTimeH;
    private TextView textViewOffTimeM;
    private SwipeRefreshLayout mSwipeRefresh;

    private Context context = getContext();
    private static final String BASE_URL = "https://rightech.lab.croc.ru/";

    private String id;
    private String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_now, container, false);
        context = getContext();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        mSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.basicLayout);

        //Настраиваем выполнение OnRefreshListener для данной activity:
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        //Останавливаем обновление:
                        mSwipeRefresh.setRefreshing(false)
                        ;}}, 500);
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        if (preferences.getString("theme", "light").equals("dark")){
            rootView.findViewById(R.id.basicLayout).setBackgroundColor(Color.parseColor("#18191D"));
            rootView.findViewById(R.id.constraint1).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint2).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint3).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint4).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint5).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint6).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint7).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint8).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint9).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint10).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint13).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint14).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint15).setBackgroundResource(R.drawable.dark_frame);
            rootView.findViewById(R.id.constraint16).setBackgroundResource(R.drawable.dark_frame);
        }
        else{
            rootView.findViewById(R.id.basicLayout).setBackgroundColor(Color.parseColor("#ffffff"));
            rootView.findViewById(R.id.constraint1).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint2).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint3).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint4).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint5).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint6).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint7).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint8).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint9).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint10).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint13).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint14).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint15).setBackgroundResource(R.drawable.frame);
            rootView.findViewById(R.id.constraint16).setBackgroundResource(R.drawable.frame);
        }


        textViewNowDate = rootView.findViewById(R.id.text_view_date);
        textViewNowTime = rootView.findViewById(R.id.text_view_time);
        textViewRNTemp = rootView.findViewById(R.id.text_view_temperature);
        textViewIndicatorRN = rootView.findViewById(R.id.text_view_rN);
        textViewTemp = rootView.findViewById(R.id.text_view_SOZ);
        textViewDensity = rootView.findViewById(R.id.text_view_density);
        textViewLevel = rootView.findViewById(R.id.text_view_level);
        textViewWorkTime = rootView.findViewById(R.id.text_view_work_time);
        textViewNotWorkTime = rootView.findViewById(R.id.text_view_notWork_time);
        textViewPumpWork = rootView.findViewById(R.id.text_view_pump_work);
        textViewDifference = rootView.findViewById(R.id.text_view_difference);
        textViewOnTimeH = rootView.findViewById(R.id.text_view_on_timeH);
        textViewOnTimeM = rootView.findViewById(R.id.text_view_on_timeM);
        textViewOffTimeH = rootView.findViewById(R.id.text_view_off_timeH);
        textViewOffTimeM = rootView.findViewById(R.id.text_view_off_timeM);

        id = preferences.getString("id", null);
        name = preferences.getString("name", null);


        if (getContext() != null && !isOnline(getContext())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        } else if (getContext()!=null) {
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
                    // переписать, чтобы можно было выбирать из toolbar
                    if (id != null && name != null) {
                        findElement(id, name, response.body());
                    } else {
                        if(context!=null) {
                            Toast.makeText(context, "Произошла ошибка. Id и/или имя объекта не найдены", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    if(context != null) {
                        Toast.makeText(context, "Нет ответа от сервера", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                if(context!=null) {
                    Toast.makeText(getContext(), "error " + t, Toast.LENGTH_SHORT).show();
                }
                Log.i("Request", "error " + t);
            }
        });
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
            String timeDiff = getDataFromJson(state, "timediff");
            String prevTime = getDataFromJson(state, "prevtime"); // время фиксации

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
                timeObj = Long.parseLong(timeObject);
                Date dateObj = new Date(timeObj / 1000);
                textViewNowDate.setText(formatDate.format(dateObj));
                textViewNowTime.setText(formatTime.format(dateObj));

            } catch (Exception e) {
                textViewNowDate.setText("Null");
                textViewNowTime.setText("Null");
            }

            try {
                timePr = Long.parseLong(prevTime);
                Date datePrev = new Date(timePr / 1000);
                //textViewFixTime.setText(formatTime.format(datePrev)); // время фиксации (время)
                //textViewFixDate.setText(formatDate.format(datePrev)); // время фиксации (дата)

            } catch (Exception e) {
            }

            try {
                timeW = Long.parseLong(workTime);
                Date timeWork = new Date(timeW / 1000);
                textViewWorkTime.setText(workTime);
            } catch (Exception e) {
            }

            try {
                timeIdle = Long.parseLong(idleTime);
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
            textViewOnTimeH.setText(nTonH);
            textViewOnTimeM.setText(nTonM);
            textViewOffTimeH.setText(nTofH);
            textViewOffTimeM.setText(nTofM);
        } else {
            if(context!=null) {
                Toast.makeText(context, "Невозможно отобразить информацию, т.к. объект выключен", Toast.LENGTH_LONG).show();
            }
        }
    }

    // получаем значение из json
    private String getDataFromJson(JsonElement state, String id) {
        String result = "null";
        try {
            if (state.getAsJsonObject().get(id) != null) {
                result = state.getAsJsonObject().get(id).getAsString();
            }
        } catch (Exception e){
            if(context!=null) {
                Toast.makeText(context, "Вероятнее всего чать или все измерения не найдены", Toast.LENGTH_LONG).show();
            }
        }
        return result;
    }

    // округляем до places знаков после запятой (принимает String значения)
    private static double round(String value, int places) {
        try {
            if (places < 0) throw new IllegalArgumentException();

            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        } catch (Exception e){
            return 0;
        }
    }

    private static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
