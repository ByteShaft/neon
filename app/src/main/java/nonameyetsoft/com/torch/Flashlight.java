package nonameyetsoft.com.torch;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;

public class Flashlight {

    // Returns true if the camera torch is being used
    // by the widget.
    public static boolean isBusyByWidget = false;
    // returns true if flashlight is being used
    // by another process.
    public static boolean isBusy = false;
    // The tag name to appear in logs.
    private final String LOG_NAME = "NEON";
    // This is a list of devices which we have tested and
    // are known to work without the use of surfaceTexture.
    private String[] whiteListedDevices = {
        "dlx", "mako", "ghost", "g2", "m0", "ms013g",
        "LT26i", "klte", "scorpion_mini", "C6602"
    };

    private static boolean isRunning = false;
    private Camera mCamera = null;
    private Camera.Parameters mParams = null;
    private Context mContext = null;
    private PowerManager.WakeLock mWakeLock = null;
    private SurfaceTexture mSurfaceTexture = null;

    public Flashlight(Context context, Camera camera, Camera.Parameters params) {
        this.mContext = context;
        this.mCamera = camera;
        this.mParams = params;
    }

    public static boolean isAvailable(Context context) {
        boolean availability;
        PackageManager packageManager = context.getPackageManager();
        availability = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        return availability;
    }

    public static boolean isOn() {
        // Returns true if flashlight is on.
        return isRunning;
    }

    public void turnOn() {
        Log.i(LOG_NAME, String.format("Device is %s", Build.DEVICE));
        // We have a list of "known-to-work" devices where we don't
        // need any videoTexture hacks.
        if (Arrays.asList(whiteListedDevices).contains(Build.DEVICE)) {
            Log.i(LOG_NAME, "Running the faster code path.");
            setCameraPreviewWithTorchOn();
        }
        // We don't "officially" support gingerbread devices but we don't
        // want them to be left off, so we implemented this code as a gamble
        // for such devices.
        else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Log.w(LOG_NAME, String.format("Running on an API level %d device, may not run",
                                          Build.VERSION.SDK_INT));
            setCameraPreviewWithTorchOn();
        // For all other devices, start videoTexture before attempting to
        // enable flash. <Known to be a bit slow>.
        } else {
            Log.i(LOG_NAME, "Running the slower code path.");
            setVideoTexture();
            setCameraPreviewWithTorchOn();
        }
        // On some devices the phone gets really slow after waking from
        // sleep, so we need to set a partial wakelock to prevent the
        // processor turning off.
        mWakeLock = getWakeLock();
        mWakeLock.acquire();
    }

    public void turnOff() {
        mParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(mParams);
        mCamera.stopPreview();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            releaseVideoTexture();
        }
        releaseWakeLock();
        isRunning = false;
    }

    private void setCameraPreviewWithTorchOn() {
        mParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(mParams);
        mCamera.startPreview();
        isRunning = true;
    }

    private PowerManager getPowerManager() {
        return (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
    }

    private PowerManager.WakeLock getWakeLock() {
        PowerManager pm = getPowerManager();
        return pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "NEON");
    }

    private void releaseWakeLock() {
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
    }

    @TargetApi(11)
    private void setVideoTexture() {
        // Flashlight does not work on many devices unless
        // surfaceTexture is set.
        mSurfaceTexture = new SurfaceTexture(0);
        try {
            mCamera.setPreviewTexture(mSurfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(14)
    private void releaseVideoTexture() {
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
    }
}
