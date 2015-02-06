package nonameyetsoft.com.torch;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener {

    Button switcher;
    Camera camera;
    Camera.Parameters params;
    Flashlight flashlight;
    Helpers helpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_main);

        initializeClasses();
        initializeXmlReferences();
        helpers.showErrorDialogIfFlashlightNotAvailable(MainActivity.this);
        switcher.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(flashlight.isFlashOn()) {
            flashlight.turnOffFlash();
        }
        camera.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!flashlight.isFlashOn()) {
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (camera == null) {
            initializeClasses();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.switcher:
                if (!flashlight.isFlashOn()) {
                    flashlight.turnOnFlash();
                    switcher.setBackgroundResource(R.drawable.button_off);
                } else {
                    flashlight.turnOffFlash();
                    switcher.setBackgroundResource(R.drawable.button_on);
                }
        }
    }

    private void initializeClasses() {
        helpers = new Helpers();
        try {
            camera = Camera.open();
            params = camera.getParameters();
            flashlight = new Flashlight(camera, params);
        } catch(RuntimeException e) {
            Log.e("FLASH_LIGHT", "Flashlight resource busy.");
            helpers.showFlashlightBusyErrorDialog(MainActivity.this);
        }
    }
    
    private void initializeXmlReferences() {
        switcher = (Button) findViewById(R.id.switcher);
    }
}