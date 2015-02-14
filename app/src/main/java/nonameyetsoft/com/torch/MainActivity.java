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
    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            flashlight.turnOff();
            finish();
        }
    };
    private boolean isReceiverRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeClasses();
        initializeXmlReferences();
        IntentFilter filter = new IntentFilter("android.intent.CLOSE_ACTIVITY");
        registerReceiver(mReceiver, filter);
        isReceiverRegistered = true;
        helpers.checkFlashlightAvailability();
        switcher.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Flashlight.isOn()) {
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
        if (!Flashlight.isOn() && Flashlight.isBusy ) {
            camera = null;
        } else if (!Flashlight.isOn() && camera != null) {
            destroyCamera();
        }
        // Unregister the broadcast receive when the app is
        // being closed to avoid leakage of resource.
        if (!Flashlight.isOn() && isReceiverRegistered) {
            // Be super cautious, incase the boolean state was
            // incorrect.
            try {
                unregisterReceiver(mReceiver);
            } catch (IllegalArgumentException e) {
                Log.w("NEON", "Receiver not registered.");
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!Flashlight.isOn()) {
            if (camera != null) { destroyCamera(); }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Flashlight.isOn()) {
            flashlight.turnOff();
            notifications.endNotification();
//            try {
//                unregisterReceiver(mReceiver);
//            } catch (IllegalArgumentException e) {
//                Log.w("NEON", "Receiver not registered.");
//            }
            destroyCamera();
        }
        notifications.endNotification();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switcher:
                if (!Flashlight.isOn()) {
                    // Turn on the flash in a new thread as this
                    // operation could potentially block the UI.
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            flashlight.turnOn();
                        }
                    }).start();
                    notifications.startNotification();
                    switcher.setBackgroundResource(R.drawable.button_off);
                } else {
                    // Again, turning off flash is an expensive operation
                    // on the UI thread, do it in a different thread.
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            flashlight.turnOff();
                        }
                    }).start();
                    notifications.endNotification();
                    switcher.setBackgroundResource(R.drawable.button_on);
                }
        }
    }

    private void initializeCamera() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (camera == null) {
                    try {
                        camera = Camera.open();
                        params = camera.getParameters();
                        flashlight = new Flashlight(MainActivity.this, camera, params);
                    } catch (RuntimeException e) {
                        Log.e("FLASHLIGHT", "Resource busy.");

                        // If camera is busy show the Error dialog in the
                        // UI thread.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                helpers.showFlashlightBusyDialog();
                                Flashlight.isBusy = true;
                            }
                        });
                    }
                }
            }
        }).start();
    }

    private void destroyCamera() {
        camera.release();
        camera = null;
    }

    private void initializeClasses() {
        helpers = new Helpers(MainActivity.this);
        notifications = new Notifications(MainActivity.this);
    }

    private void initializeXmlReferences() {
        switcher = (Button) findViewById(R.id.switcher);
    }
}
