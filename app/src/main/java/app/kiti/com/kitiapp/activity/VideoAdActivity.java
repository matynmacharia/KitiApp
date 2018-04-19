package app.kiti.com.kitiapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import app.kiti.com.kitiapp.R;
import app.kiti.com.kitiapp.firebase.SyncManager;
import app.kiti.com.kitiapp.preference.PreferenceManager;
import app.kiti.com.kitiapp.utils.FirebaseDataField;
import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoAdActivity extends AppCompatActivity implements RewardedVideoAdListener {

    public static final String TAG = VideoAdActivity.class.getSimpleName();
    @BindView(R.id.video_loading_progress)
    ProgressBar videoLoadingProgress;
    @BindView(R.id.video_loading_message)
    TextView videoLoadingMessage;
    @BindView(R.id.loading_progress_card)
    LinearLayout loadingProgress;
    @BindView(R.id.top_banner)
    TextView topBanner;
    @BindView(R.id.earned_message)
    TextView earnedMessage;
    @BindView(R.id.earned_card)
    LinearLayout earnedCard;
    @BindView(R.id.go_back_btn)
    TextView goBackBtn;
    @BindView(R.id.general_message)
    TextView generalMessage;
    @BindView(R.id.adView)
    AdView adView;
    private RewardedVideoAd mRewardedVideoAd;
    private SyncManager syncManager;
    private boolean mCompletedVideo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_ad);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        MobileAds.initialize(this, getResources().getString(R.string.admob_test_app_id));
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        syncManager = new SyncManager();

        loadRewardedVideoAd();
        onVideoAdLoading();
        attachListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initBannerAd();
    }

    private void initBannerAd() {

        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544~3347511713");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }


    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }


    private void onUserSeenFullVideo() {

        long reward = PreferenceManager.getInstance().getVideoRate();

        //hide general message
        if (generalMessage != null) {
            generalMessage.setText(getResources().getString(R.string.message_after_earning_reward));
            generalMessage.setVisibility(View.VISIBLE);
        }

        //hide loading card
        if (loadingProgress != null) {
            loadingProgress.setVisibility(View.GONE);
        }
        //show earned card
        if (earnedCard != null) {
            earnedCard.setVisibility(View.VISIBLE);
        }
        //update earn amount
        if (earnedMessage != null) {
            topBanner.setText("Congratualtions!!!");
            earnedMessage.setText(String.format("You earned : Rs. %d", reward));
        }
        //show go back button
        if (goBackBtn != null) {
            goBackBtn.setVisibility(View.VISIBLE);
        }

        //update database
        syncManager.addBalance(reward);

    }

    private void onUserNotSeenFullVideo() {
        //hide general message
        if (generalMessage != null) {
            generalMessage.setText(getResources().getString(R.string.message_after_earning_reward));
            generalMessage.setVisibility(View.VISIBLE);
        }

        //hide loading card
        if (loadingProgress != null) {
            loadingProgress.setVisibility(View.GONE);
        }
        //show earned card
        if (earnedCard != null) {
            earnedCard.setVisibility(View.VISIBLE);
        }
        //update earn amount
        if (earnedMessage != null) {
            topBanner.setText("Oops!!!");
            earnedMessage.setText(String.format("You have to see the full video to get reward."));
        }
        //show go back button
        if (goBackBtn != null) {
            goBackBtn.setVisibility(View.VISIBLE);
        }

    }

    private void onVideoAdLoading() {

        //show general message
        if (generalMessage != null) {
            generalMessage.setText(String.format(getResources().getString(R.string.general_message_before_video), PreferenceManager.getInstance().getVideoRate()));
            generalMessage.setVisibility(View.GONE);
        }

        //hide earned card
        if (earnedCard != null) {
            earnedCard.setVisibility(View.GONE);
        }
        //show loading card
        if (loadingProgress != null) {
            loadingProgress.setVisibility(View.VISIBLE);
        }

        //hide back button
        if (goBackBtn != null) {
            goBackBtn.setVisibility(View.GONE);
        }

    }

    private void attachListener() {

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void onRewarded(RewardItem reward) {
        onUserSeenFullVideo();
        // Reward the user.
        syncManager.setAdClick(FirebaseDataField.VIDEO_AD);

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        onUserNotSeenFullVideo();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        if (!mCompletedVideo) {
            onUserNotSeenFullVideo();
        }
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        onUserNotSeenFullVideo();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        mRewardedVideoAd.show();

    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(this, "See complete video to get reward.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoCompleted() {
        mCompletedVideo = true;
        Toast.makeText(this, "Congratulations!!! You can now close the Video.", Toast.LENGTH_SHORT).show();
    }


}
