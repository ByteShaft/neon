package nonameyetsoft.com.torch;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Switch;

public class MainActivity extends Activity implements View.OnClickListener  {

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
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_main);

        initializeClasses();
        initializeXmlReferences();
        helpers.showErrorDialogIfFlashlightNotAvailable(MainActivity.this);
        switcher.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (camera == null) {
            initializeClasses();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.switcher:
                if (!flashlight.isFlashOn()) {
                    flashlight.turnOnFlash();
                    layout.setBackgroundColor(Color.WHITE);
                    dataOnClick.addOneToDatabase(MainActivity.this);
                } else {
                    flashlight.turnOffFlash();
                    layout.setBackgroundColor(Color.BLACK);
                }
        }
    }

    private void initializeClasses() {
        camera = Camera.open();
        params = camera.getParameters();
        flashlight = new Flashlight(camera, params);
        helpers = new Helpers();
        dataOnClick = new DatabaseSP();
    }

    private void initializeXmlReferences() {
        layout = (RelativeLayout) findViewById(R.id.mainLayout);
        switcher = (Switch) findViewById(R.id.switcher);
    }
}
