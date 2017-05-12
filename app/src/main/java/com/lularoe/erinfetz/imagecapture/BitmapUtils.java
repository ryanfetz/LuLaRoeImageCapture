package com.lularoe.erinfetz.imagecapture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.widget.ImageView;

import com.lularoe.erinfetz.core.storage.files.StoredFile;

import java.io.IOException;

public class BitmapUtils {
    private BitmapUtils(){

    }

    public static Bitmap loadForView(ImageView imageView, StoredFile file){
        /* Get the size of the ImageView */
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        /* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getFile().getAbsolutePath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        /* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        /* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(file.getFile().getAbsolutePath(), bmOptions);
        return bitmap;
    }


//    public static Bitmap rotateIfRequired(String photoPath, Bitmap bitmap)throws IOException {
//        ExifInterface ei = new ExifInterface(photoPath);
//        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//        switch (orientation) {
//            case ExifInterface.ORIENTATION_ROTATE_90:
//                return rotateImage(bitmap, 90);
//            case ExifInterface.ORIENTATION_ROTATE_180:
//                return rotateImage(bitmap, 180);
//            case ExifInterface.ORIENTATION_ROTATE_270:
//                return rotateImage(bitmap, 270);
//            default:
//                return bitmap;
//        }
//    }

}
