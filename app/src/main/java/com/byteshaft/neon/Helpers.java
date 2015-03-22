package com.byteshaft.neon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.widget.Toast;

public class Helpers {

    static void showFlashlightBusyDialog(Activity context) {
        String title = context.getString(R.string.dialog_title_resource_busy);
        String description = context.getString(R.string.dialog_description_resource_busy);
        String buttonText = context.getString(R.string.dialog_ok);

        AlertDialog alertDialog = buildErrorDialog(context, title, description, buttonText);
        alertDialog.show();
    }

    static void showFlashlightBusyToast(Context context) {
        Toast.makeText(context, context.getString(R.string.toast_resource_busy),
                Toast.LENGTH_SHORT).show();
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
