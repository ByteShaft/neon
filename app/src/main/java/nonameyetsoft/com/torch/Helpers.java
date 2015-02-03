package nonameyetsoft.com.torch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

public class Helpers {

    public boolean isFlashlightAvailable(Context context) {
        boolean availability;
        PackageManager packageManager = context.getPackageManager();
        availability = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        return availability;
    }

    public void showErrorDialog(final Activity context) {
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
