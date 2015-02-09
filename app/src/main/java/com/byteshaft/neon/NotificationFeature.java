package com.byteshaft.neon;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by husnain on 2/7/15.
 */
public class NotificationFeature {

    int notificationId = 446;
    public NotificationCompat.Builder builder;
    public NotificationManager notificationManager;
    public PendingIntent contentIntent;
    Activity context;

    public NotificationFeature(Activity context) {
        this.context = context;
    }


    public void notifyMe() {
        builder = initializationNotification(context);
        clickOnNotificationToOpenApp(context);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }

    @TargetApi(16)
    private void clickOnNotificationToOpenApp(Context view) {
        Intent intent = new Intent(String.valueOf(this));
        PendingIntent pIntent = PendingIntent.getActivity(view, 0, intent, 0);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(view);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

    }

    private NotificationCompat.Builder initializationNotification(Context v) {
         builder = new NotificationCompat.Builder(v);
        builder.setContentTitle("Yes");
        builder.setContentText("Hello this is notification");
        builder.setTicker("Notification");
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.light);
        return builder;
    }


}
