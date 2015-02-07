package nonameyetsoft.com.torch;


import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

public class Flashlight {

    public static boolean running = false;
    private Camera camera;
    private Camera.Parameters params;
    private Thread blinker;


    public void turnOn() {
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        camera.startPreview();
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

    public void startBlinking(int blinkPeriod) {
        createBlinkerThread(blinkPeriod);
        blinker.start();
    }

    public void stopBlinking() {
        blinker = null;
    }

    private void createBlinkerThread(final int blinkPeriod) {
        blinker = new Thread() {
            @Override
            public void run() {
                try {
                    while(blinker != null) {
                        turnOnFlash();
                        sleep(blinkPeriod);
                        turnOffFlash();
                        sleep(blinkPeriod);
                    }
                } catch(InterruptedException e) {
                    Log.e("FLASH_BLINKER", "Blinking thread was interrupted.");
                }
            }
        };
    }

    public Flashlight(Camera camera, Camera.Parameters params) {
        this.camera = camera;
        this.params = params;
    }
}
