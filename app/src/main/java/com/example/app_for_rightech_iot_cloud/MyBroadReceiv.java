package com.example.app_for_rightech_iot_cloud;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class MyBroadReceiv extends BroadcastReceiver {

    final String LOG_TAG = "myLogs";
    private static int sJobId = 1;

    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive " + intent.getAction());
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            context.startService(new Intent(context, NotificationsService.class));
        } else {
            ComponentName jobService = new ComponentName(context, NotificationsJobService.class);
            JobInfo.Builder exerciseJobBuilder = new JobInfo.Builder(sJobId++, jobService);
            exerciseJobBuilder.setMinimumLatency(TimeUnit.SECONDS.toMillis(1));
            exerciseJobBuilder.setOverrideDeadline(TimeUnit.SECONDS.toMillis(5));
            exerciseJobBuilder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
            exerciseJobBuilder.setRequiresDeviceIdle(false);
            exerciseJobBuilder.setRequiresCharging(false);
            exerciseJobBuilder.setBackoffCriteria(TimeUnit.SECONDS.toMillis(3), JobInfo.BACKOFF_POLICY_LINEAR);

            Log.i(TAG, "scheduleJob: adding job to scheduler");

            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.schedule(exerciseJobBuilder.build());
        }
    }
}

