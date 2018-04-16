package app.kiti.com.kitiapp.banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;


/**
 * Created by Ankit on 4/16/2018.
 */

public class CornerRoundTransformation extends BitmapTransformation {

    private int radius;
    private int diameter;
    private int margin;

    public CornerRoundTransformation(Context context, int radius , int margin) {
        super(context);
        this.radius = radius;
        this.diameter = this.radius * 2;
        this.margin = margin;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {

        int width = toTransform.getWidth();
        int height = toTransform.getHeight();

        Bitmap bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setHasAlpha(true);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        drawRoundRect(canvas, paint, width, height);
        return bitmap;

    }

    private void drawRoundRect(Canvas canvas, Paint paint, int width, int height) {

        float right = width - margin;
        float bottom = height - margin;

        canvas.drawRoundRect(new RectF(margin, margin, right, bottom), radius, radius, paint);
    }

    @Override
    public String getId() {
        return "CornerRoundTransformation";
    }
}
