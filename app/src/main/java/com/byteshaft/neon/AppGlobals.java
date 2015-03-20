package com.byteshaft.neon;

public class AppGlobals {

    // The tag name to appear in logs.
    static final String LOG_TAG = "NEON";
    private static boolean isWidgetTapped = false;

    public static boolean isWidgetTapped() {
        return isWidgetTapped;
    }

    public static void setIsWidgetTapped(boolean tapped) {
        isWidgetTapped = tapped;
    }
}
