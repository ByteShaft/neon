package com.byteshaft.neon;

public class AppGlobals {

    static final String LOG_TAG = "NEON";
    private static boolean isWidgetTapped = false;

    static boolean isWidgetTapped() {
        return isWidgetTapped;
    }

    static void setIsWidgetTapped(boolean tapped) {
        isWidgetTapped = tapped;
    }
}
