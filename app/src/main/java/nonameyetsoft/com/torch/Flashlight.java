package nonameyetsoft.com.torch;


import android.hardware.Camera;

public class Flashlight {

    private boolean flashRunning = false;

    private Camera camera;
    private Camera.Parameters params;

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

    public Flashlight(Camera camera, Camera.Parameters params) {
        this.camera = camera;
        this.params = params;
    }
}
