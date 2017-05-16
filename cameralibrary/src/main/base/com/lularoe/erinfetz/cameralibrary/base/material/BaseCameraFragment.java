package com.lularoe.erinfetz.cameralibrary.base.material;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lularoe.erinfetz.cameralibrary.R;
import com.lularoe.erinfetz.cameralibrary.types.CameraIntentKey;
import com.lularoe.erinfetz.cameralibrary.base.CameraOutputUriProvider;
import com.lularoe.erinfetz.cameralibrary.types.Cameras;
import com.lularoe.erinfetz.cameralibrary.util.DisplayOrientation;
import com.lularoe.erinfetz.cameralibrary.util.ManufacturerUtil;
import com.lularoe.erinfetz.core.DateTimeUtils;
import com.lularoe.erinfetz.core.graphics.Colors;

import java.io.File;

import static android.app.Activity.RESULT_CANCELED;
import static com.lularoe.erinfetz.cameralibrary.types.Media.*;
import static com.lularoe.erinfetz.cameralibrary.types.Cameras.*;

/**
 * @author Aidan Follestad (afollestad)
 */
public abstract class BaseCameraFragment extends Fragment implements CameraOutputUriProvider, View.OnClickListener {

    protected ImageButton mButtonVideo;
    protected ImageButton mButtonStillshot;
    protected ImageButton mButtonFacing;
    protected ImageButton mButtonFlash;
    protected TextView mRecordDuration;
    protected TextView mDelayStartCountdown;

    private boolean mIsRecording;
    protected Uri mOutputUri;
    protected MaterialMediaCaptureContext mInterface;
    protected Handler mPositionHandler;
    protected MediaRecorder mMediaRecorder;
    private int mIconTextColor;

    private boolean mDidAutoRecord = false;
    private Handler mDelayHandler;
    private int mDelayCurrentSecond = -1;

    private final Runnable mPositionUpdater = new Runnable() {
        @Override
        public void run() {
            if (mInterface == null || mRecordDuration == null) return;
            final long mRecordStart = mInterface.getRecordingStart();
            final long mRecordEnd = mInterface.getRecordingEnd();
            if (mRecordStart == -1 && mRecordEnd == -1) return;
            final long now = System.currentTimeMillis();
            if (mRecordEnd != -1) {
                if (now >= mRecordEnd) {
                    stopRecordingVideo(true);
                } else {
                    final long diff = mRecordEnd - now;
                    mRecordDuration.setText(String.format("-%s", DateTimeUtils.getDurationString(diff)));
                }
            } else {
                mRecordDuration.setText(DateTimeUtils.getDurationString(now - mRecordStart));
            }
            if (mPositionHandler != null)
                mPositionHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mcam_fragment_videocapture, container, false);
    }

