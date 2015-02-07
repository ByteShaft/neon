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

    private String[] whiteListedDevices = {"dlx", "mako", "ghost", "maguro"};
    private boolean isRunning = false;
    public static boolean isBusy = false;

    private Camera camera;
    private Camera.Parameters params;

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
        if(Arrays.asList(whiteListedDevices).contains(Build.DEVICE)) {
            setCameraPreviewWithTorchOn();
        }
        else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD) {
            setCameraPreviewWithTorchOn();
        } else {
            setVideoTexture();
            setCameraPreviewWithTorchOn();
        }
    }

    public void turnOff() {
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();
        isRunning = false;
    }

    private void setCameraPreviewWithTorchOn() {
        camera.startPreview();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        isRunning = true;
    }

    @TargetApi(11)
    private void setVideoTexture() {
        // Flashlight does not work on many devices unless
        // surfaceTexture is set.
        SurfaceTexture mSurfaceTexture = new SurfaceTexture(0);
        try {
            camera.setPreviewTexture(mSurfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
