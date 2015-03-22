package com.byteshaft.neon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WidgetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final String STARTER = "widget";
        AppGlobals.setIsWidgetTapped(true);
        Intent serviceIntent = new Intent(context, FlashlightService.class);
        serviceIntent.putExtra("STARTER", STARTER);
        RemoteUpdateUiHelpers remoteUi = new RemoteUpdateUiHelpers(context);

        if (FlashlightService.isRunning()) {
            remoteUi.setUiButtonsOn(false);
            context.stopService(serviceIntent);
        } else {
            remoteUi.setUiButtonsOn(true);
            context.startService(serviceIntent);
        }
    }
}
