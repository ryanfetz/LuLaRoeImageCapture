package com.lularoe.erinfetz.cameralibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.lularoe.erinfetz.cameralibrary.internal.CameraIntentKey;
import com.lularoe.erinfetz.cameralibrary.material.CaptureActivity;
import com.lularoe.erinfetz.cameralibrary.util.CameraUtil;
import com.afollestad.materialdialogs.util.DialogUtils;

import java.io.File;

/**
 * Work based on
 * https://github.com/afollestad/material-camera
 * https://github.com/florent37/CameraFragment
 */
@SuppressWarnings("WeakerAccess")
public class CameraActivityManager {

    public static final String ERROR_EXTRA = "mcam_error";
    public static final String STATUS_EXTRA = "mcam_status";

    public static final int STATUS_RECORDED = 1;
    public static final int STATUS_RETRY = 2;

    private Context mContext;
    private Activity mActivityContext;
    private android.app.Fragment mAppFragment;
    private android.support.v4.app.Fragment mSupportFragment;
    private boolean mIsFragment = false;

    private String mSaveDir;



    public CameraActivityManager(@NonNull Activity context) {
        mContext = context;
        mActivityContext = context;
        mPrimaryColor = DialogUtils.resolveColor(context, R.attr.colorPrimary);
    }

    public CameraActivityManager(@NonNull android.app.Fragment context) {
        mIsFragment = true;
        mContext = context.getActivity();
        mAppFragment = context;
        mSupportFragment = null;
        mPrimaryColor = DialogUtils.resolveColor(mContext, R.attr.colorPrimary);
    }

    public CameraActivityManager(@NonNull android.support.v4.app.Fragment context) {
        mIsFragment = true;
        mContext = context.getContext();
        mSupportFragment = context;
        mAppFragment = null;
        mPrimaryColor = DialogUtils.resolveColor(mContext, R.attr.colorPrimary);
    }

//    public CameraActivityManager countdownMillis(long lengthLimitMs) {
//        mLengthLimit = lengthLimitMs;
//        return this;
//    }
//
//    public CameraActivityManager countdownSeconds(float lengthLimitSec) {
//        return countdownMillis((int) (lengthLimitSec * 1000f));
//    }
//
//    public CameraActivityManager countdownMinutes(float lengthLimitMin) {
//        return countdownMillis((int) (lengthLimitMin * 1000f * 60f));
//    }



    public CameraActivityManager saveDir(@Nullable File dir) {
        if (dir == null) return saveDir((String) null);
        return saveDir(dir.getAbsolutePath());
    }

    public CameraActivityManager saveDir(@Nullable String dir) {
        mSaveDir = dir;
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