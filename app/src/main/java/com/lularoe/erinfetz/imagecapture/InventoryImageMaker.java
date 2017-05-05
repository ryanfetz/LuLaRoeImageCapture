package com.lularoe.erinfetz.imagecapture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;

import com.google.common.base.Strings;

import java.io.IOException;

public class InventoryImageMaker {
    private static final String TAG = InventoryImageMaker.class.getSimpleName();
    private final Context context;

    static final int PROD_X=1780;
    static final int PROD_Y=40;

    static final int WM_X=40;
    static final int WM_Y=4200;

    static final int SZ_X=40;
    static final int SZ_Y=40;

    static final int SIZE_IMAGE_WIDTH = 800;
    static final int PRICE_IMAGE_WIDTH = 800;
    static final int WATERMARK_IMAGE_WIDTH = 1000;

    public InventoryImageMaker(Context context) {
        this.context = context;
    }

    private Bitmap getImage(int id){

        return BitmapFactory.decodeResource(context.getResources(),id);
    }


    public Bitmap standard(String photoPath, Bitmap bitmap, String style, String size)throws IOException {

        Bitmap im = BitmapUtils.rotateIfRequired(photoPath, bitmap);

        Canvas canvas = new Canvas(im);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

        Bitmap overlay = BitmapUtils.resizeWidth(ProductPriceImages.getInstance(context).get(style), PRICE_IMAGE_WIDTH);

        canvas.drawBitmap(overlay, PROD_X, PROD_Y, paint);

        overlay = null;
        if(!Strings.isNullOrEmpty(size)){
            overlay = ProductSizeBubbles.getInstance(context).create(size);
            if(overlay!=null) {
                canvas.drawBitmap(BitmapUtils.resizeWidth(overlay,SIZE_IMAGE_WIDTH), SZ_X, SZ_Y, paint);
            }
        }
        overlay = getImage(R.drawable.watermark);

        canvas.drawBitmap(BitmapUtils.resizeWidth(overlay,WATERMARK_IMAGE_WIDTH), WM_X, WM_Y, paint);
        return im;
    }
}
