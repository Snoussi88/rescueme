package com.snoussi.univox;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class NotifHelper extends Application {
    public static final String Channel_1_ID = "channel1";
    public static final String Channel_2_ID = "channel2";


    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                    Channel_1_ID,"channel 1", NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("this is my first notification");


            NotificationChannel channel2 = new NotificationChannel(
                    Channel_2_ID,"channel 2", NotificationManager.IMPORTANCE_HIGH
            );
            channel2.setDescription("this is my 2nd notification");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);



        }

    }


}
