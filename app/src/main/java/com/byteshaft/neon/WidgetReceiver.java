package com.byteshaft.neon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WidgetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(Flashlight.LOG_TAG, "Widget tapped.");

        if (Flashlight.isToggleInProgress()) {
            Log.i(Flashlight.LOG_TAG, "Widget tap ignored, too fast.");
            return;
        }

        Intent serviceIntent = new Intent(context, FlashlightService.class);
        RemoteUpdateUiHelpers mRemoteUi = new RemoteUpdateUiHelpers(context);
        mRemoteUi.setUiButtonsOn(true);

        Flashlight.setIsRunningFromWidget(true);

        if (Flashlight.isOn()) {
            Log.i(Flashlight.LOG_TAG, "Turning off from widget.");
            context.stopService(serviceIntent);
            Flashlight.setBusyByWidget(false);
        } else {
            Log.i(Flashlight.LOG_TAG, "Turning on from widget.");
            context.startService(serviceIntent);
            Flashlight.setBusyByWidget(true);
        }
    }
}
