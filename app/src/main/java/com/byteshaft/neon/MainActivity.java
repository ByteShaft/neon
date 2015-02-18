package com.byteshaft.neon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements View.OnClickListener {

    private static MainActivity instance = null;
    static Button mSwitcher;
    private Helpers mHelpers;

    public static MainActivity getContext() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Flashlight.activityRunning = true;
        setContentView(R.layout.activity_main);
        instance = this;
        initializeXmlReferences();
        initializeClasses();
        mHelpers.checkFlashlightAvailability();
        mSwitcher.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Flashlight.isOn()) {
            stopService(getFlashlightServiceIntent());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Helpers.isCameraInUse() && !Flashlight.isOn()) {
            Helpers.showFlashlightBusyDialog(this);
        }
        if (Flashlight.isOn()) {
            mSwitcher.setBackgroundResource(R.drawable.button_off);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Flashlight.isOn()) {
            stopService(getFlashlightServiceIntent());
        }
    }

    @Override
    public void onClick(View view) {
        if (Flashlight.isToggleInProgress()) {
            return;
        }
        switch (view.getId()) {
            case R.id.switcher:
                if (!Flashlight.isOn()) {
                    Intent serviceIntent = new Intent(this, FlashlightService.class);
                    serviceIntent.putExtra("command", "turnOn");
                    startService(serviceIntent);
                    Flashlight.setIsBusyByActivity(true);
                } else {
                    stopService(getFlashlightServiceIntent());
                    Flashlight.setIsBusyByActivity(false);
                    Flashlight.setInUseByWidget(false);
                }
        }
    }

    private Intent getFlashlightServiceIntent() {
        return new Intent(MainActivity.this, FlashlightService.class);
    }

    private void initializeClasses() {
        mHelpers = new Helpers(MainActivity.this);
    }

    private void initializeXmlReferences() {
        mSwitcher = (Button) findViewById(R.id.switcher);
    }
}
