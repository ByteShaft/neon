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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.byteshaft.ezflashlight.FlashlightGlobals;

import java.util.Timer;
import java.util.TimerTask;

public class Helpers extends ContextWrapper {

    private static Toast sToast = null;

    public Helpers(Context base) {
        super(base);
    }

    static void showFlashlightBusyDialog(Activity context) {
        String title = context.getString(R.string.dialog_title_resource_busy);
        String description = context.getString(R.string.dialog_description_resource_busy);
        String buttonText = context.getString(R.string.dialog_ok);

        AlertDialog alertDialog = buildErrorDialog(context, title, description, buttonText);
        alertDialog.show();
    }

    static void showFlashlightBusyToast(Context context) {
        if (sToast != null && sToast.getView().isShown()) {
            sToast.cancel();
        } else {
            sToast = Toast.makeText(context, context.getString(R.string.toast_resource_busy),
                    Toast.LENGTH_SHORT);
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                sToast.show();
            }
        });
    }

    TimerTask getServiceStopTimerTask(final Intent serviceIntent) {
        return new TimerTask() {
            @Override
            public void run() {
                if (!FlashlightGlobals.isFlashlightOn()) {
                    stopService(serviceIntent);
                }
            }
        };
    }

    Timer getTimer() {
        return new Timer();
    }

    private static AlertDialog buildErrorDialog(final Activity context, String title,
                                                String description, String buttonText) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(description);
        builder.setCancelable(false);
        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, int which) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        context.finish();
                    }
                });
            }
        });

        return builder.create();
    }
}
