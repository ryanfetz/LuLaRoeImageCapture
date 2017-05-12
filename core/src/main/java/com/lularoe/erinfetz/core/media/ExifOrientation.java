package com.lularoe.erinfetz.core.media;


import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

public class ExifOrientation {
    public static final String TAG = ExifOrientation.class.getSimpleName();

    private int orientation;
    @Degrees.DegreeUnits private int degrees;
    private Matrix matrix;

    private ExifOrientation(int o){
        this.orientation = o;
        matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                degrees = Degrees.DEGREES_0;
                break;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                degrees = Degrees.DEGREES_0;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                degrees = Degrees.DEGREES_180;
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                degrees = Degrees.DEGREES_180;
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                degrees = Degrees.DEGREES_90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                degrees = Degrees.DEGREES_90;
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                degrees = Degrees.DEGREES_270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                degrees = Degrees.DEGREES_270;
                break;
            default:
                degrees = Degrees.DEGREES_0;
                break;
        }
    }

    @NonNull
    public static ExifOrientation get(@NonNull String inputFile){
        return new ExifOrientation(getExifOrientation(inputFile));
    }

    @Degrees.DegreeUnits
    public static int getExifOrientationDegrees(@NonNull String inputFile) {
        try {
            ExifInterface exif = new ExifInterface(inputFile);

            int o= exif == null ? ExifInterface.ORIENTATION_UNDEFINED :
                    exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (o) {
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return Degrees.DEGREES_180;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                    return Degrees.DEGREES_180;
                case ExifInterface.ORIENTATION_TRANSPOSE:
                    return Degrees.DEGREES_90;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return Degrees.DEGREES_90;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    return Degrees.DEGREES_270;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return Degrees.DEGREES_270;
            }
        } catch (IOException e) {
            Log.e(TAG, "Error when trying to get exif data from : " + inputFile, e);
        }

        return Degrees.DEGREES_0;
    }

    public static int getExifOrientation(@NonNull String inputFile) {

        try {
            ExifInterface exif = new ExifInterface(inputFile);

            return exif == null ? ExifInterface.ORIENTATION_UNDEFINED :
                    exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        } catch (IOException e) {
            Log.e(TAG, "Error when trying to get exif data from : " + inputFile, e);
        }

        return ExifInterface.ORIENTATION_UNDEFINED;
    }

    @NonNull
    public Matrix getMatrix() {
        return matrix;
    }

    public int getOrientation() {
        return orientation;
    }

    @Degrees.DegreeUnits
    public int getDegrees() {
        return degrees;
    }

}
