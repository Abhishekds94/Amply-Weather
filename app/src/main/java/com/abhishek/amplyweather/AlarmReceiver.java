package com.abhishek.amplyweather;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        // Get id & message from intent.
        int notificationId = intent.getIntExtra("notificationId", 0);
        String CHANNEL_ID = "amplyWeather_channel01";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, "name", importance);

        String temperatureDesc = intent.getStringExtra("temperatureDesc");
        String comfort = intent.getStringExtra("comfort");

        // When notification is tapped, call MainActivity.
        Intent mainIntent = new Intent(context, DashboardActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

        NotificationManager myNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        myNotificationManager.createNotificationChannel(mChannel);

        // Prepare notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setStyle(new
                NotificationCompat.BigTextStyle().bigText("Hey, Today's weather seems to be "+temperatureDesc+" " +
                "with "+comfort+" \u00B0F. Click here to know more.")) ;
        builder.setSmallIcon(R.drawable.icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon))
                .setContentTitle("Today's Weather Details")
                .setContentText("Hey, Today's weather seems to be "+temperatureDesc+" with "+comfort+" \u00B0F. Click here to know more.")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setChannelId(CHANNEL_ID).build();

        // Notify
        myNotificationManager.notify(notificationId, builder.build());
    }
}