    protected void setImageRes(ImageView iv, @DrawableRes int res) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && iv.getBackground() instanceof RippleDrawable) {
            RippleDrawable rd = (RippleDrawable) iv.getBackground();
            rd.setColor(ColorStateList.valueOf(Colors.adjustAlpha(mIconTextColor, 0.3f)));
        }
        Drawable d = AppCompatResources.getDrawable(iv.getContext(), res);
        d = DrawableCompat.wrap(d.mutate());
        DrawableCompat.setTint(d, mIconTextColor);
        iv.setImageDrawable(d);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDelayStartCountdown = (TextView) view.findViewById(R.id.delayStartCountdown);
        mButtonVideo = (ImageButton) view.findViewById(R.id.video);
        mButtonStillshot = (ImageButton) view.findViewById(R.id.stillshot);
        mRecordDuration = (TextView) view.findViewById(R.id.recordDuration);
        mButtonFacing = (ImageButton) view.findViewById(R.id.facing);
        if (!mInterface.getCameraStyleConfiguration().isAllowChangeCamera() || ManufacturerUtil.isChromium()) {
            mButtonFacing.setVisibility(View.GONE);
        } else {
            setImageRes(mButtonFacing, mInterface.getCurrentCameraPosition() == CAMERA_POSITION_BACK ?
                    mInterface.getCameraStyleConfiguration().getIconFrontCamera() : mInterface.getCameraStyleConfiguration().getIconRearCamera());
        }

        mButtonFlash = (ImageButton) view.findViewById(R.id.flash);
        setupFlashMode();

        mButtonVideo.setOnClickListener(this);
        mButtonStillshot.setOnClickListener(this);
        mButtonFacing.setOnClickListener(this);
        mButtonFlash.setOnClickListener(this);

        int primaryColor = getArguments().getInt(CameraIntentKey.PRIMARY_COLOR);
        if (Colors.isColorDark(primaryColor)) {
            mIconTextColor = ContextCompat.getColor(getActivity(), R.color.mcam_color_light);
            primaryColor = Colors.darkenColor(primaryColor);
        } else {
            mIconTextColor = ContextCompat.getColor(getActivity(), R.color.mcam_color_dark);
        }
        view.findViewById(R.id.controlsFrame).setBackgroundColor(primaryColor);
        mRecordDuration.setTextColor(mIconTextColor);

        if (mMediaRecorder != null && mIsRecording) {
            setImageRes(mButtonVideo, mInterface.getCameraStyleConfiguration().getIconStop());
        } else {
            setImageRes(mButtonVideo, mInterface.getCameraStyleConfiguration().getIconRecord());
            mInterface.setDidRecord(false);
        }

        if (savedInstanceState != null)
            mOutputUri = savedInstanceState.getParcelable(CameraIntentKey.OUTPUT_URI);

        if (mInterface.useStillshot()) {
            mButtonVideo.setVisibility(View.GONE);
            mRecordDuration.setVisibility(View.GONE);
            mButtonStillshot.setVisibility(View.VISIBLE);
            setImageRes(mButtonStillshot, mInterface.getCameraStyleConfiguration().getIconStillShot());
            mButtonFlash.setVisibility(View.VISIBLE);
        }

        if (autoRecordDelay()< 1000) {
            mDelayStartCountdown.setVisibility(View.GONE);
        } else {
            mDelayStartCountdown.setText(Long.toString(autoRecordDelay() / 1000));
        }
    }

    protected void onFlashModesLoaded() {
        if (getCurrentCameraPosition() != CAMERA_POSITION_FRONT) {
            invalidateFlash(false);
        }
    }

    protected long autoRecordDelay(){
        return mInterface.getCameraConfiguration().getAutoRecord();
    }

    protected void onCameraOpened() {
        if (mDidAutoRecord || mInterface == null || mInterface.useStillshot() || autoRecordDelay() < 0 || getActivity() == null) {
            mDelayStartCountdown.setVisibility(View.GONE);
            mDelayHandler = null;
            return;
        }
        mDidAutoRecord = true;
        mButtonFacing.setVisibility(View.GONE);

        if (autoRecordDelay() == 0) {
            mDelayStartCountdown.setVisibility(View.GONE);
            mIsRecording = startRecordingVideo();
            mDelayHandler = null;
            return;
        }

        mDelayHandler = new Handler();
        mButtonVideo.setEnabled(false);

        if (autoRecordDelay() < 1000) {
            // Less than a second delay
            mDelayStartCountdown.setVisibility(View.GONE);
            mDelayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isAdded() || getActivity() == null || mIsRecording) return;
                    mButtonVideo.setEnabled(true);
                    mIsRecording = startRecordingVideo();
                    mDelayHandler = null;
                }
            }, autoRecordDelay());
            return;
        }

        mDelayStartCountdown.setVisibility(View.VISIBLE);
        mDelayCurrentSecond = (int) autoRecordDelay() / 1000;
        mDelayHandler.postDelayed(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                if (!isAdded() || getActivity() == null || mIsRecording) return;
                mDelayCurrentSecond -= 1;
                mDelayStartCountdown.setText(Integer.toString(mDelayCurrentSecond));

                if (mDelayCurrentSecond == 0) {
                    mDelayStartCountdown.setVisibility(View.GONE);
                    mButtonVideo.setEnabled(true);
                    mIsRecording = startRecordingVideo();
                    mDelayHandler = null;
                    return;
                }

                mDelayHandler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mButtonVideo = null;
        mButtonStillshot = null;
        mButtonFacing = null;
        mButtonFlash = null;
        mRecordDuration = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mInterface != null && mInterface.getCameraConfiguration().hasVideoDuration()) {
            if (countdownImmediately() || mInterface.getRecordingStart() > -1) {
                if (mInterface.getRecordingStart() == -1)
                    mInterface.setRecordingStart(System.currentTimeMillis());
                startCounter();
            } else {
                mRecordDuration.setText(String.format("-%s", DateTimeUtils.getDurationString(mInterface.getCameraConfiguration().getVideoDuration())));
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public final void onAttach(Activity activity) {
        super.onAttach(activity);
        mInterface = (MaterialMediaCaptureContext) activity;
    }

    @NonNull
    protected final Uri getOutputMediaFile() {
//        return CameraUtil.makeTempFile(getActivity(), getArguments().getString(CameraIntentKey.SAVE_DIR), "VID_", ".mp4");
        return getArguments().getParcelable(CameraIntentKey.OUTPUT_URI);
    }
//
//    @NonNull
//    protected final File getOutputPictureFile() {
//        return CameraUtil.makeTempFile(getActivity(), getArguments().getString(CameraIntentKey.SAVE_DIR), "IMG_", ".jpg");
//    }

    public abstract void openCamera();

    public abstract void closeCamera();

    public void cleanup() {
        closeCamera();
        releaseRecorder();
        stopCounter();
    }

    public abstract void takeStillshot();

    public abstract void onPreferencesUpdated();

    @Override
    public void onPause() {
        super.onPause();
        cleanup();
    }

    @Override
    public final void onDetach() {
        super.onDetach();
        mInterface = null;
    }

    public final void startCounter() {
        if (mPositionHandler == null)
            mPositionHandler = new Handler();
        else mPositionHandler.removeCallbacks(mPositionUpdater);
        mPositionHandler.post(mPositionUpdater);
    }

    @Cameras.CameraPosition
    public final int getCurrentCameraPosition() {
        if (mInterface == null) return CAMERA_POSITION_UNKNOWN;
        return mInterface.getCurrentCameraPosition();
    }

    public final int getCurrentCameraId() {
        if (mInterface.getCurrentCameraPosition() == CAMERA_POSITION_BACK)
            return (Integer) mInterface.getBackCamera();
        else return (Integer) mInterface.getFrontCamera();
    }

    public final void stopCounter() {
        if (mPositionHandler != null) {
            mPositionHandler.removeCallbacks(mPositionUpdater);
            mPositionHandler = null;
        }
    }

    public final void releaseRecorder() {
        if (mMediaRecorder != null) {
            if (mIsRecording) {
                try {
                    mMediaRecorder.stop();
                } catch (Throwable t) {
                    //noinspection ResultOfMethodCallIgnored
                    new File(mOutputUri.getPath()).delete();
                    t.printStackTrace();
                }
                mIsRecording = false;
            }
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    protected boolean countdownImmediately(){
        return mInterface.getCameraStyleConfiguration().isCountdownImmediately();
    }

    public boolean startRecordingVideo() {
        if (mInterface != null && mInterface.getCameraConfiguration().hasVideoDuration() && !countdownImmediately()) {
            // Countdown wasn't started in onResume, start it now
            if (mInterface.getRecordingStart() == -1)
                mInterface.setRecordingStart(System.currentTimeMillis());
            startCounter();
        }

        final int orientation = DisplayOrientation.getActivityOrientation(getActivity());
        //noinspection ResourceType
        getActivity().setRequestedOrientation(orientation);
        mInterface.setDidRecord(true);
        return true;
    }

    public void stopRecordingVideo(boolean reachedZero) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public final void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CameraIntentKey.OUTPUT_URI, mOutputUri);
    }

    @Override
    public final Uri getOutputUri() {
        return mOutputUri;
    }

    protected final void throwError(Exception e) {
        Activity act = getActivity();
        if (act != null) {
            act.setResult(RESULT_CANCELED, new Intent().putExtra(MEDIA_ERROR_EXTRA, e));
            act.finish();
        }
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        if (id == R.id.facing) {
            mInterface.toggleCameraPosition();
            setImageRes(mButtonFacing, mInterface.getCurrentCameraPosition() == CAMERA_POSITION_BACK ?
                    mInterface.getCameraStyleConfiguration().getIconFrontCamera() : mInterface.getCameraStyleConfiguration().getIconRearCamera());
            closeCamera();
            openCamera();
            setupFlashMode();
        } else if (id == R.id.video) {
            if (mIsRecording) {
                stopRecordingVideo(false);
                mIsRecording = false;
            } else {
                if (getArguments().getBoolean(CameraIntentKey.SHOW_PORTRAIT_WARNING, true) &&
                        DisplayOrientation.isPortrait(getActivity())) {
                    new MaterialDialog.Builder(getActivity())
                            .title(R.string.mcam_portrait)
                            .content(R.string.mcam_portrait_warning)
                            .positiveText(R.string.mcam_yes)
                            .negativeText(android.R.string.cancel)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                                    mIsRecording = startRecordingVideo();
                                }
                            })
                            .show();
                } else {
                    mIsRecording = startRecordingVideo();
                }
            }
        } else if (id == R.id.stillshot) {
            takeStillshot();
        } else if (id == R.id.flash) {
            invalidateFlash(true);
        }
    }

    private void invalidateFlash(boolean toggle) {
        if (toggle) mInterface.toggleFlashMode();
        setupFlashMode();
        onPreferencesUpdated();
    }

    private void setupFlashMode() {
        if (mInterface.shouldHideFlash()) {
            mButtonFlash.setVisibility(View.GONE);
            return;
        } else {
            mButtonFlash.setVisibility(View.VISIBLE);
        }

        final int res;
        switch (mInterface.getFlashMode()) {
            case FLASH_MODE_AUTO:
                res = mInterface.getCameraStyleConfiguration().getIconFlashAuto();
                break;
            case FLASH_MODE_ALWAYS_ON:
                res = mInterface.getCameraStyleConfiguration().getIconFlashOn();
                break;
            case FLASH_MODE_OFF:
            default:
                res = mInterface.getCameraStyleConfiguration().getIconFlashOff();
        }

        setImageRes(mButtonFlash, res);
    }
}