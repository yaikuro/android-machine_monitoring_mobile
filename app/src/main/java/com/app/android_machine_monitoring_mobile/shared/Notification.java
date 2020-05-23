package com.app.android_machine_monitoring_mobile.shared;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class Notification extends Application {
    public static final String CHANNEL_1_ID = "Breakdown Notification";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        NotificationChannel breakdownChannel = new NotificationChannel(
                CHANNEL_1_ID,
                "Breakdown Notification",
                NotificationManager.IMPORTANCE_HIGH
        );
        breakdownChannel.setDescription("Sends a notification whenever a breakdown occurs");

        NotificationManager manager = getSystemService(NotificationManager.class);
        assert manager != null;
        manager.createNotificationChannel(breakdownChannel);
    }
}
