package app.kiti.com.kitiapp.utils;

import android.util.Log;

/**
 * Created by Ankit on 4/16/2018.
 */

public class ReadingTimeCalculater {

    private final static int wordsPerMinute = 120;

    public static int getSecondsToRead(String text) {

        int noOfWords = text.split(" ").length;
        int secToRead = noOfWords*60 / wordsPerMinute;
        //Log.d("ReadingTime","words: "+noOfWords+" secToRead: "+secToRead);
        return secToRead;

    }
}
