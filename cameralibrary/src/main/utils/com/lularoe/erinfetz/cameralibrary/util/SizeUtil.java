package com.lularoe.erinfetz.cameralibrary.util;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.media.CamcorderProfile;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.lularoe.erinfetz.cameralibrary.types.Media;
import com.lularoe.erinfetz.core.graphics.Size;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SizeUtil {
    private SizeUtil(){

    }

    public static final String TAG = SizeUtil.class.getSimpleName();

    public static final double ASPECT_TOLERANCE = 0.1;

    public static Size getPictureSize(List<Size> choices, @Media.MediaQuality int mediaQuality) {
        if (choices == null || choices.isEmpty()) return null;
        if (choices.size() == 1) return choices.get(0);

        Size maxPictureSize = Collections.max(choices, Size.byArea());
        Size minPictureSize = Collections.min(choices, Size.byArea());

        Collections.sort(choices, Size.byArea());


        return getPictureSize(choices, mediaQuality, maxPictureSize, minPictureSize);
    }

    public static <T>  T getPictureSize(List<T> choices, @Media.MediaQuality int mediaQuality, T max, T min){
        T result = null;
        if (mediaQuality == Media.MEDIA_QUALITY_HIGHEST) {
            result = max;
        } else if (mediaQuality == Media.MEDIA_QUALITY_LOW) {
            if (choices.size() == 2) result = min;
            else {
                int half = choices.size() / 2;
                int lowQualityIndex = (choices.size() - half) / 2;
                result = choices.get(lowQualityIndex + 1);
            }
        } else if (mediaQuality == Media.MEDIA_QUALITY_HIGH) {
            if (choices.size() == 2) result = max;
            else {
                int half = choices.size() / 2;
                int highQualityIndex = (choices.size() - half) / 2;
                result = choices.get(choices.size() - highQualityIndex - 1);
            }
        } else if (mediaQuality == Media.MEDIA_QUALITY_MEDIUM) {
            if (choices.size() == 2) result = min;
            else {
                int mediumQualityIndex = choices.size() / 2;
                result = choices.get(mediumQualityIndex);
            }
        } else if (mediaQuality == Media.MEDIA_QUALITY_LOWEST) {
            result = min;
        }

        return result;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Size getPictureSize(Size[] sizes, @Media.MediaQuality int mediaQuality) {
        if (sizes == null || sizes.length == 0) return null;

        List<Size> choices = Arrays.asList(sizes);

        if (choices.size() == 1) return choices.get(0);

        Size maxPictureSize = Collections.max(choices, Size.byArea());
        Size minPictureSize = Collections.min(choices, Size.byArea());

        Collections.sort(choices, Size.byArea());

        return getPictureSize(choices, mediaQuality, maxPictureSize, minPictureSize);
    }

    public static Size getOptimalPreviewSize(List<Size> sizes, int width, int height) {
        double targetRatio = (double) height / width;

        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = height;

        for (Size size : sizes) {
            double ratio = (double) size.getWidth() / size.getHeight();
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.getHeight() - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.getHeight() - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.getHeight() - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.getHeight() - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Size getOptimalPreviewSize(Size[] sizes, int width, int height) {
        double targetRatio = (double) height / width;

        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = height;

        for (Size size : sizes) {
            double ratio = (double) size.getWidth() / size.getHeight();
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.getHeight() - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.getHeight() - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.getHeight() - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.getHeight() - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public static Size getSizeWithClosestRatio(List<Size> sizes, int width, int height) {

        if (sizes == null) return null;

        double MIN_TOLERANCE = 100;
        double targetRatio = (double) height / width;
        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = height;

        for (Size size : sizes) {
            if (size.getWidth() == width && size.getHeight() == height)
                return size;

            double ratio = (double) size.getHeight() / size.getWidth();

            if (Math.abs(ratio - targetRatio) < MIN_TOLERANCE) MIN_TOLERANCE = ratio;
            else continue;

            if (Math.abs(size.getHeight() - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.getHeight() - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.getHeight() - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.getHeight() - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Size getSizeWithClosestRatio(Size[] sizes, int width, int height) {

        if (sizes == null) return null;

        double MIN_TOLERANCE = 100;
        double targetRatio = (double) height / width;
        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = height;

        for (Size size : sizes) {
//            if (size.getWidth() == width && size.getHeight() == height)
//                return size;

            double ratio = (double) size.getHeight() / size.getWidth();

            if (Math.abs(ratio - targetRatio) < MIN_TOLERANCE) MIN_TOLERANCE = ratio;
            else continue;

            if (Math.abs(size.getHeight() - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.getHeight() - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.getHeight() - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.getHeight() - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, Size.byArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return null;
        }
    }

    public static Size chooseOptimalSize(List<Size> choices, int width, int height, Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, Size.byArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return null;
        }
    }

    public static Size chooseVideoSize(int videoPreferredHeight, float videoPreferredAspect, List<Size> choices) {
        Size backupSize = null;
        for (Size size : choices) {
            if (size.getHeight() <= videoPreferredHeight) {
                if (size.getWidth() == size.getHeight() * videoPreferredAspect)
                    return size;
                if (videoPreferredHeight >= size.getHeight())
                    backupSize = size;
            }
        }
        if (backupSize != null) return backupSize;
        Log.e(TAG, "Couldn't find any suitable video size");
        return choices.get(choices.size() - 1);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Size chooseVideoSize(int videoPreferredHeight, float videoPreferredAspect, Size[] choices) {
        Size backupSize = null;
        for (Size size : choices) {
            if (size.getHeight() <= videoPreferredHeight) {
                if (size.getWidth() == size.getHeight() * videoPreferredAspect)
                    return size;
                if (videoPreferredHeight >= size.getHeight())
                    backupSize = size;
            }
        }
        if (backupSize != null) return backupSize;
        Log.e(TAG, "Couldn't find any suitable video size");
        return choices[choices.length - 1];
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, Size.byArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, Size.byArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    public static Size chooseOptimalSize(List<Size> choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, Size.byArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, Size.byArea());
        } else {
            Log.e(TAG,"Couldn't find any suitable preview size");
            return choices.get(0);
        }
    }

    public static double calculateApproximateVideoSize(CamcorderProfile camcorderProfile, int seconds) {
        return ((camcorderProfile.videoBitRate / (float) 1 + camcorderProfile.audioBitRate / (float) 1) * seconds) / (float) 8;
    }

    public static double calculateApproximateVideoDuration(CamcorderProfile camcorderProfile, long maxFileSize) {
        return 8 * maxFileSize / (camcorderProfile.videoBitRate + camcorderProfile.audioBitRate);
    }

    public static long calculateMinimumRequiredBitRate(CamcorderProfile camcorderProfile, long maxFileSize, int seconds) {
        return 8 * maxFileSize / seconds - camcorderProfile.audioBitRate;
    }

    public static Size getDimensions(int orientation, float videoWidth, float videoHeight, View v) {
        final float aspectRatio = videoWidth / videoHeight;
        int width;
        int height;
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ||
                orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
            width = v.getMeasuredWidth();
            height = (int) ((float) width / aspectRatio);
            if (height > v.getMeasuredHeight()) {
                height = v.getMeasuredHeight();
                width = (int) ((float) height * aspectRatio);
            }
        } else {
            height = v.getMeasuredHeight();
            width = (int) ((float) height * aspectRatio);
            if (width > v.getMeasuredWidth()) {
                width = v.getMeasuredWidth();
                height = (int) ((float) width / aspectRatio);
            }
        }
        return new Size(width, height);
    }
}
