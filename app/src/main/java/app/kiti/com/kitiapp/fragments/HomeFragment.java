package app.kiti.com.kitiapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.kiti.com.kitiapp.R;
import app.kiti.com.kitiapp.activity.OptionsActivity;
import app.kiti.com.kitiapp.banner.AutoSlideBannerView;
import app.kiti.com.kitiapp.custom.joke.AutoSlideJokeView;
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

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        addJokes();
        addBannerImages();
        attachListeners();
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

    private void addJokes() {

        ArrayList<String> jokes = new ArrayList<>();
        jokes.add(mContext.getResources().getString(R.string.joke1));
        jokes.add(mContext.getResources().getString(R.string.joke2));
        jokes.add(mContext.getResources().getString(R.string.joke3));
        jokes.add(mContext.getResources().getString(R.string.joke4));
        jokes.add(mContext.getResources().getString(R.string.joke5));
        jokes.add(mContext.getResources().getString(R.string.joke1));
        jokes.add(mContext.getResources().getString(R.string.joke2));
        jokes.add(mContext.getResources().getString(R.string.joke3));
        jokes.add(mContext.getResources().getString(R.string.joke4));
        jokes.add(mContext.getResources().getString(R.string.joke5));
        jokes.add(mContext.getResources().getString(R.string.joke1));
        jokes.add(mContext.getResources().getString(R.string.joke2));
        jokes.add(mContext.getResources().getString(R.string.joke3));
        jokes.add(mContext.getResources().getString(R.string.joke4));
        jokes.add(mContext.getResources().getString(R.string.joke5));
        //set to view
        autoSlideJokeView.setJokes(jokes);

    }

    private void addBannerImages() {

        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://www.chitramala.in/wp-content/uploads/2014/09/kajal-agarwal-success-secret.jpg");
        imageUrls.add("http://imgcdn.raagalahari.com/dec2012/hd/special-chabbis-heroine-kajal-aggarwal/special-chabbis-heroine-kajal-aggarwal6.jpg");
        imageUrls.add("http://www.indiancinemagallery.com/gallery/kajal-agarwal/Kajal-Agarwal-stills-from-Jilla-movie-(4)3824.JPG");
        imageUrls.add("https://www.chitramala.in/wp-content/uploads/2014/09/kajal-agarwal-success-secret.jpg");
        imageUrls.add("http://imgcdn.raagalahari.com/dec2012/hd/special-chabbis-heroine-kajal-aggarwal/special-chabbis-heroine-kajal-aggarwal6.jpg");
        imageUrls.add("http://www.indiancinemagallery.com/gallery/kajal-agarwal/Kajal-Agarwal-stills-from-Jilla-movie-(4)3824.JPG");
        imageUrls.add("https://www.chitramala.in/wp-content/uploads/2014/09/kajal-agarwal-success-secret.jpg");
        imageUrls.add("http://imgcdn.raagalahari.com/dec2012/hd/special-chabbis-heroine-kajal-aggarwal/special-chabbis-heroine-kajal-aggarwal6.jpg");
        imageUrls.add("http://www.indiancinemagallery.com/gallery/kajal-agarwal/Kajal-Agarwal-stills-from-Jilla-movie-(4)3824.JPG");
        imageUrls.add("https://www.chitramala.in/wp-content/uploads/2014/09/kajal-agarwal-success-secret.jpg");
        imageUrls.add("http://imgcdn.raagalahari.com/dec2012/hd/special-chabbis-heroine-kajal-aggarwal/special-chabbis-heroine-kajal-aggarwal6.jpg");
        imageUrls.add("http://www.indiancinemagallery.com/gallery/kajal-agarwal/Kajal-Agarwal-stills-from-Jilla-movie-(4)3824.JPG");
        imageUrls.add("https://www.chitramala.in/wp-content/uploads/2014/09/kajal-agarwal-success-secret.jpg");
        imageUrls.add("http://imgcdn.raagalahari.com/dec2012/hd/special-chabbis-heroine-kajal-aggarwal/special-chabbis-heroine-kajal-aggarwal6.jpg");
        imageUrls.add("http://www.indiancinemagallery.com/gallery/kajal-agarwal/Kajal-Agarwal-stills-from-Jilla-movie-(4)3824.JPG");

        bannerSlider.setImageUrls(imageUrls);

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
