package app.kiti.com.kitiapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import app.kiti.com.kitiapp.R;
import app.kiti.com.kitiapp.scheduler.Scheduler;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OptionsActivity extends AppCompatActivity {

    @BindView(R.id.banner)
    Button banner;
    @BindView(R.id.interstital)
    Button interstital;
    @BindView(R.id.video)
    Button video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toBanner();
            }
        });
        interstital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toInterst();
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toVideoAd();
            }
        });

        testScheduler();

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

    private void testScheduler(){
        Scheduler scheduler = new Scheduler(this);
        scheduler.shchedule("test");
    }


}
