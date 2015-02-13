package nonameyetsoft.com.torch;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

public class Flashlight {

    public static boolean isBusy = false;
    private boolean isRunning = false;
    private Activity context;
    private Camera camera;
    private Camera.Parameters params;
    private PowerManager.WakeLock mWakeLock;

    public Flashlight(Activity context, Camera camera, Camera.Parameters params) {
        this.context = context;
        this.camera = camera;
        this.params = params;
    }

    public static boolean isAvailable(Context context) {
        boolean availability;
        PackageManager packageManager = context.getPackageManager();
        availability = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        return availability;
    }

    public boolean isOn() {
        return isRunning;
    }

    public void turnOn() {
        Log.i("NEON", String.format("Device is %s", Build.DEVICE));
        camera.startPreview();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        isRunning = true;

        getWakeLock();
        mWakeLock.acquire();
    }

    public void turnOff() {
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();
        releaseWakeLock();

        isRunning = false;
    }

    private void getWakeLock() {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "NEON");
    }

    private void releaseWakeLock() {
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
    }
}
