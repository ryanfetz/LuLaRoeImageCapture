package com.lularoe.erinfetz.imagecapture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.google.common.base.Strings;

public class InventoryImageMaker {
    private static final String TAG = InventoryImageMaker.class.getSimpleName();
    private final Context context;

    static final int PROD_X=1780;
    static final int PROD_Y=40;

    static final int WM_X=40;
    static final int WM_Y=4200;

    static final int SZ_X=40;
    static final int SZ_Y=40;

    public InventoryImageMaker(Context context) {
        this.context = context;
    }

    private Bitmap getImage(int id){
        return BitmapFactory.decodeResource(context.getResources(),id);
    }
    public Bitmap standard(Bitmap bitmap, String style, String size){

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

        Bitmap overlay = ProductPriceImages.getInstance(context).get(style);

        canvas.drawBitmap(overlay, PROD_X, PROD_Y, paint);

        overlay = null;
        if(!Strings.isNullOrEmpty(size)){
            overlay = ProductSizeBubbles.getInstance(context).create(size);
            canvas.drawBitmap(overlay, SZ_X, SZ_Y, paint);
        }
        overlay = getImage(R.drawable.watermark);

        canvas.drawBitmap(overlay, WM_X, WM_Y, paint);
        return bitmap;
    }
}
