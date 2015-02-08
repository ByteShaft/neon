package nonameyetsoft.com.torch;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;

import java.io.IOException;
import java.util.Arrays;

public class Flashlight {

    private String[] whiteListedDevices = {"dlx", "mako", "ghost", "g2"};
    private boolean isRunning = false;
    public static boolean isBusy = false;

    private Camera camera;
    private Camera.Parameters params;
    private SurfaceTexture mSurfaceTexture;

    public Flashlight(Camera camera, Camera.Parameters params) {
        this.camera = camera;
        this.params = params;
    }

    public static boolean isAvailable(Context context) {
        boolean availability;
        PackageManager packageManager = context.getPackageManager();
        availability = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        return availability;
    }

    public boolean isOn() { return isRunning; }

    public void turnOn() {
        // We have a list of "known-to-work" devices where we don't
        // need any videoTexture voodoo.
        if(Arrays.asList(whiteListedDevices).contains(Build.DEVICE)) {
            setCameraPreviewWithTorchOn();
        }
        // We don't "officially" support gingerbread devices but we don't
        // want them to be left off, so we implemented this code as a gamble
        // for such devices.
        else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD) {
            setCameraPreviewWithTorchOn();
        // For all other devices, start videoTexture before attempting to
        // enable flash. This is expected to be a bit slow.
        } else {
            setVideoTexture();
            setCameraPreviewWithTorchOn();
        }
    }

    public void turnOff() {
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();

        // Release surfaceTexture on platforms that support it.
        // This could potentially avoid us a few bugs and resource leak.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            releaseVideoTexture();
        }
        isRunning = false;
    }

    private void setCameraPreviewWithTorchOn() {
        camera.startPreview();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        isRunning = true;
    }

    public void destroyCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
        releaseVideoTexture();
    }

    @TargetApi(11)
    private void setVideoTexture() {
        // Flashlight does not work on many devices unless
        // surfaceTexture is set.
        mSurfaceTexture = new SurfaceTexture(0);
        try {
            camera.setPreviewTexture(mSurfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void releaseVideoTexture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if(mSurfaceTexture != null) {
                mSurfaceTexture.release();
                mSurfaceTexture = null;
            }
        }
    }
}
