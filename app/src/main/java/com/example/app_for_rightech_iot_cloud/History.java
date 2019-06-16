package com.example.app_for_rightech_iot_cloud;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class History extends Fragment {

    // написать метод получения милисекунд (или микро. Хз, если чесн) из даты и времени
    // написать метод получения всех времен (или за этот день) и затем поиск ближайшего времени к заданному
    // все связать
    // сделать красиво
    // показ норма 2 времен (и в now)
    // сделать красиво историю

    private TimePicker mTimePicker;
    private TextView lastDay;
    private TextView now;

    private static final String BASE_URL = "https://rightech.lab.croc.ru/";

    private Calendar calendar = Calendar.getInstance();

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

    private JsonArray crutchJsonArray;

    private List<Long> arrayOfTimeInDay = new ArrayList<>();

    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_hisrory, container, false);

        mTimePicker = rootView.findViewById(R.id.timePicker);

        mTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        mTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        mTimePicker.setIs24HourView(true);
        now = rootView.findViewById(R.id.now);
        lastDay = rootView.findViewById(R.id.lastDay);

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

        long begin = 1552893453841L;
        long end = 1552893453841L;
        id = "5c65c98449cc586cdfa0fc26"; // написать получение id из SharedPreference


        if (!isOnline(Objects.requireNonNull(getContext()))) {
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
            //getResponse(id, begin, end);
        }

        return rootView;
    }

    public void setDate(View v) {
        new DatePickerDialog(getContext(), d,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
            Toast.makeText(getContext(), setInitialDateTime(), Toast.LENGTH_SHORT).show();
            //newTime();
        }
    };

    // для календаря
    private String setInitialDateTime() {
        return (DateUtils.formatDateTime(getContext(),
                calendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    // получаем ответ от сервера. Кст, костыль. Неплохо было бы его переписать
    private JsonArray getResponse(String id, long begin, long end) {
        JsonArray array;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiHistory apiHistory = retrofit.create(ApiHistory.class);

        apiHistory.allObject(id, begin, end).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.body() != null) {
                    Log.i("Request", response.body().toString());
                    crutchJsonArray = response.body();
                } else {
                    // написать обработку
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getContext(), "error " + t, Toast.LENGTH_SHORT).show();
                Log.i("Request", "error " + t);
            }
        });
        return null;
    }

    // устанавливаем значения
    private void setValues(JsonArray nowElement1) {
        for (int i = 0; i < nowElement1.size(); i++) {
            JsonElement state = nowElement1.get(i);

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
                textViewFixTime.setText(formatTime.format(datePrev)); // время фиксации (время)
                textViewFixDate.setText(formatDate.format(datePrev)); // время фиксации (дата)

            } catch (Exception e) {
                textViewFixTime.setText("Null");
                textViewFixDate.setText("Null");
            }

            try {
                timeW = Long.parseLong(workTime);
                Date timeWork = new Date(timeW / 1000);
                textViewWorkTime.setText(workTime);
            } catch (Exception e) {
                textViewFixTime.setText("Null");
                textViewFixDate.setText("Null");
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
            textViewWorkReset.setText(workReset); // workreset
            textViewOnTimeH.setText(nTonH);
            textViewOnTimeM.setText(nTonM);
            textViewOffTimeH.setText(nTofH);
            textViewOffTimeM.setText(nTofM);

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

    // возвращает true, если есть подключение к интернету
    private static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    // функция принимает дату и время и отдает милисекунды
    private long getMilisecond(String myDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long miliS = date.getTime();

        return miliS;
    }

    // записываем все милисекунды за день в массив
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getAllMs(String thisDate, String thisTime) {
        long thisMs = getMilisecond(thisDate + " " + thisTime);
        long startMS = getMilisecond(thisDate + " " + "00:00:00");

        JsonArray mainArray;
        if (!isOnline(Objects.requireNonNull(getContext()))) {
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
            // костыль))) Надо бы переписать :)
            getResponse(id, startMS, thisMs);
            mainArray = crutchJsonArray;
            crutchJsonArray = new JsonArray();


            for (int i = 0; i < mainArray.size(); i++) {
                String timeStr = mainArray.get(i).getAsJsonObject().get("time").getAsString();
                arrayOfTimeInDay.add(Long.parseLong(timeStr));
                // отсюда продолжать
            }

        }
    }

    // метод обновления информации на основе новой даты
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void newTime() {
        if (!isOnline(Objects.requireNonNull(getContext()))) {
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
            // начать преобразоввывать
        }
    }


}
