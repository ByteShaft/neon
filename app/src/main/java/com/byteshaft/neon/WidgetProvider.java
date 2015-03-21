package com.byteshaft.neon;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.byteshaft.ezflashlight.FlashlightGlobals;

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteUpdateUiHelpers remoteUi = new RemoteUpdateUiHelpers(context);
        if (FlashlightGlobals.isFlashlightOn()) {
            remoteUi.setUiButtonsOn(true);
        } else {
            remoteUi.setUiButtonsOn(false);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        if (FlashlightGlobals.isFlashlightOn() && AppGlobals.isWidgetTapped()) {
            Intent serviceIntent = new Intent(context, FlashlightService.class);
            context.stopService(serviceIntent);
        }
    }
}
