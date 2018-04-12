package app.kiti.com.kitiapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import app.kiti.com.kitiapp.R;
import app.kiti.com.kitiapp.firebase.SyncManager;
import app.kiti.com.kitiapp.utils.FirebaseDataField;

public class InterstitalAdActivity extends AppCompatActivity {

    public static final String TAG = InterstitalAdActivity.class.getSimpleName();
    private InterstitialAd mInterstitialAd;
    private SyncManager syncManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstital_ad);
        init();
        showAd();
    }

    private void showAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }

    private void init() {

        MobileAds.initialize(this, getResources().getString(R.string.admob_test_app_id));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(adListener);
        syncManager = new SyncManager();

    }

    private AdListener adListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            // Code to be executed when an ad finishes loading.
            Log.d(TAG, "Ad Loaded");
            mInterstitialAd.show();
        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
            // Code to be executed when an ad request fails.
            Log.d(TAG, "Failed to Load ad");
        }

        @Override
        public void onAdOpened() {
            // Code to be executed when an ad opens an overlay that
            // covers the screen.
            Log.d(TAG, "Ad Opened");
            syncManager.setAdClick(FirebaseDataField.INTERSTITAL_AD);

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
