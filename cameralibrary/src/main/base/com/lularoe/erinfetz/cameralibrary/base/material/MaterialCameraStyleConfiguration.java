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
import com.lularoe.erinfetz.cameralibrary.R;

public class MaterialCameraStyleConfiguration implements Parcelable {

    private boolean allowRetry = true;
    private boolean autoSubmit = false;
    private int primaryColor=0;
    private boolean showPortraitWarning = true;
    private boolean allowChangeCamera = false;
    private boolean defaultToFrontFacing = false;
    private boolean countdownImmediately = false;
    private boolean retryExists = false;
    private boolean restartTimerOnRetry = false;
    private boolean continueTimerInPlayback = false;

    private int iconRecord = R.drawable.mcam_action_capture;
    private int iconStop = R.drawable.mcam_action_stop;
    private int iconFrontCamera = R.drawable.mcam_camera_front;
    private int iconRearCamera = R.drawable.mcam_camera_rear;
    private int iconPlay = R.drawable.evp_action_play;
    private int iconPause = R.drawable.evp_action_pause;
    private int iconRestart = R.drawable.evp_action_restart;
    private int iconStillShot = R.drawable.mcam_action_stillshot;

    private int labelRetry=R.string.mcam_retry;
    private int labelConfirmVideo=R.string.mcam_use_video;
    private int labelConfirmPhoto = R.string.mcam_use_stillshot;

    private int iconFlashAuto = R.drawable.mcam_action_flash_auto;
    private int iconFlashOn = R.drawable.mcam_action_flash;
    private int iconFlashOff = R.drawable.mcam_action_flash_off;

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
    public int getIconStillShot() {
        return iconStillShot;
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
    public int getLabelConfirmVideo() {
        return labelConfirmVideo;
    }
    @StringRes
    public int getLabelConfirmPhoto() {
        return labelConfirmPhoto;
    }

    @DrawableRes
    public int getIconFlashAuto() {
        return iconFlashAuto;
    }

    @DrawableRes
    public int getIconFlashOn() {
        return iconFlashOn;
    }

    @DrawableRes
    public int getIconFlashOff() {
        return iconFlashOff;
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
        public Builder iconFlashAuto(@DrawableRes int iconRes) {
            c.iconFlashAuto = iconRes;
            return this;
        }
        public Builder iconFlashOn(@DrawableRes int iconRes) {
            c.iconFlashOn = iconRes;
            return this;
        }
        public Builder iconFlashOff(@DrawableRes int iconRes) {
            c.iconFlashOff = iconRes;
            return this;
        }

        public Builder labelConfirmVideo(@StringRes int stringRes) {
            c.labelConfirmVideo = stringRes;
            return this;
        }
        public Builder labelConfirmPhoto(@StringRes int stringRes) {
            c.labelConfirmPhoto = stringRes;
            return this;
        }
        public Builder iconRecord(@DrawableRes int iconRes) {
            c.iconRecord = iconRes;
            return this;
        }
        public Builder iconStillShot(@DrawableRes int iconRes) {
            c.iconStillShot = iconRes;
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
        labelConfirmVideo = in.readInt();
        labelConfirmPhoto = in.readInt();
        iconStillShot=in.readInt();
        iconFlashAuto = in.readInt();
        iconFlashOn = in.readInt();
        iconFlashOff = in.readInt();
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
        dest.writeInt(labelConfirmVideo);
        dest.writeInt(labelConfirmPhoto);
        dest.writeInt(iconStillShot);
        dest.writeInt(iconFlashAuto);
        dest.writeInt(iconFlashOn);
        dest.writeInt(iconFlashOff);
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
