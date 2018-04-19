package app.kiti.com.kitiapp.activity;

import android.content.Intent;
import android.os.Bundle;
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
        invalidateButton();
    }

    private void invalidateButton() {
        if (TimeUtils.isDateTimePast(mLastViewedAt)) {
            enableButton();
        } else {
            disableWithMessage(TimeUtils.getRelativeTime(mLastViewedAt));
        }
    }

    private void disableWithMessage(String relativeTimeString) {

        gotoVideoBtn.setText(String.format("You can Earn : %s", relativeTimeString));
        gotoVideoBtn.setEnabled(false);

    }

    private void enableButton() {

        gotoVideoBtn.setText(String.format("Earn Rs. %d", PreferenceManager.getInstance().getVideoRate()));
        gotoVideoBtn.setEnabled(true);

    }

    private void notViewedYet() {
        enableButton();
    }

}
