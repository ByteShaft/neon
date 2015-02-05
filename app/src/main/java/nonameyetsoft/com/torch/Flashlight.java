package nonameyetsoft.com.torch;


import android.hardware.Camera;
import android.util.Log;

public class Flashlight {

    private boolean flashRunning = false;

    private Camera camera;
    private Camera.Parameters params;
    private Thread blinker;

    public void turnOnFlash() {
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        camera.startPreview();
        flashRunning = true;
    }

    public void turnOffFlash() {
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();
        flashRunning = false;
    }

    public boolean isFlashOn() {
        if(flashRunning) {
            return true;
        } else {
            return false;
        }
    }

    public void startBlinking(final int blinkPeriod) {
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
                    while(blinker != null ) {
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
