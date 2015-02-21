package com.byteshaft.neon;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;


public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        RemoteUpdateUiHelpers mRemoteUi = new RemoteUpdateUiHelpers(context);
        if (Flashlight.isOn()) {
            mRemoteUi.setUiButtonsOn(true);
        } else {
            mRemoteUi.setUiButtonsOn(false);
        }
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
