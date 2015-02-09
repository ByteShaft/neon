package nonameyetsoft.com.torch;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;


public class Notifications {

    private Activity context;
    private final int ID = 001;

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager manager;

    public Notifications(Activity context) {
        this.context = context;
    }

    public void startNotification() {
        buildNotification();
        addPendingNotify();
        showNotification();
    }

    private void buildNotification() {
        notificationBuilder = new NotificationCompat.Builder(context);

        notificationBuilder.setContentTitle("Neon");
        notificationBuilder.setContentText("Tap to turn off.");
        notificationBuilder.setTicker("Flashlight on");
        // dismiss notification when its tapped.
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.drawable.ic_stat);
        // disable slide to remove for the notification.
        notificationBuilder.setOngoing(true);
    }

    private void addPendingNotify() {
        Intent intent = new Intent("android.intent.CLOSE_ACTIVITY");
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0 , intent, 0);
        notificationBuilder.setContentIntent(pIntent);
    }

    private void showNotification() {
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(ID, notificationBuilder.build());
    }
}
