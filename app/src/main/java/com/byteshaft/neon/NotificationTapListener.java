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

public class NotificationTapListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteUpdateUiHelpers remoteUi = new RemoteUpdateUiHelpers(context);
        Intent serviceIntent = new Intent(context, FlashlightService.class);
        remoteUi.setUiButtonsOn(false);
        context.stopService(serviceIntent);
        MainActivity.stopApp();
    }
}
