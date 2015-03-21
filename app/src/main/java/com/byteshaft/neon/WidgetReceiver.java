package com.byteshaft.neon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.byteshaft.ezflashlight.FlashlightGlobals;

public class WidgetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(AppGlobals.LOG_TAG, "Widget tapped.");
        AppGlobals.setIsWidgetTapped(true);
        Intent serviceIntent = new Intent(context, FlashlightService.class);
        RemoteUpdateUiHelpers remoteUi = new RemoteUpdateUiHelpers(context);

        if (FlashlightGlobals.isFlashlightOn()) {
            Log.i(AppGlobals.LOG_TAG, "Turning off from widget.");
            context.stopService(serviceIntent);
        } else {
            Log.i(AppGlobals.LOG_TAG, "Turning on from widget.");
            remoteUi.setUiButtonsOn(true);
            context.startService(serviceIntent);
        }
    }
}
