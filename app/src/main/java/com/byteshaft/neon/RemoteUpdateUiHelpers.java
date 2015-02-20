package com.byteshaft.neon;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
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
        RemoteViews views = WidgetProvider.mRemoteViews;
        if (views == null) {
            return;
        }

        if (ON) {
            views.setImageViewResource(R.id.NeonWidget, R.drawable.button_widget_off);
        } else {
            views.setImageViewResource(R.id.NeonWidget, R.drawable.button_widget_on);
        }

        appWidgetManager.updateAppWidget(new ComponentName(mContext, WidgetProvider.class),
                views);
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
