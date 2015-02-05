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
    Camera.Parameters params;
    Flashlight flashlight;
    Helpers helpers;
    RelativeLayout layout;
    DatabaseSP dataOnClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initializeClasses();
        if(!helpers.isFlashlightAvailable(this)) {
            helpers.showErrorDialog(this);
        }
        layout = (RelativeLayout) findViewById(R.id.mainLayout);
        switcher = (Switch) findViewById(R.id.switcher);
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flashlight.isFlashOn()) {
                    flashlight.turnOnFlash();
                    layout.setBackgroundColor(Color.WHITE);
                    dataOnClick.onClickAddOne(MainActivity.this);
                } else {
                    flashlight.turnOffFlash();
                    layout.setBackgroundColor(Color.BLACK);
                }
            }
        });
    }
    private void initializeClasses() {
        camera = Camera.open();
        params = camera.getParameters();
        flashlight = new Flashlight(camera, params);
        helpers = new Helpers();
        dataOnClick = new DatabaseSP();
    }
}
