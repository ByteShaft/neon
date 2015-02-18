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
            return;
        }

        Intent serviceIntent = new Intent(context, FlashlightService.class);
        Flashlight.isWidgetContext = true;

        if (Flashlight.isOn()) {
            Log.i(Flashlight.LOG_TAG, "turn off code.");
            context.stopService(serviceIntent);
            Flashlight.setInUseByWidget(false);
        } else {
            Log.i(Flashlight.LOG_TAG, "turn on code.");
//            if (Helpers.isCameraInUse() && !Flashlight.isOn()) {
//                Toast.makeText(context, "Camera Resource is busy by another application.",
//                        Toast.LENGTH_SHORT).show();
//                Flashlight.setIsBusyByOtherApp(true);
//                return;
//            }
            if (!FlashlightService.isRunning()) {
                Log.i(Flashlight.LOG_TAG, "Starting service from the widget");
                serviceIntent.putExtra("command", "turnOn");
                context.startService(serviceIntent);
            }
            Flashlight.setInUseByWidget(true);
        }
    }
}
