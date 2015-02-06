package nonameyetsoft.com.torch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

public class Helpers {

    public void showErrorDialogIfFlashlightNotAvailable(Activity context) {
        if(!isFlashlightAvailable(context)) {
            showErrorDialog(context);
        }
    }

    public void showFlashlightBusyErrorDialog(final Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Resource Busy");
        builder.setMessage("Camera Resource already in use.");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                context.finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean isFlashlightAvailable(Activity context) {
        boolean availability;
        PackageManager packageManager = context.getPackageManager();
        availability = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        return availability;
    }

    private void showErrorDialog(final Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Flashlight not detected.");
        builder.setMessage("Your device does not seem to have a flashlight.");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
