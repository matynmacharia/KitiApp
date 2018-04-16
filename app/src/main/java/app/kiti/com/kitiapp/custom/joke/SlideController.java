package app.kiti.com.kitiapp.custom.joke;

import android.os.CountDownTimer;
import android.util.Log;

/**
 * Created by Ankit on 4/15/2018.
 */

public class SlideController {

    //time are in millis
    int[] timeIntervals = {1000, 2000, 3000};
    long intervalOfTick = 10; // 10 millis
    int currentPlayingIndex = 0;
    // this is the total amount of progress bar
    // progress will have 0 - 1000 step
    public static final int PROGRESS_FACTOR = 1000;

    private CountDownTimer countDownTimer;
    private long timeElapsed;
    private long progressPercent;

    public SlideController() {
    }

    public void setTimeIntervals(int[] timeIntervals) {
        this.timeIntervals = timeIntervals;
    }

    public void setIntervalOfTick(long intervalOfTick) {
        this.intervalOfTick = intervalOfTick;
    }

    public void start() {
        //check
        if (timeIntervals.length > 0 && intervalOfTick >= 10) {
            playFor(0);
        } else {
            throw new IllegalArgumentException("Invalid slide info. Provide tickInterval>=10 and atleast one timeIntervals");
        }
    }

    public void stop() {
        if (countDownTimer != null) {
            sendStopEvent();
            countDownTimer.cancel();
        }
    }

    private void playFor(int index) {

        currentPlayingIndex = index;

        //send slide number to show
        sendSlideNumber(index);
        countDownTimer = new CountDownTimer(timeIntervals[index], intervalOfTick) {
            @Override
            public void onTick(long millisUntilFinished) {
                sendProgressChanged(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                //finished
                //start again
                int nextIndex = getNextIndexToPlay(currentPlayingIndex);
                playFor(nextIndex);

            }
        }.start();

    }

    private void sendSlideNumber(int index) {
        if (slideEventListener != null) {
            slideEventListener.changeSlideTo(index);
        }
    }

    private void sendProgressChanged(long millisUntilFinished) {
        if (slideEventListener != null) {
            timeElapsed = timeIntervals[currentPlayingIndex] - millisUntilFinished;
            progressPercent = (1000 * timeElapsed) / timeIntervals[currentPlayingIndex];
            slideEventListener.progressChangedTo(millisUntilFinished, progressPercent);
        }
    }

    private void sendStopEvent() {
        if (slideEventListener != null) {
            slideEventListener.onSlideStopped();
        }
    }

    private int getNextIndexToPlay(int currentPlayingIndex) {
        if (currentPlayingIndex == (timeIntervals.length - 1)) {
            //start from 0
            return 0;
        }
        //else play next
        return currentPlayingIndex + 1;
    }

    private SlideEventListener slideEventListener;

    public void setSlideEventListener(SlideEventListener slideEventListener) {
        this.slideEventListener = slideEventListener;
    }

    public interface SlideEventListener {

        void changeSlideTo(int slideNumber);

        void progressChangedTo(long elapsedTime, long percent);

        void onSlideStopped();

    }

}
