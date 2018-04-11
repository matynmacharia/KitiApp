package app.kiti.com.kitiapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toVideoAd();
    }

    private void toBanner() {
        Intent i = new Intent(this, BannerAdActivity.class);
        startActivity(i);
    }

    private void toInterst() {
        Intent i = new Intent(this, InterstitalAdActivity.class);
        startActivity(i);
    }

    private void toVideoAd() {
        Intent i = new Intent(this, VideoAdActivity.class);
        startActivity(i);
    }


}
