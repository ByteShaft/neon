package com.byteshaft.neon;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class Notification extends ContextWrapper {

    private final int ID = 1;
    private NotificationCompat.Builder mNotificationBuilder = null;
    private NotificationManager mNotificationManager = null;

    public Notification(Context context) {
        super(context);
    }

    void show() {
        buildNotification();
        setOnTapIntentAction();
        showNotification();
    }

    void dismiss() {
        if (mNotificationManager != null) {
            mNotificationManager.cancel(ID);
        }
    }

    private void buildNotification() {
        mNotificationBuilder = new NotificationCompat.Builder(this);

        mNotificationBuilder.setContentTitle(getString(R.string.app_name));
        mNotificationBuilder.setContentText(getString(R.string.notification_message));
        // dismiss notification when its tapped.
        mNotificationBuilder.setAutoCancel(true);
        mNotificationBuilder.setSmallIcon(R.drawable.ic_notify);
        // disable slide to remove for the notification.
        mNotificationBuilder.setOngoing(true);
    }

    private void setOnTapIntentAction() {
        Intent intent = new Intent("com.byteshaft.neon.CLOSE_ACTIVITY");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        mNotificationBuilder.setContentIntent(pendingIntent);
    }

    private void showNotification() {
        mNotificationManager = getNotificationManager();
        mNotificationManager.notify(ID, mNotificationBuilder.build());
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
