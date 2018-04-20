package app.kiti.com.kitiapp.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import app.kiti.com.kitiapp.R;

/**
 * Created by Ankit on 4/16/2018.
 */

public class BannerPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> imageUrls;
    private int size;

    public BannerPagerAdapter(Context context) {
        mContext = context;
        imageUrls = new ArrayList<>();
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
        this.size = imageUrls.size();
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.banner_image_view, container, false);
        ImageView bannerIv = view.findViewById(R.id.bannerIv);
        loadImage(bannerIv, imageUrls.get(position));
        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private void loadImage(ImageView target, String url) {

        Glide.with(mContext)
                .load(url)
                .placeholder(R.drawable.banner_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(target);


    }
}