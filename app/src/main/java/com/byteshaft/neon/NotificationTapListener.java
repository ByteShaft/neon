package com.byteshaft.neon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationTapListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteUpdateUiHelpers remoteUi = new RemoteUpdateUiHelpers(context);
        Intent serviceIntent = new Intent(context, FlashlightService.class);
        remoteUi.setUiButtonsOn(false);
        context.stopService(serviceIntent);
        MainActivity.stopApp();
    }
}
