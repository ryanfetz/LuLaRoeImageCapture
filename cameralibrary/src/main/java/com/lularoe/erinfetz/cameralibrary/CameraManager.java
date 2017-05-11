package com.lularoe.erinfetz.cameralibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.CamcorderProfile;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.lularoe.erinfetz.cameralibrary.internal.CameraIntentKey;
import com.lularoe.erinfetz.cameralibrary.types.Media;
import com.lularoe.erinfetz.cameralibrary.util.CameraUtil;
import com.afollestad.materialdialogs.util.DialogUtils;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Work based on
 * https://github.com/afollestad/material-camera
 * https://github.com/florent37/CameraFragment
 */
@SuppressWarnings("WeakerAccess")
public class CameraManager {

    public static final String ERROR_EXTRA = "mcam_error";
    public static final String STATUS_EXTRA = "mcam_status";

    public static final int STATUS_RECORDED = 1;
    public static final int STATUS_RETRY = 2;

    private Context mContext;
    private Activity mActivityContext;
    private android.app.Fragment mAppFragment;
    private android.support.v4.app.Fragment mSupportFragment;
    private boolean mIsFragment = false;
    private long mLengthLimit = -1;
    private boolean mAllowRetry = true;
    private boolean mAutoSubmit = false;
    private String mSaveDir;
    private int mPrimaryColor;
    private boolean mShowPortraitWarning = true;
    private boolean mAllowChangeCamera = true;
    private boolean mDefaultToFrontFacing = false;
    private boolean mCountdownImmediately = false;
    private boolean mRetryExists = false;
    private boolean mRestartTimerOnRetry = false;
    private boolean mContinueTimerInPlayback = true;
    private boolean mForceCamera1 = false;
    private boolean mStillShot;
    private boolean mAudioDisabled = false;
    private long mAutoRecord = -1;

    private int mVideoEncodingBitRate = -1;
    private int mAudioEncodingBitRate = -1;
    private int mVideoFrameRate = -1;
    private int mVideoPreferredHeight = -1;
    private float mVideoPreferredAspect = -1f;
    private long mMaxFileSize = -1;
    private int mQualityProfile = -1;

    private int mIconRecord;
    private int mIconStop;
    private int mIconFrontCamera;
    private int mIconRearCamera;
    private int mIconPlay;
    private int mIconPause;
    private int mIconRestart;

    private int mLabelRetry;
    private int mLabelConfirm;

    public CameraManager(@NonNull Activity context) {
        mContext = context;
        mActivityContext = context;
        mPrimaryColor = DialogUtils.resolveColor(context, R.attr.colorPrimary);
    }

    public CameraManager(@NonNull android.app.Fragment context) {
        mIsFragment = true;
        mContext = context.getActivity();
        mAppFragment = context;
        mSupportFragment = null;
        mPrimaryColor = DialogUtils.resolveColor(mContext, R.attr.colorPrimary);
    }

    public CameraManager(@NonNull android.support.v4.app.Fragment context) {
        mIsFragment = true;
        mContext = context.getContext();
        mSupportFragment = context;
        mAppFragment = null;
        mPrimaryColor = DialogUtils.resolveColor(mContext, R.attr.colorPrimary);
    }

    public CameraManager countdownMillis(long lengthLimitMs) {
        mLengthLimit = lengthLimitMs;
        return this;
    }

    public CameraManager countdownSeconds(float lengthLimitSec) {
        return countdownMillis((int) (lengthLimitSec * 1000f));
    }

    public CameraManager countdownMinutes(float lengthLimitMin) {
        return countdownMillis((int) (lengthLimitMin * 1000f * 60f));
    }

    public CameraManager allowRetry(boolean allowRetry) {
        mAllowRetry = allowRetry;
        return this;
    }

    public CameraManager autoSubmit(boolean autoSubmit) {
        mAutoSubmit = autoSubmit;
        return this;
    }

    public CameraManager countdownImmediately(boolean immediately) {
        mCountdownImmediately = immediately;
        return this;
    }

    public CameraManager saveDir(@Nullable File dir) {
        if (dir == null) return saveDir((String) null);
        return saveDir(dir.getAbsolutePath());
    }

    public CameraManager saveDir(@Nullable String dir) {
        mSaveDir = dir;
        return this;
    }

    public CameraManager primaryColor(@ColorInt int color) {
        mPrimaryColor = color;
        return this;
    }

    public CameraManager primaryColorRes(@ColorRes int colorRes) {
        return primaryColor(ContextCompat.getColor(mContext, colorRes));
    }

