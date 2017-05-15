package com.lularoe.erinfetz.cameralibrary.types;


import java.io.Serializable;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Media {
    private Media(){

    }
    public static final int MEDIA_QUALITY_AUTO = 10;
    public static final int MEDIA_QUALITY_LOWEST = 15;
    public static final int MEDIA_QUALITY_LOW = 11;
    public static final int MEDIA_QUALITY_MEDIUM = 12;
    public static final int MEDIA_QUALITY_HIGH = 13;
    public static final int MEDIA_QUALITY_HIGHEST = 14;

    @IntDef({MEDIA_QUALITY_AUTO, MEDIA_QUALITY_LOWEST, MEDIA_QUALITY_LOW, MEDIA_QUALITY_MEDIUM, MEDIA_QUALITY_HIGH, MEDIA_QUALITY_HIGHEST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaQuality {
    }


    public final static int MEDIA_ACTION_STATE_PHOTO = 0;
    public final static int MEDIA_ACTION_STATE_VIDEO = 1;

    @IntDef({MEDIA_ACTION_STATE_PHOTO, MEDIA_ACTION_STATE_VIDEO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaActionState {
    }


    public static final int MEDIA_ACTION_VIDEO = 100;
    public static final int MEDIA_ACTION_PHOTO = 101;
    public static final int MEDIA_ACTION_UNSPECIFIED = 102;

    @IntDef({MEDIA_ACTION_VIDEO, MEDIA_ACTION_PHOTO, MEDIA_ACTION_UNSPECIFIED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaAction {
    }

    public static final String MEDIA_ERROR_EXTRA = "mcam_error";
    public static final String MEDIA_STATUS_EXTRA = "mcam_status";

    public static final int MEDIA_STATUS_RECORDED = 1;
    public static final int MEDIA_STATUS_RETRY = 2;

    @IntDef({MEDIA_STATUS_RECORDED, MEDIA_STATUS_RETRY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaActionStatus {
    }

    @StringDef({MEDIA_ERROR_EXTRA, MEDIA_STATUS_EXTRA})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaActionKey {
    }
}
