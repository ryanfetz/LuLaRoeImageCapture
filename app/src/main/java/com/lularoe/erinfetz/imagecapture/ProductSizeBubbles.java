package com.lularoe.erinfetz.imagecapture;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.Random;

public class ProductSizeBubbles {
    private static final String TAG = ProductSizeBubbles.class.getSimpleName();

    //private static final int FONT_SIZE = 174;
    private static final int FONT_SIZE = 232;

    private static ProductSizeBubbles INSTANCE;
    private final Random random;
    private final Context context;

    private static final int[] COLORS = new int[]{
        R.drawable.blue,
        R.drawable.fuchsia,
        R.drawable.green,
        R.drawable.grey,
        R.drawable.orange,
        R.drawable.pink,
        R.drawable.purple,
        R.drawable.yellow
    };

    private ProductSizeBubbles(Context context) {

        this.context = context;
        random = new Random(1);
    }

    public static ProductSizeBubbles getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ProductSizeBubbles(context);
        }
        return INSTANCE;
    }

    public Bitmap create(String size){
        Resources resources = context.getResources();

        Typeface font = FontManager.getInstance(context)
                .getTypeface(FontManager.MUSEO_SANS_500, Typeface.BOLD);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                random.nextInt(COLORS.length));

        float scale = resources.getDisplayMetrics().density;

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.BLACK);
        // text size in pixels
        paint.setTextSize((int) (14 * scale));

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(size, 0, size.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width())/2;
        int y = (bitmap.getHeight() + bounds.height())/2;

        canvas.drawText(size, x, y, paint);

        return bitmap;
    }
}