    public CameraManager primaryColorAttr(@AttrRes int colorAttr) {
        return primaryColor(DialogUtils.resolveColor(mContext, colorAttr));
    }

    public CameraManager showPortraitWarning(boolean show) {
        mShowPortraitWarning = show;
        return this;
    }

    public CameraManager allowChangeCamera(boolean allowChangeCamera) {
        mAllowChangeCamera = allowChangeCamera;
        return this;
    }

    public CameraManager defaultToFrontFacing(boolean frontFacing) {
        mDefaultToFrontFacing = frontFacing;
        return this;
    }

    public CameraManager retryExits(boolean exits) {
        mRetryExists = exits;
        return this;
    }

    public CameraManager restartTimerOnRetry(boolean restart) {
        mRestartTimerOnRetry = restart;
        return this;
    }

    public CameraManager continueTimerInPlayback(boolean continueTimer) {
        mContinueTimerInPlayback = continueTimer;
        return this;
    }

    public CameraManager forceCamera1() {
        mForceCamera1 = true;
        return this;
    }

    public CameraManager audioDisabled(boolean disabled) {
        mAudioDisabled = disabled;
        return this;
    }

    /**
     * @deprecated Renamed to videoEncodingBitRate(int).
     */
    @Deprecated
    public CameraManager videoBitRate(@IntRange(from = 1, to = Integer.MAX_VALUE) int rate) {
        return videoEncodingBitRate(rate);
    }

    public CameraManager videoEncodingBitRate(@IntRange(from = 1, to = Integer.MAX_VALUE) int rate) {
        mVideoEncodingBitRate = rate;
        return this;
    }

    public CameraManager audioEncodingBitRate(@IntRange(from = 1, to = Integer.MAX_VALUE) int rate) {
        mAudioEncodingBitRate = rate;
        return this;
    }

    public CameraManager videoFrameRate(@IntRange(from = 1, to = Integer.MAX_VALUE) int rate) {
        mVideoFrameRate = rate;
        return this;
    }

    public CameraManager videoPreferredHeight(@IntRange(from = 1, to = Integer.MAX_VALUE) int height) {
        mVideoPreferredHeight = height;
        return this;
    }

    public CameraManager videoPreferredAspect(@FloatRange(from = 0.1, to = Float.MAX_VALUE) float ratio) {
        mVideoPreferredAspect = ratio;
        return this;
    }

    public CameraManager maxAllowedFileSize(long size) {
        mMaxFileSize = size;
        return this;
    }

    public CameraManager qualityProfile(@Media.MediaQuality int profile) {
        mQualityProfile = profile;
        return this;
    }

    public CameraManager iconRecord(@DrawableRes int iconRes) {
        mIconRecord = iconRes;
        return this;
    }

    public CameraManager iconStop(@DrawableRes int iconRes) {
        mIconStop = iconRes;
        return this;
    }

    public CameraManager iconFrontCamera(@DrawableRes int iconRes) {
        mIconFrontCamera = iconRes;
        return this;
    }

    public CameraManager iconRearCamera(@DrawableRes int iconRes) {
        mIconRearCamera = iconRes;
        return this;
    }

    public CameraManager iconPlay(@DrawableRes int iconRes) {
        mIconPlay = iconRes;
        return this;
    }

    public CameraManager iconPause(@DrawableRes int iconRes) {
        mIconPause = iconRes;
        return this;
    }

    public CameraManager iconRestart(@DrawableRes int iconRes) {
        mIconRestart = iconRes;
        return this;
    }

    public CameraManager labelRetry(@StringRes int stringRes) {
        mLabelRetry = stringRes;
        return this;
    }

    @Deprecated
    /*
        This has been replaced with labelConfirm
     */
    public CameraManager labelUseVideo(@StringRes int stringRes) {
        mLabelConfirm = stringRes;
        return this;
    }

    public CameraManager labelConfirm(@StringRes int stringRes) {
        mLabelConfirm = stringRes;
        return this;
    }

    /**
     * Will take a still shot instead of recording.
     */
    public CameraManager stillShot() {
        mStillShot = true;
        return this;
    }

    public CameraManager autoRecordWithDelayMs(@IntRange(from = -1, to = Long.MAX_VALUE) long delayMillis) {
        mAutoRecord = delayMillis;
        return this;
    }

    public CameraManager autoRecordWithDelaySec(@IntRange(from = -1, to = Long.MAX_VALUE) int delaySeconds) {
        mAutoRecord = delaySeconds * 1000;
        return this;
    }

