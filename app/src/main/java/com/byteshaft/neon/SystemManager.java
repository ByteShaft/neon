package com.byteshaft.neon;

import android.content.Context;
import android.os.PowerManager;

public class SystemManager {

    private PowerManager.WakeLock mWakeLock;
    private Context mContext;

    public SystemManager(Context context) {
        this.mContext = context;
    }

    public void setWakeLock() {
        mWakeLock = getWakeLockManager();
        mWakeLock.acquire();
    }

    public void releaseWakeLock() {
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
    }

    private PowerManager getPowerManager() {
        return (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
    }

    private PowerManager.WakeLock getWakeLockManager() {
        PowerManager pm = getPowerManager();
        return pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "NEON");
    }
}
