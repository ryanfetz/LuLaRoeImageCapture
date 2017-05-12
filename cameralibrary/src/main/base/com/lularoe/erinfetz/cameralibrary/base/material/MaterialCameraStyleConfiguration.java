package com.lularoe.erinfetz.cameralibrary.base.material;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.afollestad.materialdialogs.util.DialogUtils;

public class MaterialCameraStyleConfiguration implements Parcelable {

    public static final String INTENT_KEY = "materialStyleConfig";

    private boolean allowRetry = true;
    private boolean autoSubmit = false;
    private int primaryColor;
    private boolean showPortraitWarning = true;
    private boolean allowChangeCamera = true;
    private boolean defaultToFrontFacing = false;
    private boolean countdownImmediately = false;
    private boolean retryExists = false;
    private boolean restartTimerOnRetry = false;
    private boolean continueTimerInPlayback = true;

    private int iconRecord;
    private int iconStop;
    private int iconFrontCamera;
    private int iconRearCamera;
    private int iconPlay;
    private int iconPause;
    private int iconRestart;

    private int labelRetry;
    private int labelConfirm;

    public boolean isAllowRetry() {
        return allowRetry;
    }

    public boolean isAutoSubmit() {
        return autoSubmit;
    }
    @ColorInt
    public int getPrimaryColor() {
        return primaryColor;
    }

    public boolean isShowPortraitWarning() {
        return showPortraitWarning;
    }

    public boolean isAllowChangeCamera() {
        return allowChangeCamera;
    }

    public boolean isDefaultToFrontFacing() {
        return defaultToFrontFacing;
    }

    public boolean isCountdownImmediately() {
        return countdownImmediately;
    }

    public boolean isRetryExists() {
        return retryExists;
    }

    public boolean isRestartTimerOnRetry() {
        return restartTimerOnRetry;
    }

    public boolean isContinueTimerInPlayback() {
        return continueTimerInPlayback;
    }
    @DrawableRes
    public int getIconRecord() {
        return iconRecord;
    }
    @DrawableRes
    public int getIconStop() {
        return iconStop;
    }
    @DrawableRes
    public int getIconFrontCamera() {
        return iconFrontCamera;
    }
    @DrawableRes
    public int getIconRearCamera() {
        return iconRearCamera;
    }
    @DrawableRes
    public int getIconPlay() {
        return iconPlay;
    }
    @DrawableRes
    public int getIconPause() {
        return iconPause;
    }
    @DrawableRes
    public int getIconRestart() {
        return iconRestart;
    }
    @StringRes
    public int getLabelRetry() {
        return labelRetry;
    }
    @StringRes
    public int getLabelConfirm() {
        return labelConfirm;
    }

    protected MaterialCameraStyleConfiguration(){

    }

    public static class Builder{
        private MaterialCameraStyleConfiguration c;
        public Builder(){
            c = new MaterialCameraStyleConfiguration();
        }

        public MaterialCameraStyleConfiguration build(){
            return c;
        }

        public Builder labelConfirm(@StringRes int stringRes) {
            c.labelConfirm = stringRes;
            return this;
        }

        public Builder iconRecord(@DrawableRes int iconRes) {
            c.iconRecord = iconRes;
            return this;
        }

        public Builder iconStop(@DrawableRes int iconRes) {
            c.iconStop = iconRes;
            return this;
        }

        public Builder iconFrontCamera(@DrawableRes int iconRes) {
            c.iconFrontCamera = iconRes;
            return this;
        }

        public Builder iconRearCamera(@DrawableRes int iconRes) {
            c.iconRearCamera = iconRes;
            return this;
        }

        public Builder iconPlay(@DrawableRes int iconRes) {
            c.iconPlay = iconRes;
            return this;
        }

        public Builder iconPause(@DrawableRes int iconRes) {
            c.iconPause = iconRes;
            return this;
        }

        public Builder iconRestart(@DrawableRes int iconRes) {
            c.iconRestart = iconRes;
            return this;
        }

