package com.byteshaft.neon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

public class CustomBroadcastReceivers {

    private Context mContext;

    Notification mNotificationReceiver;
    ScreenStateListener mScreenStateListener;


    public CustomBroadcastReceivers(Context context) {
        mContext = context;
        initializeClasses();
    }

    public void registerReceivers() {
        mNotificationReceiver.register();
        mScreenStateListener.register();
    }

    public void unregisterReceivers() {
        mNotificationReceiver.unregister();
        mScreenStateListener.unregister();
    }

    private void initializeClasses() {
        mNotificationReceiver = new Notification(mContext);
        mScreenStateListener = new ScreenStateListener(mContext);
    }
}


class Notification {

    private BroadcastReceiver mNotificationReceiver;
    private Context mContext;
    private RemoteUpdateUiHelpers mRemoteUi;

    public Notification(Context context) {
        mContext = context;
        mRemoteUi = new RemoteUpdateUiHelpers(mContext);
    }

    public void register() {
        initialize();
        IntentFilter filter = new IntentFilter("android.intent.CLOSE_ACTIVITY");
        mContext.registerReceiver(mNotificationReceiver, filter);
    }

    public void unregister() {
        try {
            mContext.unregisterReceiver(mNotificationReceiver);
        } catch (IllegalArgumentException e) {
            Log.w(AppGlobals.LOG_TAG, "Receiver not registered.");
        }
    }

    private void initialize() {
        mNotificationReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                mContext.stopService(new Intent(mContext, FlashlightService.class));
                MainActivity.stopApp();
                mRemoteUi.setUiButtonsOn(false);
            }
        };
    }
}


class ScreenStateListener {

    BroadcastReceiver mScreenStateReceiver;
    Context mContext;

    public ScreenStateListener(Context context) {
        mContext = context;
    }

    public void register() {
        initialize();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        mContext.registerReceiver(mScreenStateReceiver, intentFilter);
    }

    public void unregister() {
        try {
            mContext.unregisterReceiver(mScreenStateReceiver);
        } catch (IllegalArgumentException e) {
            Log.w(AppGlobals.LOG_TAG, "Screen listener not registered.");
        }
    }

    private void initialize() {
        mScreenStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) && Build.MANUFACTURER
                        .equals("HTC")) {
                    restartTorch();
                }
            }
        };
    }

    private void restartTorch() {
        FlashlightService flashlightService = FlashlightService.getInstance();
        if (flashlightService != null) {
            flashlightService.stopTorch();
            flashlightService.lightenTorch();
        }
    }
}
