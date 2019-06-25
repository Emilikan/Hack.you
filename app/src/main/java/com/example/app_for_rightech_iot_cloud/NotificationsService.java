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
import android.widget.Toast;

public class NotificationsService extends Service {
    NotificationManager nm;
    public NotificationsService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast toast = Toast.makeText(NotificationsService.this.getApplicationContext(),
                "Сервис", Toast.LENGTH_LONG);
        toast.show();
        sendNotif("Напоминание", "Покорми кота");
        return super.onStartCommand(intent, flags, startId);
    }

    void sendNotif(String notifTitle, String notifMess) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(NotificationsService.this);
        int notifyID = preferences.getInt("IdNotif", 0);

        SharedPreferences.Editor editor = preferences.edit();
        if(notifyID < 30){
            editor.putInt("IdNotif", notifyID+1);
            editor.apply();
        } else {
            editor.putInt("IdNotif", 0);
            editor.apply();
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {

            Intent notificationIntent = new Intent(this, NotificationsService.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this,
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            Resources res = this.getResources();

            // до версии Android 8.0 API 26
            NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationsService.this);

            builder.setContentIntent(contentIntent)
                    // обязательные настройки
                    .setSmallIcon(R.drawable.ic_logo)
                    //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                    .setContentTitle(notifTitle)
                    //.setContentText(res.getString(R.string.notifytext))
                    .setContentText(notifMess) // Текст уведомления
                    // необязательные настройки
                    .setAutoCancel(true); // автоматически закрыть уведомление после нажатия

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // Альтернативный вариант
            // NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, builder.build());
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(MainActivity.ACTIVITY_SERVICE, "MainActivity");
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

            String CHANNEL_ID = "my_channel_01";// The id of the channel.
            NotificationChannel channel = new NotificationChannel("my_channel_01", "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            Notification notification =
                    new NotificationCompat.Builder(NotificationsService.this)
                            .setSmallIcon(R.drawable.ic_logo)
                            .setContentTitle(notifTitle)
                            .setContentText(notifMess)
                            .setContentIntent(pIntent)
                            .setChannelId(CHANNEL_ID).build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if(notifyID == 0){
                mNotificationManager.cancelAll();
            }
            mNotificationManager.createNotificationChannel(channel);
            mNotificationManager.notify(notifyID , notification);
        }
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }
}
