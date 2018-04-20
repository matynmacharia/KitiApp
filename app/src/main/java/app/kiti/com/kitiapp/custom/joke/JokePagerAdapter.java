package app.kiti.com.kitiapp.custom.joke;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.kiti.com.kitiapp.R;

/**
 * Created by Ankit on 4/15/2018.
 */

public class JokePagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<String> jokes;
    int size;

    public JokePagerAdapter(Context context) {
        mContext = context;
        jokes = new ArrayList<>();
    }

    public void setJokes(ArrayList<String> jokes) {
        this.jokes = jokes;
        this.size = jokes.size();
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.joke_text_view, container, false);
        TextView jokeTv = view.findViewById(R.id.joke_tv);
        jokeTv.setText(jokes.get(position));
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

}
