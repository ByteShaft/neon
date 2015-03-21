package com.byteshaft.neon;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.PowerManager;

public class SystemManager extends ContextWrapper {

    private PowerManager.WakeLock mWakeLock = null;

    public SystemManager(Context context) {
        super(context);
    }

    void setWakeLock() {
        mWakeLock = getWakeLockManager();
        mWakeLock.acquire();
    }

    void releaseWakeLock() {
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
    }

    private PowerManager getPowerManager() {
        return (PowerManager) getSystemService(Context.POWER_SERVICE);
    }

    private PowerManager.WakeLock getWakeLockManager() {
        PowerManager powerManager = getPowerManager();
        return powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, AppGlobals.LOG_TAG);
    }
}
