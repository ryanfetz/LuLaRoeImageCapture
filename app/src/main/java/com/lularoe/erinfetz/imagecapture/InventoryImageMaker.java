package com.lularoe.erinfetz.imagecapture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.google.common.base.Strings;
import com.lularoe.erinfetz.core.storage.files.StoredFile;

import java.io.File;
import java.io.FileOutputStream;
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

    public void createStandardImage(StoredFile input, String currentProductStyle, String currentProductSize){

        FileOutputStream outStream=null;
        try{
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inMutable=true;
            Bitmap bitmap = BitmapFactory.decodeFile(input.getFile().getAbsolutePath(), bmOptions);

            bitmap = this.standard(input.getFile().getAbsolutePath(), bitmap, currentProductStyle, currentProductSize);

            File outFile = input.getFile();

            if(outFile.exists()){
                outFile.delete();
            }

            outStream = new FileOutputStream(outFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

        }catch(IOException e){
            Log.e(TAG, e.getMessage(),e);
        }finally{
            try{
                if(outStream!=null){
                    outStream.flush();
                    outStream.close();
                }
            }catch(IOException e1){

                Log.e(TAG, e1.getMessage(),e1);
            }
        }
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
