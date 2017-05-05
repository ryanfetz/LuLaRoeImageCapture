package com.lularoe.erinfetz.imagecapture;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;

import java.io.IOException;

public class BitmapUtils {
    private BitmapUtils(){

    }

    public static Bitmap resizeWidth(Bitmap image, int maxWidth){
        if (maxWidth > 0) {


            double scale = (double) maxWidth / image.getWidth();
            int height = (int) (scale * image.getHeight());

            image = Bitmap.createScaledBitmap(image, maxWidth, height, true);
            return image;
        } else {
            return image;
        }
    }

    public static Bitmap resizeHeight(Bitmap image, int maxHeight){
        if(maxHeight>0){
            double scale = (double) maxHeight / image.getHeight();
            int width = (int) (scale * image.getWidth());

            image = Bitmap.createScaledBitmap(image, width, maxHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight){
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static Bitmap rotateIfRequired(String photoPath, Bitmap bitmap)throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(bitmap, 270);
            default:
                return bitmap;
        }
    }
    public static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
}
