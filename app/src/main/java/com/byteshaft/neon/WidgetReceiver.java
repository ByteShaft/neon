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
        } else if (FlashlightService.isRunning()) {
            /* In case when we Neon launcher icon is tapped from home screen
            and quickly, at that time the widget icon is tapped as well, we
            come across this situation where the MainActivity shows flashlight
            icon as turned on but the flashlight is not really on. In any such
            case, just do nothing. This is a corner case and a real user may
            never have come across this.
             */
            Log.i(AppGlobals.LOG_TAG, "Service already running, will do nothing.");
        } else {
            Log.i(AppGlobals.LOG_TAG, "Turning on from widget.");
            remoteUi.setUiButtonsOn(true);
            context.startService(serviceIntent);
        }
    }
}
