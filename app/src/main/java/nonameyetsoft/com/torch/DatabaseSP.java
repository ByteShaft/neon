package nonameyetsoft.com.torch;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

    // edited by bilal this is a class with method and add one on each click to database.
public class DatabaseSP {

    private final static String NUMBER = "number";

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    //Add one on each button click
    public void addOneToDatabase(Context context) {
        int valueFromDatabase;

    //calling setting manager method
        settings = getSettingsManager(context);
        //creating the values if not exsist in DB calling method
        valueFromDatabase = createDatabaseIfNotExist();
        valueFromDatabase++;

    //saving new value after incrementing it.
        saveSettings(valueFromDatabase);
    }
    // getting prefrence manager method
    private SharedPreferences getSettingsManager(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    // method to create database if not exsist if exsist then return the previous value.
    private int createDatabaseIfNotExist() {

        return settings.getInt(NUMBER, 0);
    }
    //save new value method
    private void saveSettings(int inputValue) {
        editor = settings.edit();
        editor.putInt(NUMBER, inputValue);
        editor.apply();
    }
}
