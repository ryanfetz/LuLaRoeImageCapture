package com.lularoe.erinfetz.core.utils;

import com.lularoe.erinfetz.cameralibrary.types.Orientation;
import static com.lularoe.erinfetz.cameralibrary.types.Orientation.*;

public class Degrees {
    private Degrees(){

    }
    @Orientation.DegreeUnits
    public static int mirror(@Orientation.DegreeUnits int orientation) {
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
    @Orientation.DegreeUnits
    public static int naturalize(@Orientation.DegreeUnits int orientation) {
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
