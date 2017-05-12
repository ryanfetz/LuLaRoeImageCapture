package com.lularoe.erinfetz.core.media;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class BitmapTransformations {
    public static final String TAG = BitmapTransformations.class.getSimpleName();

    private BitmapTransformations(){

    }

    public static interface BitmapTransformation{
        Bitmap apply(@NonNull Bitmap image);
    }

    public static interface ImageFileTransformation{
        Bitmap apply(@NonNull String imagePath);
    }

    public static ImageFileTransformation then( @NonNull final ImageFileTransformation first,
                                                @NonNull final BitmapTransformation second){
        return new ImageFileTransformation(){

            @Override
            public Bitmap apply(@NonNull String imagePath) {
                Bitmap i = first.apply(imagePath);

                return second.apply(i);
            }
        };
    }

    public static ImageFileTransformation loadRotatedBitmap(final int requestedWidth, final int requestedHeight){
        return new ImageFileTransformation(){

            @Override
            public Bitmap apply(@NonNull String imagePath) {
                return getRotatedBitmap(imagePath, requestedWidth, requestedHeight);
            }
        };
    }


    public static ImageFileTransformation rotatedBitmap(final int requestedWidth, final int requestedHeight){
        return new ImageFileTransformation(){

            @Override
            public Bitmap apply(@NonNull String imagePath) {
                return rotatedBitmap(imagePath, requestedWidth, requestedHeight);
            }
        };
    }

    @Nullable
    public static Bitmap rotatedBitmap(String inputFile, int requestedWidth, int requestedHeight) {
        Bitmap decodedBitmap = BitmapUtils.decodeSampledBitmap(inputFile, requestedWidth, requestedHeight);

        ExifOrientation eo = ExifOrientation.get(inputFile);

        decodedBitmap = applyMatrix(decodedBitmap, eo.getMatrix());

        return decodedBitmap;
    }

    @Nullable
    public static Bitmap getRotatedBitmap(String inputFile, int requestedWidth, int requestedHeight) {
        final int rotationInDegrees = ExifOrientation.getExifOrientationDegrees(inputFile);

        final Bitmap origBitmap = BitmapUtils.decodeSampledBitmap(inputFile, requestedWidth, requestedHeight,
                rotationInDegrees);

        if (origBitmap == null)
            return null;

        Matrix matrix = new Matrix();
        matrix.preRotate(rotationInDegrees);
        // we need not check if the rotation is not needed, since the below function will then return the same bitmap.
        // Thus no memory loss occurs.

        return Bitmap.createBitmap(origBitmap, 0, 0, origBitmap.getWidth(), origBitmap.getHeight(), matrix, true);
    }

    public static BitmapTransformation resizeWidth(final int maxWidth){
        return new BitmapTransformation(){

            @Override
            public Bitmap apply(@NonNull Bitmap image) {
                return resizeWidth(image, maxWidth);
            }
        };
    }
    public static BitmapTransformation resizeHeight(final int maxHeight){
        return new BitmapTransformation(){

            @Override
            public Bitmap apply(@NonNull Bitmap image) {
                return resizeHeight(image, maxHeight);
            }
        };
    }
    public static BitmapTransformation resize(final int maxWidth, final int maxHeight){
        return new BitmapTransformation(){

            @Override
            public Bitmap apply(@NonNull Bitmap image) {
                return resize(image, maxWidth, maxHeight);
            }
        };
    }

    public static Bitmap resizeWidth(@NonNull Bitmap image, int maxWidth){
        if (maxWidth > 0) {


            double scale = (double) maxWidth / image.getWidth();
            int height = (int) (scale * image.getHeight());

            image = Bitmap.createScaledBitmap(image, maxWidth, height, true);
            return image;
        } else {
            return image;
        }
    }

    public static Bitmap resizeHeight(@NonNull Bitmap image, int maxHeight){
        if(maxHeight>0){
            double scale = (double) maxHeight / image.getHeight();
            int width = (int) (scale * image.getWidth());

            image = Bitmap.createScaledBitmap(image, width, maxHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static Bitmap resize(@NonNull Bitmap image, int maxWidth, int maxHeight){
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

    public static BitmapTransformation rotateImage(@Degrees.DegreeUnits final int degree){
        return new BitmapTransformation(){

            @Override
            public Bitmap apply(@NonNull Bitmap image) {
                return rotateImage(image, degree);
            }
        };
    }

    public static BitmapTransformation rotateImage(@Degrees.DegreeUnits final int degree, final boolean pre){
        return new BitmapTransformation(){

            @Override
            public Bitmap apply(@NonNull Bitmap image) {
                if(pre){
                    return preRotateImage(image, degree);
                }else {
                    return rotateImage(image, degree);
                }
            }
        };
    }

    public static BitmapTransformation matrix(final Matrix matrix){
        return new BitmapTransformation(){

            @Override
            public Bitmap apply(@NonNull Bitmap image) {
                return applyMatrix(image, matrix);
            }
        };
    }

    @Nullable
    public static Bitmap rotateImage(@NonNull Bitmap img, @Degrees.DegreeUnits int degree) {
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);

            Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);

            return rotatedImg;
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    @Nullable
    public static Bitmap preRotateImage(@NonNull Bitmap img, @Degrees.DegreeUnits int degree) {
        try {
            Matrix matrix = new Matrix();
            matrix.preRotate(degree);

            Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);

            return rotatedImg;
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e.getMessage(),e);
            return null;
        }
    }

    @Nullable
    public static Bitmap applyMatrix(@NonNull Bitmap img, Matrix matrix) {
        try {
            Bitmap bmRotated = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
            return bmRotated;
        } catch (OutOfMemoryError e) {
            Log.e(TAG, e.getMessage(),e);
            return null;
        }
    }
}
