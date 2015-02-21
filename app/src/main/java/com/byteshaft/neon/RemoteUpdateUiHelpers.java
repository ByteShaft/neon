package com.byteshaft.neon;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.RemoteViews;

public class RemoteUpdateUiHelpers {

    private Context mContext = null;

    public RemoteUpdateUiHelpers(Context context) {
        this.mContext = context;
    }

    public void setUiButtonsOn(boolean ON) {
        if (ON) {
            setWidgetIconOn(true);
            setMainActivitySwitchOn(true);
        } else {
            setWidgetIconOn(false);
            setMainActivitySwitchOn(false);
        }
    }

    private void setWidgetIconOn(boolean ON) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
        Intent receiver = new Intent(mContext, WidgetReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                mContext, 0, receiver, 0);
        RemoteViews mRemoteViews = new RemoteViews(mContext.getPackageName(),
                R.layout.neon_widget);
        mRemoteViews.setOnClickPendingIntent(R.id.NeonWidget, pendingIntent);

        if (ON) {
            mRemoteViews.setImageViewResource(R.id.NeonWidget, R.drawable.button_widget_off);
        } else {
            mRemoteViews.setImageViewResource(R.id.NeonWidget, R.drawable.button_widget_on);
        }

        appWidgetManager.updateAppWidget(new ComponentName(mContext, WidgetProvider.class),
                mRemoteViews);
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
