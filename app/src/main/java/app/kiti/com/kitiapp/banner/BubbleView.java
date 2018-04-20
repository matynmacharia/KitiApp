package app.kiti.com.kitiapp.banner;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.kiti.com.kitiapp.R;
import app.kiti.com.kitiapp.utils.FontManager;
import app.kiti.com.kitiapp.utils.PixelUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 4/20/2018.
 */

public class BubbleView extends FrameLayout {

    @BindView(R.id.container)
    LinearLayout container;
    private Context mContext;
    private int mBubbleCount;

    public static final String bubble_text = "\uF111";
    private int mLastSelection = 0;

    public BubbleView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public BubbleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BubbleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.bubble_view, this);
        ButterKnife.bind(this, view);

    }

    public void setBubbleCount(int bubbleCount) {
        this.mBubbleCount = bubbleCount;
    }

    public void setSelectionIndex(int index) {

        resetLastSelection();
        TextView target = (TextView) container.getChildAt(index);
        target.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        this.mLastSelection = index;

    }

    private void resetLastSelection() {
        TextView target = (TextView) container.getChildAt(mLastSelection);
        target.setTextColor(Color.parseColor("#ffffff"));
    }

    public void init() {

        if (mBubbleCount > 1) {
            addBubbles();
        } else {
            throw new IllegalArgumentException("There should be atleast two bubble!");
        }

        setSelectionIndex(0);

    }

    private void addBubbles() {

        for (int i = 0; i < mBubbleCount; i++) {

            TextView textView = new TextView(mContext);
            textView.setText(bubble_text);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
            textView.setTextColor(Color.parseColor("#ffffff"));
            int padding = PixelUtils.dpToPx(2);
            textView.setPadding(padding, padding, padding, padding);
            textView.setTypeface(FontManager.getInstance().getTypeFace());
            container.addView(
                    textView,
                    i,
                    new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
            );

        }
    }

}
