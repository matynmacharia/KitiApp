package app.kiti.com.kitiapp.main;

import android.app.Application;
import android.content.Context;

/**
 * Created by Ankit on 4/11/2018.
 */

public class KitiAppMain extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
    }

    public static Context getContext() {
        return context;
    }

}
