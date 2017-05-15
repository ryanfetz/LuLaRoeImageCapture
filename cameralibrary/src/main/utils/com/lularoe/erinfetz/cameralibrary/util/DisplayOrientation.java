package com.lularoe.erinfetz.cameralibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;

import com.lularoe.erinfetz.cameralibrary.types.Orientation;
import com.lularoe.erinfetz.core.media.Degrees;

import static com.lularoe.erinfetz.core.media.Degrees.*;

public class DisplayOrientation {

    private DisplayOrientation(){

    }

    public static final String TAG = DisplayOrientation.class.getSimpleName();

    @DegreeUnits
    public static int getDisplayRotation(@NonNull Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return DEGREES_0;
            case Surface.ROTATION_90:
                return DEGREES_90;
            case Surface.ROTATION_180:
                return DEGREES_180;
            case Surface.ROTATION_270:
                return DEGREES_270;
        }
        return DEGREES_0;
    }


    @SuppressWarnings("ResourceType")
    @DegreeUnits
    public static int getDisplayOrientation(@DegreeUnits int sensorOrientation, @DegreeUnits int displayOrientation, boolean front) {
        final boolean isLandscape = isLandscape(displayOrientation);
        if (displayOrientation == DEGREES_0)
            displayOrientation = DEGREES_360;
        int result = sensorOrientation - displayOrientation;
        result = Degrees.naturalize(result);
        if (isLandscape && front)
            result = Degrees.mirror(result);
        return result;
    }

    @Orientation.DeviceDefaultOrientation
    public static int getDeviceDefaultOrientation(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Configuration config = context.getResources().getConfiguration();

        int rotation = windowManager.getDefaultDisplay().getRotation();

        if (((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) &&
                config.orientation == Configuration.ORIENTATION_LANDSCAPE)
                || ((rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) &&
                config.orientation == Configuration.ORIENTATION_PORTRAIT)) {
            return Configuration.ORIENTATION_LANDSCAPE;
        } else {
            return Configuration.ORIENTATION_PORTRAIT;
        }
    }

    @Orientation.ActivityOrientation
    public static int getActivityOrientation(@NonNull Activity context) {
        @DegreeUnits
        final int rotation = getDisplayRotation(context);
        switch (rotation) {
            case DEGREES_0:
            case DEGREES_360:
                return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            case DEGREES_90:
                return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            case DEGREES_180:
                return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
            case DEGREES_270:
                return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            default:
                Log.e("Degrees", "Unknown screen orientation. Defaulting to portrait.");
                return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
    }

    @Orientation.ActivityOrientation
    public static int getScreenOrientation(Activity context) {
        int rotation = context.getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
                (rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270) && width > height) {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    Log.e(TAG, "Unknown screen orientation. Defaulting to portrait.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        }
        // if the device's natural orientation is landscape or if the device
        // is square:
        else {
            switch (rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    Log.e(TAG, "Unknown screen orientation. Defaulting to landscape.");
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        return orientation;
    }

    public static boolean isPortrait(@NonNull Context activity) {
        return isPortrait(getDisplayRotation(activity));
    }

    public static boolean isLandscape(@NonNull Context activity) {
        return isLandscape(getDisplayRotation(activity));
    }

    public static boolean isPortrait(@DegreeUnits int degrees) {
        return degrees == DEGREES_0 || degrees == DEGREES_180 || degrees == DEGREES_360;
    }

    private static boolean isLandscape(@DegreeUnits int degrees) {
        return degrees == DEGREES_90 || degrees == DEGREES_270;
    }
}
