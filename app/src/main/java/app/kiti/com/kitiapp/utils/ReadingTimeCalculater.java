package app.kiti.com.kitiapp.utils;

import android.util.Log;

/**
 * Created by Ankit on 4/16/2018.
 */

public class ReadingTimeCalculater {

    private final static int wordsPerMinute = 120;
    private final static int MIN_TIME_TO_READ = 8; // 8 seconds

    public static int getSecondsToRead(String text) {

        int noOfWords = text.split(" ").length;
        int secToRead = noOfWords * 60 / wordsPerMinute;
        //Log.d("ReadingTime","words: "+noOfWords+" secToRead: "+secToRead);
        int minTime = secToRead >= MIN_TIME_TO_READ ? secToRead : MIN_TIME_TO_READ;
        return minTime;

    }
}
