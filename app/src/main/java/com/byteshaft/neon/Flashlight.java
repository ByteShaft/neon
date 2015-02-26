package com.byteshaft.neon;

import android.app.Application;

public class Flashlight extends Application {

    // The tag name to appear in logs.
    public static final String LOG_TAG = "NEON";
    public static final String TOAST_INTENT = "com.byteshaft.toast";
    private static boolean isBusyByWidget = false;
    private static boolean isRunning = false;
    private static boolean isRunningFromWidget = false;
    public static boolean activityRunning = false;
    public static boolean isToggleInProgress = false;

    public static boolean isOn() {
        return isRunning;
    }

    public static void setIsOn(boolean ON) {
        isRunning = ON;
    }

    public static void setBusyByWidget(boolean busy) {
        isBusyByWidget = busy;
    }

    public static boolean isBusyByWidget() {
        return isBusyByWidget;
    }

    public static void setToggleInProgress(boolean inProgress) {
        isToggleInProgress = inProgress;
    }

    public static boolean isToggleInProgress() {
        return isToggleInProgress;
    }

    public static boolean isRunningFromWidget() {
        return isRunningFromWidget;
    }

    public static void setIsRunningFromWidget(boolean YES) {
        isRunningFromWidget = YES;
    }

}
