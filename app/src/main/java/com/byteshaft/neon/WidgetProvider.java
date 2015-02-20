package com.byteshaft.neon;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


public class WidgetProvider extends AppWidgetProvider {

    public static RemoteViews mRemoteViews;

    public static void setClickListenerOnWidget(Context context) {
        Intent receiver = new Intent(context, WidgetReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, receiver, 0);
        if (mRemoteViews == null) {
            mRemoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.neon_widget);
        }
        mRemoteViews.setOnClickPendingIntent(R.id.NeonWidget, pendingIntent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        RemoteUpdateUiHelpers mRemoteUi = new RemoteUpdateUiHelpers(context);
        setClickListenerOnWidget(context);

        if (Flashlight.isOn()) {
            mRemoteUi.setUiButtonsOn(true);
        }
        appWidgetManager.updateAppWidget(appWidgetIds, mRemoteViews);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        if (Flashlight.isOn() && Flashlight.isRunningFromWidget()) {
            Intent serviceIntent = new Intent(context, FlashlightService.class);
            context.stopService(serviceIntent);
        }
    }
}
