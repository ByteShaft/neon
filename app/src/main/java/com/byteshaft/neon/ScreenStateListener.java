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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import com.byteshaft.ezflashlight.FlashlightGlobals;

public class ScreenStateListener extends ContextWrapper {

    private BroadcastReceiver mReceiver = null;

    public ScreenStateListener(Context base) {
        super(base);
    }

    void register() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) && Build.MANUFACTURER
                        .equals("HTC") && FlashlightGlobals.isFlashlightOn()) {
                    restartTorch();
                }
            }
        };
        registerReceiver(mReceiver, filter);
    }

    void unregister() {
        try {
            unregisterReceiver(mReceiver);
        } catch (IllegalArgumentException ignore) {

        }
    }

    private void restartTorch() {
        FlashlightService flashlightService = FlashlightService.getInstance();
        if (flashlightService != null) {
            flashlightService.stopTorch();
            flashlightService.lightenTorch();
        }
    }
}
