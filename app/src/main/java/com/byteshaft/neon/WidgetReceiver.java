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
import android.content.Intent;

import com.byteshaft.ezflashlight.FlashlightGlobals;

import java.util.Timer;
import java.util.TimerTask;

public class WidgetReceiver extends BroadcastReceiver {

    private static Timer sTimer = null;

    @Override
    public void onReceive(final Context context, Intent intent) {
        AppGlobals.setIsWidgetTapped(true);
        final int SERVICE_SHUTDOWN_DELAY = 600;
        final String STARTER = "widget";
        Intent serviceIntent = new Intent(context, FlashlightService.class);
        RemoteUpdateUiHelpers remoteUi = new RemoteUpdateUiHelpers(context);
        Helpers helpers = new Helpers(context);
        TimerTask serviceTimerTask = helpers.getServiceStopTimerTask(serviceIntent);

        if (sTimer == null) {
            sTimer = helpers.getTimer();
        }

        if (!FlashlightService.isRunning()) {
            serviceIntent.putExtra("STARTER", STARTER);
            context.startService(serviceIntent);
        } else if (FlashlightGlobals.isFlashlightOn()) {
            remoteUi.setUiButtonsOn(false);
            FlashlightService.getInstance().stopTorch();
            sTimer.schedule(serviceTimerTask, SERVICE_SHUTDOWN_DELAY);
        } else {
            sTimer.cancel();
            sTimer = null;
            remoteUi.setUiButtonsOn(true);
            FlashlightService.getInstance().lightenTorch();
        }
    }
}
