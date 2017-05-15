package com.lularoe.erinfetz.cameralibrary.util;


import android.annotation.TargetApi;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;

import com.lularoe.erinfetz.cameralibrary.types.Media;
import com.lularoe.erinfetz.core.graphics.SizeComparators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("deprecation")
public class CameraSizeUtil {
    private CameraSizeUtil(){

    }
    public static final String TAG = CameraSizeUtil.class.getSimpleName();

    public static Camera.Size getPictureSize(List<Camera.Size> choices, @Media.MediaQuality int mediaQuality) {
        if (choices == null || choices.isEmpty()) return null;
        if (choices.size() == 1) return choices.get(0);

        Camera.Size maxPictureSize = Collections.max(choices, SizeComparators.byArea1());
        Camera.Size minPictureSize = Collections.min(choices, SizeComparators.byArea1());

        Collections.sort(choices, SizeComparators.byArea1());


        return SizeUtil.getPictureSize(choices, mediaQuality, maxPictureSize, minPictureSize);
    }

    public static Camera.Size getPictureSize(Camera.Size[] sizes, @Media.MediaQuality int mediaQuality) {
        if (sizes == null || sizes.length == 0) return null;

        List<Camera.Size> choices = Arrays.asList(sizes);

        if (choices.size() == 1) return choices.get(0);

        Camera.Size maxPictureSize = Collections.max(choices, SizeComparators.byArea1());
        Camera.Size minPictureSize = Collections.min(choices, SizeComparators.byArea1());

        Collections.sort(choices, SizeComparators.byArea1());

        return SizeUtil.getPictureSize(choices, mediaQuality, maxPictureSize, minPictureSize);
    }

    public static Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int width, int height) {
        double targetRatio = (double) height / width;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = height;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > SizeUtil.ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public static Camera.Size getOptimalPreviewSize(Camera.Size[] sizes, int width, int height) {
        double targetRatio = (double) height / width;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = height;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > SizeUtil.ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public static Camera.Size getSizeWithClosestRatio(List<Camera.Size> sizes, int width, int height) {

        if (sizes == null) return null;

        double MIN_TOLERANCE = 100;
        double targetRatio = (double) height / width;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = height;

        for (Camera.Size size : sizes) {
//            if (size.width == width && size.height == height)
//                return size;

            double ratio = (double) size.height / size.width;

            if (Math.abs(ratio - targetRatio) < MIN_TOLERANCE) MIN_TOLERANCE = ratio;
            else continue;

            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public static Camera.Size getSizeWithClosestRatio(Camera.Size[] sizes, int width, int height) {

        if (sizes == null) return null;

        double MIN_TOLERANCE = 100;
        double targetRatio = (double) height / width;
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = height;

        for (Camera.Size size : sizes) {
//            if (size.getWidth() == width && size.getHeight() == height)
//                return size;

            double ratio = (double) size.height / size.width;

            if (Math.abs(ratio - targetRatio) < MIN_TOLERANCE) MIN_TOLERANCE = ratio;
            else continue;

            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public static Camera.Size chooseOptimalSize(Camera.Size[] choices, int width, int height, Camera.Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Camera.Size> bigEnough = new ArrayList<>();
        int w = aspectRatio.width;
        int h = aspectRatio.height;
        for (Camera.Size option : choices) {
            if (option.height == option.width * h / w &&
                    option.width >= width && option.height >= height) {
                bigEnough.add(option);
            }
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, SizeComparators.byArea1());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return null;
        }
    }
}
