package nonameyetsoft.com.torch;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
// edited by bilal this is a class with method and add one on each click to database.
public class DatabaseSP extends Activity {

    public static final String NUM = "number";
    public static int counter;

    public void onClickAddOne(Context context) {
        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(context);
        // working with  shared preference  edited by bilal
        int val =  setting.getInt(NUM , 0);
        if (val == 0) {
            counter = val+1;
        } else {
            counter = val+1;
        }
        SharedPreferences.Editor  editor;
        editor = setting.edit();
        int inputVal = counter ;
        editor.putInt(NUM, inputVal);
        editor.apply();
        int val2 =  setting.getInt(NUM , 0);
        //System.out.println(val2);
        // ends
    }
}
