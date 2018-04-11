package app.kiti.com.kitiapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class BannerAdActivity extends AppCompatActivity {

    private AdView mAdView;
    public static final String TAG = BannerAdActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_ad);
        init();
    }

    private void init() {
        MobileAds.initialize(this, getResources().getString(R.string.admob_test_app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(adListener);
    }

    private AdListener adListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            // Code to be executed when an ad finishes loading.
            Log.d(TAG,"Ad Loaded");
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            // Code to be executed when an ad request fails.
            Log.d(TAG,"Failed to Load ad");
        }

        @Override
        public void onAdOpened() {
            // Code to be executed when an ad opens an overlay that
            // covers the screen.
            Log.d(TAG,"Ad Opened");
        }

        @Override
        public void onAdLeftApplication() {
            // Code to be executed when the user has left the app.
        }

        @Override
        public void onAdClosed() {
            // Code to be executed when when the user is about to return
            // to the app after tapping on an ad.
        }
    };

}
