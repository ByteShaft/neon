package com.byteshaft.neon;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.byteshaft.ezflashlight.CameraInitializationListener;
import com.byteshaft.ezflashlight.Flashlight;
import com.byteshaft.ezflashlight.FlashlightGlobals;

public class FlashlightService extends Service implements CameraInitializationListener {

    private static FlashlightService sFlashlightService = null;
    private CustomBroadcastReceivers mCustomReceivers = null;
    private Notifications mNotifications = null;
    private SystemManager mSystemManager = null;
    private RemoteUpdateUiHelpers mRemoteUi = null;
    private com.byteshaft.ezflashlight.Flashlight mFlashlight = null;
    private boolean AUTOSTART = true;

    static FlashlightService getInstance() {
        return sFlashlightService;
    }

    private static void setServiceInstance(FlashlightService flashlightService) {
        sFlashlightService = flashlightService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setServiceInstance(this);
        Log.i(AppGlobals.LOG_TAG, "Service started.");
        mRemoteUi = new RemoteUpdateUiHelpers(this);
        mCustomReceivers = new CustomBroadcastReceivers(this);
        mSystemManager = new SystemManager(this);
        mNotifications = new Notifications(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AUTOSTART = intent.getBooleanExtra("autoStart", true);
        mFlashlight = new Flashlight(this);
        mFlashlight.setOnCameraStateChangeListener(this);
        mFlashlight.initializeCamera();
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (FlashlightGlobals.isFlashlightOn()) {
            stopTorch();
        }
        mFlashlight.releaseAllResources();
        setServiceInstance(null);
        Log.i(AppGlobals.LOG_TAG, "Service down.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected synchronized void lightenTorch() {
        mRemoteUi.setUiButtonsOn(true);
        mFlashlight.turnOn();
        mCustomReceivers.registerReceivers();
        mNotifications.startNotification();
        mSystemManager.setWakeLock();
    }

    protected synchronized void stopTorch() {
        mRemoteUi.setUiButtonsOn(false);
        mFlashlight.turnOff();
        mNotifications.endNotification();
        mCustomReceivers.unregisterReceivers();
        mSystemManager.releaseWakeLock();
        AppGlobals.setIsWidgetTapped(false);
    }

    @Override
    public void onCameraOpened() {
        if (AUTOSTART) {
            mRemoteUi.setUiButtonsOn(true);
        }
    }

    @Override
    public void onCameraViewSetup() {
        if (AUTOSTART) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    lightenTorch();
                }
            });
        }
    }

    @Override
    public void onCameraBusy() {
        if (AppGlobals.isWidgetTapped()) {
            mRemoteUi.setUiButtonsOn(false);
            Helpers.showFlashlightBusyToast(getApplicationContext());
            AppGlobals.setIsWidgetTapped(false);
        } else {
            Helpers.showFlashlightBusyDialog(MainActivity.getInstance());
        }
        stopSelf();
    }
}
