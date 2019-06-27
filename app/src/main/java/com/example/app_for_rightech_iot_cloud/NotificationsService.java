package com.example.app_for_rightech_iot_cloud;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationsService extends Service {

    private class MyTimer extends TimerTask implements Runnable{
        private Context context;
        private Calendar calendar = Calendar.getInstance();
        private static final String BASE_URL = "https://rightech.lab.croc.ru/";

        private String id = "5c65c98449cc586cdfa0fc26";
        private String name = "Метровагонмаш";
        MyTimer(Context context){
            this.context = context;
        }

        @Override
        public void run() {
           beforeSendNotification();
        }

        private void beforeSendNotification() {
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
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    Log.i("Request", "error " + t);
                }
            });
        }

        private void findElement(String id, String name, JsonArray response) {
            JsonElement nowElement;
            for (int i = 0; i < response.size(); i++) {
                // находим необходимый нам объект
                String newId = response.get(i).getAsJsonObject().get("_id").getAsString();
                String newName = response.get(i).getAsJsonObject().get("name").getAsString();
                if ((newId.equals(id)) && (newName.equals(name))) {
                    nowElement = response.get(i);
                    comparison(nowElement.getAsJsonObject().get("state"));
                    break;
                }
            }

        }

        private void comparison(JsonElement state) {
            // сравниваем время если новое, то
            // получаем занчения и сравниваем с предыдущими (рассмотреть случай, когда открываем приложение первый раз)
            // потом, если есть изменение и мы перешли крит значения, то отсылаем уведомление
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();

            String thisEmulsioncalc = preferences.getString("notificationEmulsioncalc", null);
            String thisLevel = preferences.getString("notificationLevel", null);
            String thisPh = preferences.getString("notificationPh", null);
            String thisTemp_ph = preferences.getString("notificationTemp_ph", null);

            String newEmulsioncalc = state.getAsJsonObject().get("emulsioncalc").getAsString();
            String newLevel = state.getAsJsonObject().get("level").getAsString();
            String newPh = state.getAsJsonObject().get("ph").getAsString();
            String newTemp_ph = state.getAsJsonObject().get("temp_ph").getAsString();

            if (thisEmulsioncalc == null || thisLevel == null || thisPh == null || thisTemp_ph == null) {
                // первый запуск
                editor.putString("notificationEmulsioncalc", newEmulsioncalc);
                editor.putString("notificationLevel", newLevel);
                editor.putString("notificationPh", newPh);
                editor.putString("notificationTemp_ph", newTemp_ph);
                try {
                    if (Double.parseDouble(newEmulsioncalc) > Double.parseDouble(preferences.getString("EmulsioncalcMax", "10000"))
                            || Double.parseDouble(newEmulsioncalc) < Double.parseDouble(preferences.getString("EmulsioncalcMin", "0"))) {
                        // красный emulsionCalc
                        sendNotif("Важно", "Уровень концентрации эмульсии критический велик: " + newEmulsioncalc);
                        editor.putString("notificationEmulsioncalc", newEmulsioncalc);
                        editor.putString("notificationEmulsioncalcTime", setInitialDateTime());
                        editor.putString("showEmulsioncalc", "true");
                    } else if ((Double.parseDouble(newEmulsioncalc) > Double.parseDouble(preferences.getString("EmulsioncalcHigh", "10000")))
                            || (Double.parseDouble(newEmulsioncalc) < Double.parseDouble(preferences.getString("EmulsioncalcLow", "0")))) {
                        // желтый emulsionCalc
                        sendNotif("Информация", "Уровень концентрации эмульсии велик: " + newEmulsioncalc);
                        editor.putString("notificationEmulsioncalc", newEmulsioncalc);
                        editor.putString("notificationEmulsioncalcTime", setInitialDateTime());
                        editor.putString("showEmulsioncalc", "true");
                    } else {
                        editor.putString("showEmulsioncalc", "false");
                        editor.putString("notificationEmulsioncalc", newEmulsioncalc);
                    }
                } catch (Exception e){
                    Log.i("error", e + "");
                }

                try {
                    if (Double.parseDouble(newLevel) > Double.parseDouble(preferences.getString("LevelMax", "10000"))
                            || Double.parseDouble(newLevel) < Double.parseDouble(preferences.getString("LevelMin", "0"))) {
                        // красный level
                        sendNotif("Важно", "Уровень СОЖ критический велик: " + newLevel);
                        editor.putString("notificationLevelTime", setInitialDateTime());
                        editor.putString("showLevel", "true");
                        editor.putString("notificationLevel", newLevel);
                    } else if ((Double.parseDouble(newLevel) > Double.parseDouble(preferences.getString("LevelHigh", "10000")))
                            || (Double.parseDouble(newLevel) < Double.parseDouble(preferences.getString("LevelLow", "0")))) {
                        // желтый level
                        sendNotif("Информация", "Уровень СОЖ велик: " + newLevel);
                        editor.putString("notificationLevelTime", setInitialDateTime());
                        editor.putString("notificationLevel", newLevel);
                        editor.putString("showLevel", "true");
                    } else {
                        editor.putString("notificationLevel", newLevel);
                        editor.putString("showLevel", "false");
                    }
                } catch (Exception e){
                    Log.i("error", e + "");
                }

                try {
                    if (Double.parseDouble(newPh) > Double.parseDouble(preferences.getString("phMax", "10000"))
                            || Double.parseDouble(newPh) < Double.parseDouble(preferences.getString("phMin", "0"))) {
                        // красный ph
                        sendNotif("Важно", "Уровень ph критический велик: " + newPh);
                        editor.putString("notificationPh", newPh);
                        editor.putString("showPh", "true");
                        editor.putString("notificationPhTime", setInitialDateTime());
                    } else if ((Double.parseDouble(newPh) > Double.parseDouble(preferences.getString("phHigh", "10000")))
                            || (Double.parseDouble(newPh) < Double.parseDouble(preferences.getString("phLow", "0")))) {
                        // желтый ph
                        sendNotif("Информация", "Уровень ph велик: " + newPh);
                        editor.putString("notificationPh", newPh);
                        editor.putString("notificationPhTime", setInitialDateTime());
                        editor.putString("showPh", "true");
                    } else {
                        editor.putString("notificationPh", newPh);
                        editor.putString("showPh", "false");
                    }
                } catch (Exception e){
                    Log.i("error", e + "");
                }

                try {
                    if (Double.parseDouble(newTemp_ph) > Double.parseDouble(preferences.getString("temp_phMax", "10000"))
                            || Double.parseDouble(newTemp_ph) < Double.parseDouble(preferences.getString("temp_phMin", "0"))) {
                        // красный temp_ph
                        sendNotif("Важно", "Уровень температуры по данным pH-метра (в градусах по цельсию) критический велик: " + newTemp_ph);
                        editor.putString("notificationTemp_ph", newTemp_ph);
                        editor.putString("notificationTemp_phTime", setInitialDateTime());
                        editor.putString("showTemp_ph", "true");
                    } else if ((Double.parseDouble(newTemp_ph) > Double.parseDouble(preferences.getString("temp_phHigh", "10000")))
                            || (Double.parseDouble(newTemp_ph) < Double.parseDouble(preferences.getString("temp_phLow", "0")))) {
                        // желтый temp_ph
                        sendNotif("Информация", "Уровень температуры по данным pH-метра (в градусах по цельсию) велик: " + newTemp_ph);
                        editor.putString("notificationTemp_ph", newTemp_ph);
                        editor.putString("notificationTemp_phTime", setInitialDateTime());
                        editor.putString("showTemp_ph", "true");
                    } else {
                        editor.putString("notificationTemp_ph", newTemp_ph);
                        editor.putString("notificationTemp_phTime", newTemp_ph);
                        editor.putString("showTemp_ph", "false");
                    }
                } catch (Exception e){
                    Log.i("error", e + "");
                }


            } else {

                // не первый запуск
                try {
                    if (!newEmulsioncalc.equals(thisEmulsioncalc)) {
                        if (Double.parseDouble(newEmulsioncalc) > Double.parseDouble(preferences.getString("EmulsioncalcMax", "10000"))
                                || Double.parseDouble(newEmulsioncalc) < Double.parseDouble(preferences.getString("EmulsioncalcMin", "0"))) {
                            // красный emulsionCalc

                            if (getNumber(1, thisEmulsioncalc, newEmulsioncalc, preferences.getString("EmulsioncalcMax", "10000"),
                                    preferences.getString("EmulsioncalcHigh", "10000"), preferences.getString("EmulsioncalcLow", "0"),
                                    preferences.getString("EmulsioncalcMin", "0"))) {

                                sendNotif("Важно", "Уровень концентрации эмульсии критический велик: " + newEmulsioncalc);
                                editor.putString("notificationEmulsioncalc", newEmulsioncalc);
                                editor.putString("showEmulsioncalc", "true");
                                editor.putString("notificationEmulsioncalcTime", setInitialDateTime());
                            }
                        } else if ((Double.parseDouble(newEmulsioncalc) > Double.parseDouble(preferences.getString("EmulsioncalcHigh", "10000")))
                                || (Double.parseDouble(newEmulsioncalc) < Double.parseDouble(preferences.getString("EmulsioncalcLow", "0")))) {
                            // желтый emulsionCalc

                            if (getNumber(1, thisEmulsioncalc, newEmulsioncalc, preferences.getString("EmulsioncalcMax", "10000"),
                                    preferences.getString("EmulsioncalcHigh", "10000"), preferences.getString("EmulsioncalcLow", "0"),
                                    preferences.getString("EmulsioncalcMin", "0"))) {

                                sendNotif("Информация", "Уровень концентрации эмульсии велик: " + newEmulsioncalc);
                                editor.putString("notificationEmulsioncalc", newEmulsioncalc);
                                editor.putString("showEmulsioncalc", "true");
                                editor.putString("notificationEmulsioncalcTime", setInitialDateTime());
                            }
                        } else {
                            editor.putString("notificationEmulsioncalc", newEmulsioncalc);
                            editor.putString("showEmulsioncalc", "false");
                        }
                    }
                } catch (Exception e){
                    Log.i("error", e + "");
                }

                try {
                    if (!newLevel.equals(thisLevel)) {
                        if (Double.parseDouble(newLevel) > Double.parseDouble(preferences.getString("LevelMax", "10000"))
                                || Double.parseDouble(newLevel) < Double.parseDouble(preferences.getString("LevelMin", "0"))) {
                            // красный level
                            if (getNumber(1, thisLevel, newLevel, preferences.getString("LevelMax", "10000"),
                                    preferences.getString("LevelHigh", "10000"), preferences.getString("LevelLow", "0"),
                                    preferences.getString("LevelMin", "0"))) {

                                sendNotif("Важно", "Уровень СОЖ критический велик: " + newLevel);
                                editor.putString("notificationLevelTime", setInitialDateTime());
                                editor.putString("showLevel", "true");
                                editor.putString("notificationLevel", newLevel);
                            }
                        } else if ((Double.parseDouble(newLevel) > Double.parseDouble(preferences.getString("LevelHigh", "10000")))
                                || (Double.parseDouble(newLevel) < Double.parseDouble(preferences.getString("LevelLow", "0")))) {
                            // желтый level
                            if (getNumber(2, thisLevel, newLevel, preferences.getString("LevelMax", "10000"),
                                    preferences.getString("LevelHigh", "10000"), preferences.getString("LevelLow", "0"),
                                    preferences.getString("LevelMin", "0"))) {
                                sendNotif("Информация", "Уровень СОЖ велик: " + newLevel);
                                editor.putString("notificationLevelTime", setInitialDateTime());
                                editor.putString("showLevel", "true");
                                editor.putString("notificationLevel", newLevel);
                            }
                        } else {
                            editor.putString("notificationLevel", newLevel);
                            editor.putString("showLevel", "false");
                        }
                    }
                } catch (Exception e){
                    Log.i("error", e + "");
                }

                try {
                    if (!newPh.equals(thisPh)) {
                        if (Double.parseDouble(newPh) > Double.parseDouble(preferences.getString("phMax", "10000"))
                                || Double.parseDouble(newPh) < Double.parseDouble(preferences.getString("phMin", "0"))) {
                            // красный ph
                            if (getNumber(1, thisPh, newPh, preferences.getString("phMax", "10000"),
                                    preferences.getString("phHigh", "10000"), preferences.getString("phLow",
                                            "0"), preferences.getString("phMin", "0"))) {

                                sendNotif("Важно", "Уровень ph критический велик: " + newPh);
                                editor.putString("notificationPh", newPh);
                                editor.putString("showPh", "true");
                                editor.putString("notificationPhTime", setInitialDateTime());
                            }
                        } else if ((Double.parseDouble(newPh) > Double.parseDouble(preferences.getString("phHigh", "10000")))
                                || (Double.parseDouble(newPh) < Double.parseDouble(preferences.getString("phLow", "0")))) {
                            // желтый ph
                            if (getNumber(2, thisPh, newPh, preferences.getString("phMax", "10000"),
                                    preferences.getString("phHigh", "10000"), preferences.getString("phLow",
                                            "0"), preferences.getString("phMin", "0"))) {

                                editor.putString("showPh", "true");
                                sendNotif("Информация", "Уровень ph велик: " + newPh);
                                editor.putString("notificationPh", newPh);
                                editor.putString("notificationPhTime", setInitialDateTime());
                            }
                        } else {
                            editor.putString("notificationPh", newPh);
                            editor.putString("showPh", "false");
                        }
                    }
                } catch (Exception e){
                    Log.i("error", e + "");
                }

                try {
                    if (!newTemp_ph.equals(thisTemp_ph)) {
                        if (Double.parseDouble(newTemp_ph) > Double.parseDouble(preferences.getString("temp_phMax", "10000"))
                                || Double.parseDouble(newTemp_ph) < Double.parseDouble(preferences.getString("temp_phMin", "0"))) {
                            // красный temp_ph
                            if (getNumber(1, thisTemp_ph, newTemp_ph, preferences.getString("temp_phMax", "10000"),
                                    preferences.getString("temp_phHigh", "10000"), preferences.getString("temp_phLow", "0"),
                                    preferences.getString("temp_phMin", "0"))) {

                                sendNotif("Важно", "Уровень температуры по данным pH-метра (в градусах по цельсию) критический велик: " + newTemp_ph);
                                editor.putString("notificationTemp_ph", newTemp_ph);
                                editor.putString("notificationTemp_phTime", setInitialDateTime());
                                editor.putString("showTemp_ph", "true");
                            }
                        } else if ((Double.parseDouble(newTemp_ph) > Double.parseDouble(preferences.getString("temp_phHigh", "10000")))
                                || (Double.parseDouble(newTemp_ph) < Double.parseDouble(preferences.getString("temp_phLow", "0")))) {
                            // желтый temp_ph
                            if (getNumber(2, thisTemp_ph, newTemp_ph, preferences.getString("temp_phMax", "10000"),
                                    preferences.getString("temp_phHigh", "10000"), preferences.getString("temp_phLow", "0"),
                                    preferences.getString("temp_phMin", "0"))) {

                                sendNotif("Информация", "Уровень температуры по данным pH-метра (в градусах по цельсию) велик: " + newTemp_ph);
                                editor.putString("notificationTemp_ph", newTemp_ph);
                                editor.putString("notificationTemp_phTime", setInitialDateTime());
                                editor.putString("showTemp_ph", "true");
                            }
                        } else {
                            editor.putString("notificationTemp_ph", newTemp_ph);
                            editor.putString("showTemp_ph", "false");
                        }
                    }
                } catch (Exception e){
                    Log.i("error", e + "");
                }

            }
            editor.apply();

        }

        private boolean getNumber(int a, String value, String newValue, String maxValue, String highValue, String lowValue, String minValue){
            /**
             * a = 1 - красн
             * а = 2 - желт
             *
             * 0 - обычное
             * 1 - критически важно сверху
             * 2 - важно сверху
             * 3 - важно снизу
             * 4 - критически важно снизу
             */
            boolean result = false;
            int resOld = 0;
            int resNew = 0;
            try {
                if (Double.parseDouble(value) > Double.parseDouble(maxValue)) {
                    resOld = 1;
                } else if (Double.parseDouble(value) < Double.parseDouble(minValue)) {
                    resOld = 4;
                } else if (Double.parseDouble(value) < Double.parseDouble(maxValue) && Double.parseDouble(value) > Double.parseDouble(highValue)) {
                    resOld = 2;
                } else if (Double.parseDouble(value) > Double.parseDouble(minValue) && Double.parseDouble(value) < Double.parseDouble(lowValue)) {
                    resOld = 3;
                }

                if (Double.parseDouble(newValue) > Double.parseDouble(maxValue)) {
                    resNew = 1;
                } else if (Double.parseDouble(newValue) < Double.parseDouble(minValue)) {
                    resNew = 4;
                } else if (Double.parseDouble(newValue) < Double.parseDouble(maxValue) && Double.parseDouble(value) > Double.parseDouble(highValue)) {
                    resNew = 2;
                } else if (Double.parseDouble(newValue) > Double.parseDouble(minValue) && Double.parseDouble(value) < Double.parseDouble(lowValue)) {
                    resNew = 3;
                }

                if (resOld == 0 && (resNew == 1 || resNew == 4) && a == 1) {
                    result = true;
                } else if (resOld == 0 && (resNew == 2 || resNew == 3) && a == 2) {
                    result = true;
                } else if (resOld == 4 && resNew == 1 && a == 1) {
                    result = true;
                } else if (resOld == 1 && resNew == 4 && a == 1) {
                    result = true;
                } else if ((resOld == 2 || resOld == 3) && (resNew == 1 || resNew == 4) && a == 1) {
                    result = true;
                } else if (resOld == 2 && resNew == 3 && a == 2) {
                    result = true;
                } else if (resOld == 3 && resNew == 2 && a == 2) {
                    result = true;
                }


                if (resNew == 0) {
                    result = false;
                }
            } catch (Exception e){
                Log.i("error", e + "");
            }
            return result;
        }

        private String setInitialDateTime() {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            return df.format(calendar.getTime());
        }

        private void sendNotif(String notifTitle, String notifMess) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            int notifyID = preferences.getInt("IdNotif", 0);

            SharedPreferences.Editor editor = preferences.edit();
            if (notifyID < 30) {
                editor.putInt("IdNotif", notifyID + 1);
                editor.apply();
            } else {
                editor.putInt("IdNotif", 0);
                editor.apply();
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

                Intent notificationIntent = new Intent(context, LoginActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(context,
                        0, notificationIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);

                Resources res = context.getResources();

                // до версии Android 8.0 API 26
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

                builder.setContentIntent(contentIntent)
                        // обязательные настройки
                        .setSmallIcon(R.drawable.ic_logo)
                        .setContentTitle(notifTitle)// Заголовок уведомления
                        .setContentText(notifMess) // Текст уведомления
                        // необязательные настройки
                        .setAutoCancel(true); // автоматически закрыть уведомление после нажатия

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, builder.build());
            } else {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra(MainActivity.ACTIVITY_SERVICE, "MainActivity");
                PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

                String CHANNEL_ID = "my_channel_01";// The id of the channel.
                NotificationChannel channel = new NotificationChannel("my_channel_01", "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                Notification notification = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_logo)
                        .setContentTitle(notifTitle)
                        .setContentText(notifMess)
                        .setContentIntent(pIntent)
                        .setChannelId(CHANNEL_ID).build();
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (notifyID == 0) {
                    mNotificationManager.cancelAll();
                }
                mNotificationManager.createNotificationChannel(channel);
                mNotificationManager.notify(notifyID, notification);
            }
        }
    }

    NotificationManager nm;


    public NotificationsService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new MyTimer(getApplicationContext()), 0, 3, TimeUnit.MINUTES);

        return super.onStartCommand(intent, flags, startId);
    }


    public IBinder onBind(Intent arg0) {
        return null;
    }
}
