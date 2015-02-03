package nonameyetsoft.com.torch;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Switch;


public class MainActivity extends Activity  {

    Switch switcher;
    Camera camera;
    Flashlight flashlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        camera = Camera.open();
        flashlight = new Flashlight(camera);

        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainLayout);

        switcher = (Switch) findViewById(R.id.switcher);
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flashlight.isFlashOn()) {
                    flashlight.turnOnFlash();
                    layout.setBackgroundColor(Color.WHITE);
                } else {
                    flashlight.turnOffFlash();
                    layout.setBackgroundColor(Color.BLACK);
                }
            }
        });
    }
}
