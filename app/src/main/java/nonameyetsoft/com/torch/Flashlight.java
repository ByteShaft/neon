package nonameyetsoft.com.torch;

import android.os.Bundle;

public class Flashlight extends MainActivity {

    @Override
    protected void onStop() {
        super.onStop();

        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (camera == null) {
            initializeCamera();
        }
    }


}