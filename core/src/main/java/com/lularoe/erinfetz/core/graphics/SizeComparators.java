package com.lularoe.erinfetz.core.graphics;

import android.hardware.Camera;

import java.util.Comparator;

public class SizeComparators {
    private SizeComparators(){

    }

    public static class CompareSizesByArea implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }
    }

    public static Comparator<Size> byArea(){
        return new CompareSizesByArea();
    }

    @SuppressWarnings("deprecation")
    public static Comparator<Camera.Size> byArea1(){
        return new CompareSizesByArea1();
    }

    @SuppressWarnings("deprecation")
    public static class CompareSizesByArea1 implements Comparator<Camera.Size> {
        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.width * lhs.height -
                    (long) rhs.width * rhs.height);
        }
    }
}
