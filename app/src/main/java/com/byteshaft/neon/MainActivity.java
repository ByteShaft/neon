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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.byteshaft.ezflashlight.FlashlightGlobals;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements Button.OnClickListener {

    static Button mSwitcher = null;
    private static MainActivity sActivityInstance = null;
    private RemoteUpdateUiHelpers mRemoteUi = null;
    private Intent mServiceIntent = null;

    static MainActivity getInstance() {
        return sActivityInstance;
    }

    static void stopApp() {
        if (sActivityInstance != null) {
            sActivityInstance.finish();
        }
    }

    private void setActivityInstance(MainActivity mainActivity) {
        sActivityInstance = mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActivityInstance(this);
        mRemoteUi = new RemoteUpdateUiHelpers(this);
        mSwitcher = (Button) findViewById(R.id.switcher);
        mSwitcher.setOnClickListener(this);
        mServiceIntent = new Intent(this, FlashlightService.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (FlashlightGlobals.isFlashlightOn()) {
            mRemoteUi.setUiButtonsOn(true);
        } else if (!FlashlightService.isRunning()) {
            startServiceWithFlashlightAutoOn(false);
        }
        AppGlobals.setIsWidgetTapped(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopService(mServiceIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!FlashlightGlobals.isFlashlightOn()) {
            stopService(mServiceIntent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!FlashlightGlobals.isFlashlightOn()) {
            stopService(mServiceIntent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switcher:
                if (!FlashlightGlobals.isFlashlightOn()) {
                    mRemoteUi.setUiButtonsOn(true);
                    if (!FlashlightService.isRunning()) {
                        startServiceWithFlashlightAutoOn(true);
                    } else {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                FlashlightService.getInstance().lightenTorch();
                            }
                        });
                    }
                    AppGlobals.setIsWidgetTapped(false);
                } else {
                    mRemoteUi.setUiButtonsOn(false);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            FlashlightService.getInstance().stopTorch();
                        }
                    });
                }
        }
    }

    private void startServiceWithFlashlightAutoOn(boolean ON) {
        final String STARTER = "app";
        mServiceIntent.putExtra("STARTER", STARTER);
        mServiceIntent.putExtra("AUTOSTART", ON);
        startService(mServiceIntent);
    }
}
