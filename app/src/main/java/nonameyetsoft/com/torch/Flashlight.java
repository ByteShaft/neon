package nonameyetsoft.com.torch;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import java.io.IOException;

public class Flashlight {

    public static boolean running = false;
    private Camera camera;
    private Camera.Parameters params;


    public void turnOn() {
        setVideoText();
        camera.startPreview();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        running = true;
    }

    public void turnOff() {
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();
        running = false;
    }

    public static boolean isAvailable(Context context) {
        boolean availability;
        PackageManager packageManager = context.getPackageManager();
        availability = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        return availability;
    }

    public boolean isOn() {
        return running;
    }

    private void setVideoText() {
        // Flashlight does not work on many devices unless
        // surfaceTexture is set.
        SurfaceTexture mSurfaceTexture = new SurfaceTexture(0);
        try {
            camera.setPreviewTexture(mSurfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Flashlight(Camera camera, Camera.Parameters params) {
        this.camera = camera;
        this.params = params;
    }
}
