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

public class AppGlobals {

    static final String LOG_TAG = "NEON";
    private static boolean isWidgetTapped = false;
    private static boolean isServiceSwitchInProgress = false;

    static boolean isWidgetTapped() {
        return isWidgetTapped;
    }

    static void setIsWidgetTapped(boolean tapped) {
        isWidgetTapped = tapped;
    }

    static boolean isIsServiceSwitchInProgress() {
        return isServiceSwitchInProgress;
    }

    static void setIsServiceSwitchInProgress(boolean switching) {
        isServiceSwitchInProgress = switching;
    }
}
