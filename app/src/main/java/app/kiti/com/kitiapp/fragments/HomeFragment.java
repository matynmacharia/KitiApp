package app.kiti.com.kitiapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import app.kiti.com.kitiapp.R;
import app.kiti.com.kitiapp.activity.OptionsActivity;
import app.kiti.com.kitiapp.banner.AutoSlideBannerView;
import app.kiti.com.kitiapp.custom.joke.AutoSlideJokeView;
import app.kiti.com.kitiapp.firebase.SyncManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment {

    @BindView(R.id.auto_slide_joke_view)
    AutoSlideJokeView autoSlideJokeView;
    Unbinder unbinder;
    @BindView(R.id.bannerSlider)
    AutoSlideBannerView bannerSlider;
    @BindView(R.id.jokeCaption)
    TextView jokeCaption;
    @BindView(R.id.start_earning_btn)
    TextView startEarningBtn;
    private Context mContext;

    private SyncManager syncManager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        syncManager = new SyncManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        attachListeners();
        synJokes();
        syncFeaturedImages();

    }

    private void attachListeners() {
        startEarningBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoOptionsPage();
            }
        });
    }

    private void gotoOptionsPage() {
        Intent i = new Intent(mContext, OptionsActivity.class);
        mContext.startActivity(i);
    }

    private void showDefaultJokes() {

        ArrayList<String> jokes = new ArrayList<>();
        String[] default_jokes = mContext.getResources().getStringArray(R.array.jokes);
        for (int i = 0; i < default_jokes.length; i++) {
            jokes.add(default_jokes[i]);
        }
        //set to view
        autoSlideJokeView.setJokes(jokes);

    }

    private void showDefaultBanner() {

        ArrayList<String> imageUrls = new ArrayList<>();
        String[] default_banners = mContext.getResources().getStringArray(R.array.featured_image);
        for (int i = 0; i < default_banners.length; i++) {
            imageUrls.add(default_banners[i]);
        }
        bannerSlider.setImageUrls(imageUrls);

    }

    private void synJokes() {

        showDefaultJokes();

        syncManager.getJokesNodeRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    collectJokes((Map<String, Object>) dataSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void syncFeaturedImages() {

        showDefaultBanner();

        syncManager.getFeaturedContentNodeRef()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            collectFeaturedContent((Map<String, Object>) dataSnapshot.getValue());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void collectFeaturedContent(Map<String, Object> map) {

        ArrayList<String> images = new ArrayList<>();
        if (map != null) {
            for (Map.Entry<String, Object> objectEntry : map.entrySet()) {
                images.add((String) objectEntry.getValue());
            }
            //set to view
            bannerSlider.setImageUrls(images);
        }
    }

    private void collectJokes(Map<String, Object> map) {

        ArrayList<String> jokes = new ArrayList<>();
        if (map != null) {
            for (Map.Entry<String, Object> objectEntry : map.entrySet()) {
                Log.d("HomeFragment", "entry " + objectEntry.toString());
                jokes.add((String) objectEntry.getValue());
            }
            autoSlideJokeView.setJokes(jokes);
        }
        //set to view

    }

    @Override
    public void onPause() {
        super.onPause();
        autoSlideJokeView.cancelSliding();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
