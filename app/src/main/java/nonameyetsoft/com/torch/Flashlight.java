package nonameyetsoft.com.torch;


import android.hardware.Camera;

public class Flashlight {

    private boolean flashRunning = false;

    private Camera camera;
    private Camera.Parameters param;

    public void turnOnFlash() {
        param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(param);
        camera.startPreview();
        flashRunning = true;
    }

    public void turnOffFlash() {
        param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(param);
        camera.stopPreview();
        flashRunning = false;
    }

    private void initialize() {
        param = camera.getParameters();
    }

    public boolean isFlashOn() {
        if(flashRunning) {
            return true;
        } else {
            return false;
        }
    }

    public Flashlight(Camera camera) {
        this.camera = camera;
        initialize();
    }
}
