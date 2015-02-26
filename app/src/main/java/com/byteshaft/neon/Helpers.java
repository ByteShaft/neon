package com.byteshaft.neon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Camera;

@SuppressWarnings("deprecation")
public class Helpers {

    public static boolean isCameraInUse() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (RuntimeException e) {
            return true;
        } finally {
            if (camera != null) {
                camera.release();
            }
        }
        return false;
    }

    public static void showFlashlightBusyDialog(final Activity context) {
        String title = context.getString(R.string.dialog_title_resource_busy);
        String description = context.getString(R.string.dialog_description_resource_busy);
        String buttonText = context.getString(R.string.dialog_ok);

        AlertDialog alertDialog = buildErrorDialog(context, title, description, buttonText);
        alertDialog.show();
    }

    private static AlertDialog buildErrorDialog(final Activity context, String title,
                                                String description, String buttonText) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(description);
        builder.setCancelable(false);
        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                context.finish();
            }
        });

        return builder.create();
    }
}
