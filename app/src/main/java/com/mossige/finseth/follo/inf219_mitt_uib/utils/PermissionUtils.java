package com.mossige.finseth.follo.inf219_mitt_uib.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by andre on 17.01.17.
 */

public class PermissionUtils {

    public static boolean isPermissionGranted(String permission, Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;

        } else { // Permission is automatically granted on sdk < 23 upon installation
            return true;
        }
    }

}
