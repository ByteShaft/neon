package nonameyetsoft.com.torch;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener {

    Button switcher;
    Camera camera;
    Camera.Parameters params;
    Flashlight flashlight;
    Helpers helpers;
    Notifications notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeClasses();
        initializeXmlReferences();
        IntentFilter filter = new IntentFilter("android.intent.CLOSE_ACTIVITY");
        registerReceiver(mReceiver, filter);
        helpers.checkFlashlightAvailability();
        switcher.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
    protected void onPause() {
        super.onPause();
        if(!flashlight.isOn() && Flashlight.isBusy) {
            camera = null;
        } else if(!flashlight.isOn() && camera != null) {
            destroyCamera();
        }
        // Unregister the broadcast receive when the app is
        // being closed to avoid leakage of resource.
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.switcher:
                if (!flashlight.isOn()) {
                    flashlight.turnOn();
                    notifications.startNotification();
                    switcher.setBackgroundResource(R.drawable.button_off);

                } else {
                    flashlight.turnOff();
                    switcher.setBackgroundResource(R.drawable.button_on);
                    notifications.turnOfnotificationOnTurnOff();
                }
        }
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
                Flashlight.isBusy = true;
            }
        }
    }

    private void destroyCamera() {
        camera.release();
        camera = null;
    }

    private void initializeClasses() {
        helpers = new Helpers(MainActivity.this);
        notifications = new Notifications(MainActivity.this);
    }
    
    private void initializeXmlReferences() { switcher = (Button) findViewById(R.id.switcher); }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            flashlight.turnOff();
            finish();
        }
    };
}
