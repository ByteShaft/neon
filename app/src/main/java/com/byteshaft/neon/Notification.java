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

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class Notification extends ContextWrapper {

    public Notification(Context context) {
        super(context);
    }

    android.app.Notification getNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        setupVisuals(builder);
        setOnTapIntentAction(builder);
        return builder.build();
    }

    private void setupVisuals(NotificationCompat.Builder builder) {
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(getString(R.string.notification_message));
        // dismiss notification when its tapped.
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_notify);
        // disable slide to remove for the notification.
        builder.setOngoing(true);
    }

    private void setOnTapIntentAction(NotificationCompat.Builder builder) {
        Intent intent = new Intent("com.byteshaft.neon.CLOSE_ACTIVITY");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
    }
}
