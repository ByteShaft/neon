package nonameyetsoft.com.torch;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;


public class MainActivity extends Activity  {

    Switch switcher;
    Camera camera;
    Camera.Parameters params;
    Flashlight flashlight;
    Helpers helpers;
    RelativeLayout layout;
//creating a string for DB
    public static final String NUM = "number";
    SharedPreferences setting;
    public static int counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initializeClasses();
//DB edited by bilal
        setting = PreferenceManager.getDefaultSharedPreferences(this);

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
                    // working with  shared preference starts edited by bilal
                    int val =  setting.getInt(NUM , 0);
                    if (val == 0){
                        counter = val+1;
                    }
                    else
                    {
                        counter = val+1;
                    }
                    SharedPreferences.Editor  editor;
                    editor = setting.edit();
                    int inputVal = counter ;
                    editor.putInt(NUM, inputVal);
                    editor.apply();
                    int val2 =  setting.getInt(NUM , 0);
                   // System.out.println(val2);
                    // ends edited by bilal
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

    }
}
