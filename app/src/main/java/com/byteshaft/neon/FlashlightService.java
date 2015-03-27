/*
 *
 *  * (C) Copyright 2015 byteShaft Inc.
 *  *
 *  * All rights reserved. This program and the accompanying materials
 *  * are made available under the terms of the GNU Lesser General Public License
 *  * (LGPL) version 2.1 which accompanies this distribution, and is available at
 *  * http://www.gnu.org/licenses/lgpl-2.1.html
 *  *
 *  * This library is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  * Lesser General Public License for more details.
 *  
 */

package com.byteshaft.neon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.byteshaft.ezflashlight.CameraStateChangeListener;
import com.byteshaft.ezflashlight.Flashlight;
import com.byteshaft.ezflashlight.FlashlightGlobals;

public class FlashlightService extends Service implements CameraStateChangeListener {

    private static FlashlightService sFlashlightService = null;
    private ScreenStateListener mScreenStateListener = null;
    private Notification mNotification = null;
    private SystemManager mSystemManager = null;
    private RemoteUpdateUiHelpers mRemoteUi = null;
    private com.byteshaft.ezflashlight.Flashlight mFlashlight = null;
    private boolean AUTOSTART = true;

    static FlashlightService getInstance() {
        return sFlashlightService;
    }

    static boolean isRunning() {
        return sFlashlightService != null;
    }

    private static void setServiceInstance(FlashlightService flashlightService) {
        sFlashlightService = flashlightService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setServiceInstance(this);
        AppGlobals.setIsServiceSwitchInProgress(true);
        mScreenStateListener = new ScreenStateListener(this);
        mRemoteUi = new RemoteUpdateUiHelpers(this);
        mSystemManager = new SystemManager(this);
        mNotification = new Notification(this);
        mFlashlight = new Flashlight(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String starter = intent.getStringExtra("STARTER");
        Log.i(AppGlobals.LOG_TAG, String.format("Service started from %s", starter));
        AUTOSTART = intent.getBooleanExtra("AUTOSTART", true);
        mFlashlight.setOnCameraStateChangedListener(this);
        mFlashlight.initializeCamera();
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (!AppGlobals.isWidgetTapped()) {
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRemoteUi.setUiButtonsOn(false);
        if (FlashlightGlobals.isFlashlightOn()) {
            stopTorch();
        }
        mScreenStateListener.unregister();
        mSystemManager.releaseWakeLock();
        mFlashlight.releaseAllResources();
        AppGlobals.setIsServiceSwitchInProgress(false);
        setServiceInstance(null);
        Log.i(AppGlobals.LOG_TAG, "Service down.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    synchronized void lightenTorch() {
        Log.i(AppGlobals.LOG_TAG, "Turning on");
        /* Make is foreground service by showing a Notification,
        this ensures the service does not get killed on low resources.
        Unless the situation is really really bad.
        786 is just a random ID for the notification.
        */
        startForeground(786, mNotification.getNotification());
        mFlashlight.turnOn();
    }

    synchronized void stopTorch() {
        Log.i(AppGlobals.LOG_TAG, "Turning off");
        stopForeground(true);
        mFlashlight.turnOff();
    }

    @Override
    public void onCameraOpened() {

    }

    @Override
    public void onCameraViewSetup() {
        if (AUTOSTART) {
            lightenTorch();
        } else {
            mRemoteUi.setUiButtonsOn(false);
        }
        mScreenStateListener.register();
        mSystemManager.setWakeLock();
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

    @Override
    public void onFlashlightTurnedOn() {
        mRemoteUi.setUiButtonsOn(true);
        AppGlobals.setIsServiceSwitchInProgress(false);
    }

    @Override
    public void onFlashlightTurnedOff() {
        mRemoteUi.setUiButtonsOn(false);
        AppGlobals.setIsWidgetTapped(false);
    }
}
