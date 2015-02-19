package com.byteshaft.neon;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class FlashlightService extends Service implements SurfaceHolder.Callback {

    private static FlashlightService instance = null;

    private Camera mCamera = null;
    private CustomBroadcastReceivers mCustomReceivers = null;
    private Notifications mNotifications = null;
    private SurfaceHolder mHolder = null;
    private SurfaceView mPreview = null;
    private SystemManager mSystemManager = null;
    private WindowManager mWindowManager = null;

    private RemoteUpdateUiHelpers mRemoteUi;

    public static FlashlightService getInstance() {
        return instance;
    }

    /* Start Override methods of the Service class. */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(Flashlight.LOG_TAG, "Service started.");
        instance = this;
        Flashlight.setToggleInProgress(true);
        mRemoteUi = new RemoteUpdateUiHelpers(this);
        mCustomReceivers = new CustomBroadcastReceivers(this);
        mSystemManager = new SystemManager(this);
        mNotifications = new Notifications(this);
        lightenTorch();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
        if (Flashlight.isOn()) {
            stopTorch();
        }
        destroyCamera();
        removeSurfaceView();
        if (mNotifications != null) {
            mNotifications.endNotification();
        }
        mCustomReceivers.unregisterReceivers();
        Flashlight.setToggleInProgress(false);
        Flashlight.setInUseByWidget(false);
        Log.i(Flashlight.LOG_TAG, "Service down.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    /* End Override methods of the Service class. */

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera == null) {
            openCamera();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera != null) {
            mRemoteUi.setUiButtonsOn(true);
            setCameraPreview();
            setCameraModeTorch(true);
            mCamera.startPreview();
            Flashlight.setIsOn(true);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void lightenTorch() {
        mPreview = new SurfaceView(instance);
        mHolder = mPreview.getHolder();
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.addCallback(this);

        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(1, 1,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.UNKNOWN);
        mWindowManager.addView(mPreview, params);

        mSystemManager.setWakeLock();
        mCustomReceivers.registerReceivers();
        mNotifications.startNotification();
        Flashlight.setToggleInProgress(false);
    }

    public void stopTorch() {
        setCameraModeTorch(false);
        mCamera.stopPreview();
        Flashlight.setIsOn(false);
        if (mSystemManager != null) {
            mSystemManager.releaseWakeLock();
        }
        mCustomReceivers.unregisterReceivers();
        mNotifications.endNotification();
        Flashlight.setToggleInProgress(false);
        mRemoteUi.setUiButtonsOn(false);
    }

    private void openCamera() {
        try {
            mCamera = Camera.open();
        } catch (RuntimeException e) {
            Log.w(Flashlight.LOG_TAG,
                "Failed to open camera, probably in use by another App.");
            if (!Flashlight.isBusyByWidget() && !Flashlight.isRunningFromWidget()) {
                Helpers.showFlashlightBusyDialog(MainActivity.getContext());
            } else if (Flashlight.isRunningFromWidget()) {
                Intent i = new Intent(Flashlight.TOAST_INTENT);
                sendBroadcast(i);
                mRemoteUi.setUiButtonsOn(false);
            }
            // If we fail to open camera, make sure to kill the newly started
            // service, as it is of no use.
            stopService(new Intent(this, FlashlightService.class));
        }
    }

    private void destroyCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private void setCameraPreview() {
        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCameraModeTorch(boolean ON) {
        Camera.Parameters mParams = mCamera.getParameters();
        if (ON) {
            mParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(mParams);
        } else {
            mParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(mParams);
        }
    }

    private void removeSurfaceView() {
        if (mWindowManager != null) {
            mWindowManager.removeView(mPreview);
        }
    }

}
