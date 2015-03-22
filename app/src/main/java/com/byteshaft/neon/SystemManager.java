/*
 *
 *  * (C) Copyright 2015 byteShaft Inc.
 *  *
 *  * All rights reserved. This program and the accompanying materials
 *  * are made available under the terms of the GNU Lesser General Public License
 *  * (LGPL) version 2.1 which accompanies this distribution, and is available at
 *  * http://www.gnu.org/licenses/lgpl-2.1.html
 *  *
 *  * This library is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  * Lesser General Public License for more details.
 *  
 */

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
