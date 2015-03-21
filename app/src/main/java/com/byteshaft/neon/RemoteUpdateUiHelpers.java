package com.byteshaft.neon;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.widget.Button;
import android.widget.RemoteViews;

public class RemoteUpdateUiHelpers extends ContextWrapper {

    public RemoteUpdateUiHelpers(Context context) {
        super(context);
    }

    void setUiButtonsOn(boolean ON) {
        if (ON) {
            setMainActivitySwitchOn(true);
            setWidgetIconOn(true);
        } else {
            setMainActivitySwitchOn(false);
            setWidgetIconOn(false);
        }
    }

    private void setWidgetIconOn(boolean ON) {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        Intent receiver = new Intent(this, WidgetReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, receiver, 0);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.neon_widget);
        remoteViews.setOnClickPendingIntent(R.id.NeonWidget, pendingIntent);

        if (ON) {
            remoteViews.setImageViewResource(R.id.NeonWidget, R.drawable.button_widget_off);
        } else {
            remoteViews.setImageViewResource(R.id.NeonWidget, R.drawable.button_widget_on);
        }

        widgetManager.updateAppWidget(new ComponentName(this, WidgetProvider.class), remoteViews);
    }

    private void setMainActivitySwitchOn(boolean ON) {
        Button switcher = MainActivity.mSwitcher;
        if (switcher != null) {
            if (ON) {
                switcher.setBackgroundResource(R.drawable.button_off);
            } else {
                switcher.setBackgroundResource(R.drawable.button_on);
            }
        }
    }
}
