package com.lularoe.erinfetz.core.res;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class UnitConversion {
    private UnitConversion(){

    }

    public static int convertDipToPixels(Context context, int dip) {
        Resources resources = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, resources.getDisplayMetrics());
        return (int) px;
    }

    public static int convertPtToPixels(Context context, int dip) {
        Resources resources = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, dip, resources.getDisplayMetrics());
        return (int) px;
    }
}
