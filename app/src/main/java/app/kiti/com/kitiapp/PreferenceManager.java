package app.kiti.com.kitiapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Ankit on 4/11/2018.
 */

public class PreferenceManager {

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static PreferenceManager mInstance;
    private static final String PREF_NAME = "kiti_pref";

    public PreferenceManager() {

        preferences = KitiAppMain.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

    }

    public static PreferenceManager getInstance() {
        if (mInstance == null) {
            mInstance = new PreferenceManager();
        }
        return mInstance;
    }

    public void clearPreferences() {
        editor.clear();
        editor.apply();
    }

    public void setLoggedIn(boolean loggedIn) {
        editor.putBoolean("isLoggedIn", loggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean("isLoggedIn", false);
    }

}
