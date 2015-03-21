package com.byteshaft.neon;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class Notification extends ContextWrapper {

    public Notification(Context context) {
        super(context);
    }

    android.app.Notification getNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        setupVisuals(builder);
        setOnTapIntentAction(builder);
        return builder.build();
    }

    private void setupVisuals(NotificationCompat.Builder builder) {
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(getString(R.string.notification_message));
        // dismiss notification when its tapped.
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_notify);
        // disable slide to remove for the notification.
        builder.setOngoing(true);
    }

    private void setOnTapIntentAction(NotificationCompat.Builder builder) {
        Intent intent = new Intent("com.byteshaft.neon.CLOSE_ACTIVITY");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
    }
}
