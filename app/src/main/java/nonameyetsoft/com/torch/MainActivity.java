package nonameyetsoft.com.torch;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;


public class MainActivity extends Activity implements View.OnClickListener, SurfaceHolder.Callback {

    Button switcher;
    Camera camera;
    Camera.Parameters params;
    Flashlight flashlight;
    Helpers helpers;
    Notifications notifications;
    SurfaceHolder mHolder;
    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            flashlight.turnOff();
            destroyCamera();
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
        if (flashlight.isOn()) {
            flashlight.turnOff();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeCamera();

        SurfaceView preview = (SurfaceView)findViewById(R.id.preview);
        SurfaceHolder mHolder = preview.getHolder();
        mHolder.addCallback(MainActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!flashlight.isOn() && Flashlight.isBusy) {
            camera = null;
        } else if (!flashlight.isOn() && camera != null) {
            destroyCamera();
        }

        // Unregister the broadcast receive when the app is
        // being closed to avoid leakage of resource.
        if (isReceiverRegistered && !flashlight.isOn()) {
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switcher:
                if (!flashlight.isOn()) {
                    flashlight.turnOn();
                    notifications.startNotification();
                    switcher.setBackgroundResource(R.drawable.button_off);
                } else {
                    flashlight.turnOff();
                    notifications.endNotification();
                    switcher.setBackgroundResource(R.drawable.button_on);
                }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHolder = holder;
        try {
            camera.setPreviewDisplay(mHolder);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHolder = null;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    private void initializeCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
                flashlight = new Flashlight(MainActivity.this, camera, params);
            } catch (RuntimeException e) {
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

    private void initializeXmlReferences() {
        switcher = (Button) findViewById(R.id.switcher);
    }
}