    public Intent getIntent() {
        final Class<?> cls = !mForceCamera1 && CameraUtil.hasCamera2(mContext, mStillShot) ?
                CaptureActivity2.class : CaptureActivity.class;
        Intent intent = new Intent(mContext, cls)
                .putExtra(CameraIntentKey.LENGTH_LIMIT, mLengthLimit)
                .putExtra(CameraIntentKey.ALLOW_RETRY, mAllowRetry)
                .putExtra(CameraIntentKey.AUTO_SUBMIT, mAutoSubmit)
                .putExtra(CameraIntentKey.SAVE_DIR, mSaveDir)
                .putExtra(CameraIntentKey.PRIMARY_COLOR, mPrimaryColor)
                .putExtra(CameraIntentKey.SHOW_PORTRAIT_WARNING, mShowPortraitWarning)
                .putExtra(CameraIntentKey.ALLOW_CHANGE_CAMERA, mAllowChangeCamera)
                .putExtra(CameraIntentKey.DEFAULT_TO_FRONT_FACING, mDefaultToFrontFacing)
                .putExtra(CameraIntentKey.COUNTDOWN_IMMEDIATELY, mCountdownImmediately)
                .putExtra(CameraIntentKey.RETRY_EXITS, mRetryExists)
                .putExtra(CameraIntentKey.RESTART_TIMER_ON_RETRY, mRestartTimerOnRetry)
                .putExtra(CameraIntentKey.CONTINUE_TIMER_IN_PLAYBACK, mContinueTimerInPlayback)
                .putExtra(CameraIntentKey.STILL_SHOT, mStillShot)
                .putExtra(CameraIntentKey.AUTO_RECORD, mAutoRecord)
                .putExtra(CameraIntentKey.AUDIO_DISABLED, mAudioDisabled);

        if (mVideoEncodingBitRate > 0)
            intent.putExtra(CameraIntentKey.VIDEO_BIT_RATE, mVideoEncodingBitRate);
        if (mAudioEncodingBitRate > 0)
            intent.putExtra(CameraIntentKey.AUDIO_ENCODING_BIT_RATE, mAudioEncodingBitRate);
        if (mVideoFrameRate > 0)
            intent.putExtra(CameraIntentKey.VIDEO_FRAME_RATE, mVideoFrameRate);
        if (mVideoPreferredHeight > 0)
            intent.putExtra(CameraIntentKey.VIDEO_PREFERRED_HEIGHT, mVideoPreferredHeight);
        if (mVideoPreferredAspect > 0f)
            intent.putExtra(CameraIntentKey.VIDEO_PREFERRED_ASPECT, mVideoPreferredAspect);
        if (mMaxFileSize > -1)
            intent.putExtra(CameraIntentKey.MAX_ALLOWED_FILE_SIZE, mMaxFileSize);
        if (mQualityProfile > -1)
            intent.putExtra(CameraIntentKey.QUALITY_PROFILE, mQualityProfile);

        if (mIconRecord != 0)
            intent.putExtra(CameraIntentKey.ICON_RECORD, mIconRecord);
        if (mIconStop != 0)
            intent.putExtra(CameraIntentKey.ICON_STOP, mIconStop);
        if (mIconFrontCamera != 0)
            intent.putExtra(CameraIntentKey.ICON_FRONT_CAMERA, mIconFrontCamera);
        if (mIconRearCamera != 0)
            intent.putExtra(CameraIntentKey.ICON_REAR_CAMERA, mIconRearCamera);
        if (mIconPlay != 0)
            intent.putExtra(CameraIntentKey.ICON_PLAY, mIconPlay);
        if (mIconPause != 0)
            intent.putExtra(CameraIntentKey.ICON_PAUSE, mIconPause);
        if (mIconRestart != 0)
            intent.putExtra(CameraIntentKey.ICON_RESTART, mIconRestart);
        if (mLabelRetry != 0)
            intent.putExtra(CameraIntentKey.LABEL_RETRY, mLabelRetry);
        if (mLabelConfirm != 0)
            intent.putExtra(CameraIntentKey.LABEL_CONFIRM, mLabelConfirm);

        return intent;
    }

    public void start(int requestCode) {
        if (mIsFragment && mSupportFragment != null)
            mSupportFragment.startActivityForResult(getIntent(), requestCode);
        else if (mIsFragment && mAppFragment != null)
            mAppFragment.startActivityForResult(getIntent(), requestCode);
        else
            mActivityContext.startActivityForResult(getIntent(), requestCode);
    }
}