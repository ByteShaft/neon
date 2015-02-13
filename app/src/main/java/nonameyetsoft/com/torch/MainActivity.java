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
    Notification notification;
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
//        IntentFilter filter = new IntentFilter("android.intent.CLOSE_ACTIVITY");
//        registerReceiver(mReceiver, filter);
//        isReceiverRegistered = true;
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

//        IntentFilter filter = new IntentFilter("android.intent.CLOSE_ACTIVITY");
//        registerReceiver(mReceiver, filter);
//        isReceiverRegistered = true;


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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notification.clear();
        if (isReceiverRegistered) {
            unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switcher:
                if (!flashlight.isOn()) {
                    flashlight.turnOn();
                    notification.start();
                    switcher.setBackgroundResource(R.drawable.button_off);
                    registerReceiver();
                } else {
                    flashlight.turnOff();
                    notification.clear();
                    switcher.setBackgroundResource(R.drawable.button_on);
                    unregisterReceiver();
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

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter("android.intent.CLOSE_ACTIVITY");
        registerReceiver(mReceiver, filter);
        isReceiverRegistered = true;
    }

    private void unregisterReceiver() {
        if (isReceiverRegistered && !flashlight.isOn()) {
            unregisterReceiver(mReceiver);
        }
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
        notification = new Notification(MainActivity.this);
    }

    private void initializeXmlReferences() {
        switcher = (Button) findViewById(R.id.switcher);
    }
}
