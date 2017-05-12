package com.lularoe.erinfetz.cameralibrary.types;


import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.IntDef;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import static com.lularoe.erinfetz.core.media.Degrees.*;

public class Orientation {
    private Orientation(){

    }

    @IntDef({ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT, ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ActivityOrientation {
    }

    public static final int SENSOR_POSITION_UP = DEGREES_90;
    public static final int SENSOR_POSITION_UP_SIDE_DOWN = DEGREES_270;
    public static final int SENSOR_POSITION_LEFT = DEGREES_0;
    public static final int SENSOR_POSITION_RIGHT = DEGREES_180;
    public static final int SENSOR_POSITION_UNSPECIFIED = -1;

    @IntDef({SENSOR_POSITION_UP, SENSOR_POSITION_UP_SIDE_DOWN, SENSOR_POSITION_LEFT, SENSOR_POSITION_RIGHT, SENSOR_POSITION_UNSPECIFIED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SensorPosition {
    }


    @IntDef({Configuration.ORIENTATION_UNDEFINED, Configuration.ORIENTATION_LANDSCAPE, Configuration.ORIENTATION_PORTRAIT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DeviceDefaultOrientation {
    }
}
