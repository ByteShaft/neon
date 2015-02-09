package com.byteshaft.neon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class Helpers {

    private Activity context;

    public Helpers(Activity context) {
        this.context = context;
    }

    public void checkFlashlightAvailability() {
        if(!Flashlight.isAvailable(context)) {
            showFlashlightNotAvailableDialog();
        }
    }

    public void showFlashlightBusyDialog() {
        String title = context.getString(R.string.dialog_title_resource_busy);
        String description = context.getString(R.string.dialog_description_resource_busy);
        String buttonText = context.getString(R.string.dialog_ok);

        AlertDialog alertDialog = buildErrorDialog(title, description, buttonText);
        alertDialog.show();
    }

    private void showFlashlightNotAvailableDialog() {
        String title = context.getString(R.string.dialog_title_flashlight_not_available);
        String description = context.getString(R.string.dialog_description_flash_not_available);
        String buttonText = context.getString(R.string.dialog_ok);

        AlertDialog alertDialog = buildErrorDialog(title, description, buttonText);
        alertDialog.show();
    }

    private AlertDialog buildErrorDialog(String title, String description, String buttonText) {

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
