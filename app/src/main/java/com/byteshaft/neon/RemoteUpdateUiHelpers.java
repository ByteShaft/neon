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
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.widget.Button;
import android.widget.RemoteViews;

public class RemoteUpdateUiHelpers extends ContextWrapper {

    public RemoteUpdateUiHelpers(Context context) {
        super(context);
    }

    void setUiButtonsOn(boolean ON) {
        if (ON) {
            setMainActivitySwitchOn(true);
            setWidgetIconOn(true);
        } else {
            setMainActivitySwitchOn(false);
            setWidgetIconOn(false);
        }
    }

    private void setWidgetIconOn(boolean ON) {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        ComponentName widgetComponent = new ComponentName(this, WidgetProvider.class);
        Intent receiver = new Intent(this, WidgetReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, receiver, 0);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.neon_widget);

        if (ON) {
            remoteViews.setImageViewResource(R.id.NeonWidget, R.drawable.button_widget_off);
        } else {
            remoteViews.setImageViewResource(R.id.NeonWidget, R.drawable.button_widget_on);
        }

        remoteViews.setOnClickPendingIntent(R.id.NeonWidget, pendingIntent);
        widgetManager.updateAppWidget(widgetComponent, remoteViews);
    }

    private void setMainActivitySwitchOn(boolean ON) {
        Button switcher = MainActivity.mSwitcher;
        if (switcher != null) {
            if (ON) {
                switcher.setBackgroundResource(R.drawable.button_off);
            } else {
                switcher.setBackgroundResource(R.drawable.button_on);
            }
        }
    }
}
