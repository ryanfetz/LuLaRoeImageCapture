package com.lularoe.erinfetz.core.media;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Degrees {
    private Degrees(){

    }

    public static final int DEGREES_0 = 0;
    public static final int DEGREES_90 = 90;
    public static final int DEGREES_180 = 180;
    public static final int DEGREES_270 = 270;
    public static final int DEGREES_360 = 360;

    @IntDef({DEGREES_0, DEGREES_90, DEGREES_180, DEGREES_270, DEGREES_360})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DegreeUnits {
    }

    @DegreeUnits
    public static int mirror(@DegreeUnits int orientation) {
        switch (orientation) {
            case DEGREES_0:
            case DEGREES_360:
                return DEGREES_180;
            case DEGREES_90:
                return DEGREES_270;
            case DEGREES_180:
                return DEGREES_0;
            case DEGREES_270:
                return DEGREES_90;
        }
        return DEGREES_0;
    }

    @SuppressWarnings("ResourceType")
    @DegreeUnits
    public static int naturalize(@DegreeUnits int orientation) {
        if (orientation == DEGREES_360)
            orientation = DEGREES_0;
        else if (orientation > DEGREES_360) {
            do {
                orientation = orientation - DEGREES_360;
            } while (orientation > DEGREES_360);
        } else if (orientation < DEGREES_0) {
            do {
                orientation = DEGREES_360 + orientation;
            } while (orientation < DEGREES_0);
        }
        return orientation;
    }
}
