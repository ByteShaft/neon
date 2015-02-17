package nonameyetsoft.com.torch;

import android.content.Context;
import android.content.pm.PackageManager;

public class Flashlight {

    // The tag name to appear in logs.
    public static final String LOG_TAG = "NEON";
    private static boolean isBusyByWidget = false;
    private static boolean isBusy = false;
    private static boolean isRunning = false;
    private static boolean isBusyByActivity = false;
    private static boolean isBusyByOtherApp = false;
    public static boolean isWidgetContext = false;
    public static boolean activityRunning = false;

    public static boolean isAvailable(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public static boolean isOn() {
        // Returns true if flashlight is on.
        return isRunning;
    }

    public static void setBusy(boolean busy) {
        isBusy = busy;
    }

    public static void setInUseByWidget(boolean busy) {
        isBusyByWidget = busy;
    }

    public static boolean isBusyByWidget() {
        return isBusyByWidget;
    }

    public static void setIsOn(boolean ON) {
        isRunning = ON;
    }

    public static void setIsBusyByActivity(boolean busy) {
        isBusyByActivity = true;
    }

    public static void setIsBusyByOtherApp(boolean busy) {
        isBusyByOtherApp = true;
    }

    public static boolean isIsBusyByOtherApp() {
        return isBusyByOtherApp;
    }
}