        public Builder labelRetry(@StringRes int stringRes) {
            c.labelRetry = stringRes;
            return this;
        }

        public Builder primaryColor(@ColorInt int color) {
            c.primaryColor = color;
            return this;
        }

        public Builder primaryColorRes(@NonNull Context context, @ColorRes int colorRes) {
            return primaryColor(ContextCompat.getColor(context, colorRes));
        }

        public Builder primaryColorAttr(@NonNull Context context, @AttrRes int colorAttr) {
            return primaryColor(DialogUtils.resolveColor(context, colorAttr));
        }

        public Builder showPortraitWarning(boolean show) {
            c.showPortraitWarning = show;
            return this;
        }

        public Builder allowChangeCamera(boolean allowChangeCamera) {
            c.allowChangeCamera = allowChangeCamera;
            return this;
        }

        public Builder defaultToFrontFacing(boolean frontFacing) {
            c.defaultToFrontFacing = frontFacing;
            return this;
        }

        public Builder retryExits(boolean exits) {
            c.retryExists = exits;
            return this;
        }

        public Builder restartTimerOnRetry(boolean restart) {
            c.restartTimerOnRetry = restart;
            return this;
        }

        public Builder continueTimerInPlayback(boolean continueTimer) {
            c.continueTimerInPlayback = continueTimer;
            return this;
        }

        public Builder allowRetry(boolean allowRetry) {
            c.allowRetry = allowRetry;
            return this;
        }

        public Builder autoSubmit(boolean autoSubmit) {
            c.autoSubmit = autoSubmit;
            return this;
        }

        public Builder countdownImmediately(boolean immediately) {
            c.countdownImmediately = immediately;
            return this;
        }
    }

    protected MaterialCameraStyleConfiguration(Parcel in) {
        allowRetry = in.readByte() != 0;
        autoSubmit = in.readByte() != 0;
        primaryColor = in.readInt();
        showPortraitWarning = in.readByte() != 0;
        allowChangeCamera = in.readByte() != 0;
        defaultToFrontFacing = in.readByte() != 0;
        countdownImmediately = in.readByte() != 0;
        retryExists = in.readByte() != 0;
        restartTimerOnRetry = in.readByte() != 0;
        continueTimerInPlayback = in.readByte() != 0;
        iconRecord = in.readInt();
        iconStop = in.readInt();
        iconFrontCamera = in.readInt();
        iconRearCamera = in.readInt();
        iconPlay = in.readInt();
        iconPause = in.readInt();
        iconRestart = in.readInt();
        labelRetry = in.readInt();
        labelConfirm = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (allowRetry ? 1 : 0));
        dest.writeByte((byte) (autoSubmit ? 1 : 0));
        dest.writeInt(primaryColor);
        dest.writeByte((byte) (showPortraitWarning ? 1 : 0));
        dest.writeByte((byte) (allowChangeCamera ? 1 : 0));
        dest.writeByte((byte) (defaultToFrontFacing ? 1 : 0));
        dest.writeByte((byte) (countdownImmediately ? 1 : 0));
        dest.writeByte((byte) (retryExists ? 1 : 0));
        dest.writeByte((byte) (restartTimerOnRetry ? 1 : 0));
        dest.writeByte((byte) (continueTimerInPlayback ? 1 : 0));
        dest.writeInt(iconRecord);
        dest.writeInt(iconStop);
        dest.writeInt(iconFrontCamera);
        dest.writeInt(iconRearCamera);
        dest.writeInt(iconPlay);
        dest.writeInt(iconPause);
        dest.writeInt(iconRestart);
        dest.writeInt(labelRetry);
        dest.writeInt(labelConfirm);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MaterialCameraStyleConfiguration> CREATOR = new Creator<MaterialCameraStyleConfiguration>() {
        @Override
        public MaterialCameraStyleConfiguration createFromParcel(Parcel in) {
            return new MaterialCameraStyleConfiguration(in);
        }

        @Override
        public MaterialCameraStyleConfiguration[] newArray(int size) {
            return new MaterialCameraStyleConfiguration[size];
        }
    };
}
