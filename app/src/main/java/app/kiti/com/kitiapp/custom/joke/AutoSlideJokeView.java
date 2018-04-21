package app.kiti.com.kitiapp.custom.joke;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import app.kiti.com.kitiapp.R;
import app.kiti.com.kitiapp.utils.ReadingTimeCalculater;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 4/15/2018.
 */

public class AutoSlideJokeView extends FrameLayout implements SlideController.SlideEventListener {


    private static final double SCROLL_DELAY_FACTOR = 10;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.count_down_text)
    TextView countDownText;
    private Context mContext;

    private ArrayList<String> jokes;
    private SlideController slideController;
    private JokePagerAdapter jokePagerAdapter;

    public AutoSlideJokeView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public AutoSlideJokeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoSlideJokeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        this.mContext = context;
        jokes = new ArrayList<>();

        View view = LayoutInflater.from(context).inflate(R.layout.joke_slider_view, this);
        ButterKnife.bind(this, view);

        viewPager.setScrollDurationFactor(SCROLL_DELAY_FACTOR);
        slideController = new SlideController();
        slideController.setSlideEventListener(this);
        disableUserSwipeControl(viewPager);

    }

    public void setJokes(ArrayList<String> jokes) {

        this.jokes = jokes;
        jokePagerAdapter = new JokePagerAdapter(mContext);
        jokePagerAdapter.setJokes(jokes);
        viewPager.setAdapter(jokePagerAdapter);
        //set fix time for now
        ArrayList<Integer> time = getReadingTimes(jokes);
        slideController.setIntervalOfTick(10);
        slideController.setTimeIntervals(time);
        
        // we can now start sliding
        if (slideController != null) {
            slideController.stop();
        }
        // we can now start sliding
        slideController.start();
    }


    private ArrayList<Integer> getReadingTimes(ArrayList<String> jokes) {
        //times should be in millis
        int size = jokes.size();
        ArrayList<Integer> times = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            times.add(ReadingTimeCalculater.getSecondsToRead(jokes.get(i)) * 1000);
        }

        return times;
    }

    public void cancelSliding() {
        slideController.stop();
    }

    @Override
    public void changeSlideTo(int slideNumber) {
        viewPager.setCurrentItem(slideNumber);
    }

    @Override
    public void progressChangedTo(long timeLeftInMillis, long progressPercent) {
        //   Log.d("AutoSlideView","progress "+progressPercent);
        if (countDownText != null) {
            countDownText.setText(getTime(timeLeftInMillis));
        }
        if (progressBar != null) {
            progressBar.setProgress((int) progressPercent);
        }
    }

    @SuppressLint("DefaultLocale")
    private String getTime(long timeLeftInMillis) {
        //handles 2 cases(we will not have jokes beyond 1 hrs)
        // 1- 1 Min 10 Sec for(secs > 60)
        // 2- 30 sec for (secs<=60)
        int inSecond = (int) (timeLeftInMillis / 1000);
        int min = inSecond / 60;
        int sec = inSecond % 60;
        if (inSecond > 60) {
            return String.format("%d Min %d Sec", min, sec);
        } else {
            return String.format("%d Sec", inSecond);
        }
    }

    @Override
    public void onSlideStopped() {

    }

    private void disableUserSwipeControl(CustomViewPager viewPager) {
        viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    public void startSliding() {
        if (slideController != null) {
            slideController.start();
        }
    }
}
