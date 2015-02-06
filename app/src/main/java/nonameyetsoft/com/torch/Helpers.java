package nonameyetsoft.com.torch;

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
        String title = "Resource Busy";
        String description = "Camera Resource already in use.";
        String buttonText = "Ok";

        AlertDialog alertDialog = buildErrorDialog(title, description, buttonText);
        alertDialog.show();
    }

    private void showFlashlightNotAvailableDialog() {
        String title = "Flashlight not detected.";
        String description = "Your device does not seem to have a flashlight.";
        String buttonText = "Ok";

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
