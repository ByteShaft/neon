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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.byteshaft.ezflashlight.FlashlightGlobals;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements Button.OnClickListener {

    static Button mSwitcher = null;
    private static MainActivity sActivityInstance = null;
    private Helpers mHelpers = null;
    private RemoteUpdateUiHelpers mRemoteUi = null;

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
        mHelpers = new Helpers(this);
        mSwitcher = (Button) findViewById(R.id.switcher);
        mSwitcher.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (FlashlightGlobals.isFlashlightOn()) {
            mRemoteUi.setUiButtonsOn(true);
        } else if (!FlashlightService.isRunning()) {
            mHelpers.startFlashlightServiceWithFlashlightOn(false);
        }
        AppGlobals.setIsWidgetTapped(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mHelpers.stopFlashlightService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!FlashlightGlobals.isFlashlightOn()) {
            mHelpers.stopFlashlightService();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!FlashlightGlobals.isFlashlightOn()) {
            mHelpers.stopFlashlightService();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switcher:
                if (!FlashlightGlobals.isFlashlightOn()) {
                    if (!FlashlightService.isRunning()) {
                        mRemoteUi.setUiButtonsOn(true);
                        mHelpers.startFlashlightServiceWithFlashlightOn(true);
                    } else {
                        FlashlightService.getInstance().lightenTorch();
                    }
                    AppGlobals.setIsWidgetTapped(false);
                } else {
                    FlashlightService.getInstance().stopTorch();
                }
        }
    }
}
