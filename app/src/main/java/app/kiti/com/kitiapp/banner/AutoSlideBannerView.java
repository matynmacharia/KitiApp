package app.kiti.com.kitiapp.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

import app.kiti.com.kitiapp.R;
import app.kiti.com.kitiapp.custom.joke.CustomViewPager;
import app.kiti.com.kitiapp.custom.joke.SlideController;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 4/16/2018.
 */

public class AutoSlideBannerView extends FrameLayout implements SlideController.SlideEventListener {

    private static final double SCROLL_DELAY_FACTOR = 10;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    @BindView(R.id.bubble_view)
    BubbleView bubbleView;

    private Context mContext;

    private ArrayList<String> bannerUrls;
    private SlideController slideController;
    private BannerPagerAdapter bannerPagerAdapter;
    private static final int DEFAULT_IMAGE_SLIDE_DURATION = 3000;

    public AutoSlideBannerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public AutoSlideBannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoSlideBannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        this.mContext = context;
        bannerUrls = new ArrayList<>();
        View view = LayoutInflater.from(context).inflate(R.layout.banner_slider_view, this);
        ButterKnife.bind(this, view);
        viewPager.setScrollDurationFactor(SCROLL_DELAY_FACTOR);
        viewPager.setPageTransformer(false, new PageTransformer());
        slideController = new SlideController();
        slideController.setSlideEventListener(this);
        disableUserSwipeControl(viewPager);

    }

    public void setImageUrls(ArrayList<String> imageUrls) {

        this.bannerUrls = imageUrls;
        bannerPagerAdapter = new BannerPagerAdapter(mContext);
        bannerPagerAdapter.setImageUrls(imageUrls);
        viewPager.setAdapter(bannerPagerAdapter);

        ArrayList<Integer> times = getScrollingTimes(imageUrls.size());
        slideController.setIntervalOfTick(1000);
        slideController.setTimeIntervals(times);

        //init bubbles
        bubbleView.setBubbleCount(imageUrls.size());
        bubbleView.init();

        if (slideController != null) {
            slideController.stop();
        }
        // we can now start sliding
        slideController.start();

    }

    private ArrayList<Integer> getScrollingTimes(int size) {
        ArrayList<Integer> times = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            times.add(DEFAULT_IMAGE_SLIDE_DURATION);
        }
        return times;

    }

    public void cancelSliding() {
        slideController.stop();
    }

    @Override
    public void changeSlideTo(int slideNumber) {
        viewPager.setCurrentItem(slideNumber);
        //change bubble
        bubbleView.setSelectionIndex(slideNumber);
    }

    @Override
    public void progressChangedTo(long timeLeftInMillis, long progressPercent) {
        // in future, use this to change bubbles
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


}
