package nonameyetsoft.com.torch;

import android.app.Activity;
import android.app.Notification;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener {
    private boolean flashBusy = false;
    Button switcher;
    Camera camera;
    Camera.Parameters params;
    Flashlight flashlight;
    Helpers helpers;
    NotificationFeature notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppWindowFullScreen();
        setContentView(R.layout.activity_main);

        initializeClasses();
        initializeXmlReferences();
        helpers.checkFlashlightAvailability();
        switcher.setOnClickListener(this);
        importNotification();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        notification.notificationManager.cancel(446);
        if(flashlight.isOn()) {
            flashlight.turnOff();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!Flashlight.running && flashBusy) {
            camera = null;
        } else if(!Flashlight.running) {
            destroyCamera();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.switcher:
                if (!flashlight.isOn()) {
                    flashlight.turnOn();
                    notification.notifyMe();
                    switcher.setBackgroundResource(R.drawable.button_off);

                } else {
                    flashlight.turnOff();
                    switcher.setBackgroundResource(R.drawable.button_on);
                    notification.notificationManager.cancel(446);

                }
        }
    }

    private void setAppWindowFullScreen() {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
    }

    private void initializeCamera() {
        if(camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
                flashlight = new Flashlight(camera, params);
            } catch(RuntimeException e) {
                Log.e("FLASHLIGHT", "Resource busy.");
                helpers.showFlashlightBusyDialog();
                flashBusy = true;
            }
        }
    }

    private void destroyCamera() {
        camera.release();
        camera = null;
    }

    private void initializeClasses() {
        helpers = new Helpers(MainActivity.this);
    }
    public void importNotification() { notification = new NotificationFeature(MainActivity.this);}
    private void initializeXmlReferences() {
        switcher = (Button) findViewById(R.id.switcher);
    }
}