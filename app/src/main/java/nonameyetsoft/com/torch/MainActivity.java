package nonameyetsoft.com.torch;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements View.OnClickListener {

    private static MainActivity instance = null;
    private Button mSwitcher;
    private Helpers mHelpers;
    private Notifications mNotifications;

    public static MainActivity getContext() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        initializeXmlReferences();
        if (!FlashlightService.isRunning()) {
            Log.i(Flashlight.LOG_TAG, "Starting service.");
            startService(getFlashlightServiceIntent());
        }
        initializeClasses();
        mHelpers.checkFlashlightAvailability();
        mSwitcher.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Flashlight.isOn()) {
            stopService(getFlashlightServiceIntent());
            Flashlight.setInUseByWidget(false);
            setWidgetIconOn(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Flashlight.isBusyByWidget()) {
            mSwitcher.setBackgroundResource(R.drawable.button_off);
        } else {
            mSwitcher.setBackgroundResource(R.drawable.button_on);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!Flashlight.isOn()) {
            if (FlashlightService.isRunning()) {
                stopService(getFlashlightServiceIntent());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!FlashlightService.isRunning()) {
            startService(getFlashlightServiceIntent());
        } else if (Flashlight.isOn()) {
            mSwitcher.setBackgroundResource(R.drawable.button_off);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setWidgetIconOn(false);
        if (Flashlight.isOn()) {
            stopService(getFlashlightServiceIntent());
        }
        mNotifications.endNotification();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switcher:
                if (!Flashlight.isOn()) {
                    FlashlightService.getInstance().lightenTorch();
                    mSwitcher.setBackgroundResource(R.drawable.button_off);
                    setWidgetIconOn(true);
                } else {
                    FlashlightService.getInstance().stopTorch();
                    mSwitcher.setBackgroundResource(R.drawable.button_on);
                    Flashlight.setInUseByWidget(false);
                    setWidgetIconOn(false);
                }
        }
    }

    private Intent getFlashlightServiceIntent() {
        return new Intent(MainActivity.this, FlashlightService.class);
    }

    private void initializeClasses() {
        mHelpers = new Helpers(MainActivity.this);
        mNotifications = new Notifications(MainActivity.this);
    }

    private void initializeXmlReferences() {
        mSwitcher = (Button) findViewById(R.id.switcher);
    }

    private void setWidgetIconOn(boolean ON) {
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.neon_widget);
        if (ON) {
            Log.i(Flashlight.LOG_TAG, "Setting widget icon from app to ON");
            views.setImageViewResource(R.id.NeonWidget, R.drawable.button_widget_off);
        } else {
            views.setImageViewResource(R.id.NeonWidget, R.drawable.button_widget_on);
            Log.i(Flashlight.LOG_TAG, "Setting widget icon from app to OFF");
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.updateAppWidget(new ComponentName(this, WidgetProvider.class),
                views);
    }
}
