package com.lularoe.erinfetz.core.media;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class BitmapUtils {
    public static final String TAG = BitmapUtils.class.getSimpleName();

    private BitmapUtils() {

    }

    @Nullable
    public static Bitmap decodeSampledBitmap(String inputFile, int requestedWidth, int requestedHeight,
                                             @Degrees.DegreeUnits int rotationInDegrees) {
        final BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(inputFile, opts);

        opts.inSampleSize = calculateInSampleSize(opts, requestedWidth, requestedHeight, rotationInDegrees);
        opts.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(inputFile, opts);
    }

    @Nullable
    public static Bitmap decodeSampledBitmap(String inputFile, int requestedWidth, int requestedHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(inputFile, options);

        options.inSampleSize = calculateInSampleSize(options, requestedWidth, requestedHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(inputFile, options);
    }

    public static int calculateInSampleSize(@NonNull BitmapFactory.Options options, int reqWidth, int reqHeight,
                                            @Degrees.DegreeUnits int rotationInDegrees) {

        // Raw height and width of image
        final int height;
        final int width;
        int inSampleSize = 1;

        // Check for rotation
        if (rotationInDegrees == Degrees.DEGREES_90 || rotationInDegrees == Degrees.DEGREES_270) {
            width = options.outHeight;
            height = options.outWidth;
        } else {
            height = options.outHeight;
            width = options.outWidth;
        }

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static int calculateInSampleSize(@NonNull BitmapFactory.Options options,
                                            int requestedWidth, int requestedHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > requestedHeight || width > requestedWidth) {

            final int halfHeight = height / inSampleSize;
            final int halfWidth = width / inSampleSize;

            while ((halfHeight / inSampleSize) > requestedHeight
                    && (halfWidth / inSampleSize) > requestedWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}