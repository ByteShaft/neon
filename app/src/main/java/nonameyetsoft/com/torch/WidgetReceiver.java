package nonameyetsoft.com.torch;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.widget.RemoteViews;

public class WidgetReceiver extends BroadcastReceiver{
    private static Camera mCamera;
    Camera.Parameters mParams;
    private static Flashlight mFlashlight;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        initializeCamera();

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.neon_widget);

        if (mFlashlight == null) {
            mParams = mCamera.getParameters();
            mFlashlight = new Flashlight(context, mCamera, mParams);
        }
        if (!Flashlight.isOn()) {
            views.setImageViewResource(R.id.NeonWidget, R.drawable.button_widget_off);
            mFlashlight.turnOn();
            Flashlight.isBusyByWidget = true;
        } else {
            views.setImageViewResource(R.id.NeonWidget, R.drawable.button_widget_on);
            mFlashlight.turnOff();
            Flashlight.isBusyByWidget = false;
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(new ComponentName(context, WidgetProvider.class),
                views);
    }

    public void initializeCamera() {
        if (mCamera == null) {
            mCamera = Camera.open();
        }
    }
}
