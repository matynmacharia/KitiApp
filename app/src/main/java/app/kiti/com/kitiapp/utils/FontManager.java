package app.kiti.com.kitiapp.utils;

import android.content.Context;
import android.graphics.Typeface;

import app.kiti.com.kitiapp.main.KitiAppMain;

public class FontManager {

    public static final String FONT_AWESOME = "font.ttf";

    private static Context context;
    private static FontManager mInstance;

    public FontManager() {
        FontManager.context = KitiAppMain.getContext();
    }

    public static FontManager getInstance() {
        if (mInstance == null) {
            mInstance = new FontManager();
        }
        return mInstance;
    }

    public Typeface getTypeFace() {
        return Typeface.createFromAsset(context.getAssets(), FONT_AWESOME);
    }

}
