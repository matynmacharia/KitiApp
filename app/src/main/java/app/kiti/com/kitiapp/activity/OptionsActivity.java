package app.kiti.com.kitiapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import app.kiti.com.kitiapp.R;
import app.kiti.com.kitiapp.firebase.SyncManager;
import app.kiti.com.kitiapp.preference.PreferenceManager;
import app.kiti.com.kitiapp.utils.TimeUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OptionsActivity extends AppCompatActivity {

    SyncManager syncManager;
    @BindView(R.id.gotoVideoBtn)
    TextView gotoVideoBtn;
    @BindView(R.id.refresh_btn)
    TextView refreshBtn;
    @BindView(R.id.earning_instruction)
    TextView earningInstruction;
    @BindView(R.id.adView)
    AdView adView;
    private String mLastViewedAt;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        ButterKnife.bind(this);
        syncManager = new SyncManager();

        attachListener();

    }

    private void attachListener() {

        gotoVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toVideoAd();
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshHurdleInfo();
            }
        });

    }

    private void refreshHurdleInfo() {
        disableWithMessage("Syncing...");
        syncVideoButtonState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshHurdleInfo();
        initBannerAd();
    }

    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }

    private void initBannerAd() {

        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544~3347511713");

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    private void toVideoAd() {
        Intent i = new Intent(this, VideoAdActivity.class);
        startActivity(i);
        finish();
    }

    private void syncVideoButtonState() {

        syncManager
                .getNextVideoSeenTimeNodeRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String time = (String) dataSnapshot.getValue();
                        if (time.length() == 0) {
                            notViewedYet();
                        } else {
                            lastViewedAt(time);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void lastViewedAt(String time) {
        mLastViewedAt = time;
        //invalidate button
        //invalidateButton();
        long remaining_millis = TimeUtils.getMillisFrom(mLastViewedAt) - TimeUtils.getMillisFrom(TimeUtils.getTime());
        if (gotoVideoBtn != null) {
            startTimer(remaining_millis);
        }

    }

    private void disableWithMessage(String relativeTimeString) {

        if (gotoVideoBtn != null) {
            gotoVideoBtn.setText(String.format("You can earn : %s", relativeTimeString));
            gotoVideoBtn.setEnabled(false);
        }
    }

    private void enableButton() {

        if (gotoVideoBtn != null) {
            gotoVideoBtn.setText(String.format("Earn Rs. %d", PreferenceManager.getInstance().getVideoRate()));
            gotoVideoBtn.setEnabled(true);
        }
    }

    private void updateButtonText(String text) {
        if (gotoVideoBtn != null) {
            gotoVideoBtn.setText(String.format("You can Earn : In %s", text));
        }

    }

    private void notViewedYet() {
        enableButton();
    }

    private void startTimer(long remaining_millis) {

        countDownTimer = new CountDownTimer(remaining_millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //update button
                updateButtonText(getFormattedTime(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                //enable button
                enableButton();
            }
        }.start();

    }

    @SuppressLint("DefaultLocale")
    private String getFormattedTime(long timeLeftInMillis) {
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


}
