package com.lularoe.erinfetz.cameralibrary.base.material;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lularoe.erinfetz.cameralibrary.R;
import com.lularoe.erinfetz.cameralibrary.TimeLimitReachedException;
import com.lularoe.erinfetz.cameralibrary.base.CameraConfiguration;
import com.lularoe.erinfetz.cameralibrary.base.CameraOrientation;
import com.lularoe.erinfetz.cameralibrary.internal.BaseGalleryFragment;
import com.lularoe.erinfetz.cameralibrary.types.CameraIntentKey;
import com.lularoe.erinfetz.cameralibrary.base.CameraOutputUriProvider;
import com.lularoe.erinfetz.cameralibrary.internal.PlaybackVideoFragment;
import com.lularoe.erinfetz.cameralibrary.internal.StillshotPreviewFragment;
import com.lularoe.erinfetz.cameralibrary.types.Cameras;
import com.lularoe.erinfetz.cameralibrary.types.Media;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lularoe.erinfetz.core.graphics.Colors;
import com.lularoe.erinfetz.core.storage.files.MediaType;

import java.io.File;
import java.util.List;

/**
 *
 */
public abstract class BaseCaptureActivity extends AppCompatActivity implements MaterialMediaCaptureContext {


    private boolean mRequestingPermission;
    private long mRecordingStart = -1;
    private long mRecordingEnd = -1;

    private Object mFrontCameraId;
    private Object mBackCameraId;

    private boolean mDidRecord = false;
    private List<Integer> mFlashModes;

    public static final int PERMISSION_RC = 69;

    @Cameras.CameraPosition
    protected int mCameraPosition = Cameras.CAMERA_POSITION_UNKNOWN;

    @Cameras.FlashMode
    protected int mFlashMode = Cameras.FLASH_MODE_OFF;

    @Override
    protected final void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("camera_position", mCameraPosition);
        outState.putBoolean("requesting_permission", mRequestingPermission);
        outState.putLong("recording_start", mRecordingStart);
        outState.putLong("recording_end", mRecordingEnd);

        if (mFrontCameraId instanceof String) {
            outState.putString("front_camera_id_str", (String) mFrontCameraId);
            outState.putString("back_camera_id_str", (String) mBackCameraId);
        } else {
            if (mFrontCameraId != null)
                outState.putInt("front_camera_id_int", (Integer) mFrontCameraId);
            if (mBackCameraId != null)
                outState.putInt("back_camera_id_int", (Integer) mBackCameraId);
        }
        outState.putInt("flash_mode", mFlashMode);
    }

    @SuppressWarnings("WrongConstant")
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);

        if (!Cameras.hasCamera(this)) {
            new MaterialDialog.Builder(this)
                    .title(R.string.mcam_error)
                    .content(R.string.mcam_video_capture_unsupported)
                    .positiveText(android.R.string.ok)
                    .dismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                        }
                    }).show();
            return;
        }
        setContentView(R.layout.mcam_activity_videocapture);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final int primaryColor = getCameraStyleConfiguration().getPrimaryColor();
            final boolean isPrimaryDark = Colors.isColorDark(primaryColor);
            final Window window = getWindow();
            window.setStatusBarColor(Colors.darkenColor(primaryColor));
            window.setNavigationBarColor(isPrimaryDark ? primaryColor : Color.BLACK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                final View view = window.getDecorView();
                int flags = view.getSystemUiVisibility();
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                view.setSystemUiVisibility(flags);
            }
        }

        if (null == savedInstanceState) {
            checkPermissions();
        } else {

            mCameraPosition = savedInstanceState.getInt("camera_position", getCameraConfiguration().getCameraPosition());
            mRequestingPermission = savedInstanceState.getBoolean("requesting_permission", false);
            mRecordingStart = savedInstanceState.getLong("recording_start", -1);
            mRecordingEnd = savedInstanceState.getLong("recording_end", -1);

            if (savedInstanceState.containsKey("front_camera_id_str")) {
                mFrontCameraId = savedInstanceState.getString("front_camera_id_str");
                mBackCameraId = savedInstanceState.getString("back_camera_id_str");
            } else {
                mFrontCameraId = savedInstanceState.getInt("front_camera_id_int");
                mBackCameraId = savedInstanceState.getInt("back_camera_id_int");
            }
            mFlashMode = savedInstanceState.getInt("flash_mode", getCameraConfiguration().getFlashMode());
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            showInitialRecorder();
            return;
        }
        final boolean cameraGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        final boolean audioGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
        final boolean audioNeeded = !useStillshot() && !getCameraConfiguration().isAudioDisabled();

        String[] perms = null;
        if (cameraGranted) {
            if (audioNeeded && !audioGranted) {
                perms = new String[]{Manifest.permission.RECORD_AUDIO};
            }
        } else {
            if (audioNeeded && !audioGranted) {
                perms = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
            } else {
                perms = new String[]{Manifest.permission.CAMERA};
            }
        }

        if (perms != null) {
            ActivityCompat.requestPermissions(this, perms, PERMISSION_RC);
            mRequestingPermission = true;
        } else {
            showInitialRecorder();
        }
    }

    @Override
    protected final void onPause() {
        super.onPause();
        if (!isFinishing() && !isChangingConfigurations() && !mRequestingPermission)
            finish();
    }

    @Override
    public final void onBackPressed() {
        Fragment frag = getFragmentManager().findFragmentById(R.id.container);
        if (frag != null) {
            if (frag instanceof PlaybackVideoFragment && getCameraStyleConfiguration().isAllowRetry()) {
                onRetry(((CameraOutputUriProvider) frag).getOutputUri());
                return;
            } else if (frag instanceof BaseCameraFragment) {
                ((BaseCameraFragment) frag).cleanup();
            } else if (frag instanceof BaseGalleryFragment && getCameraStyleConfiguration().isAllowRetry()) {
                onRetry(((CameraOutputUriProvider) frag).getOutputUri());
                return;
            }
        }
        finish();
    }

    @NonNull
    public abstract Fragment getFragment();

    public final Fragment createFragment() {
        Fragment frag = getFragment();
        frag.setArguments(getIntent().getExtras());
        return frag;
    }

    @Override
    public void setRecordingStart(long start) {
        mRecordingStart = start;
        if (start > -1 && getCameraConfiguration().hasVideoDuration())
            setRecordingEnd(mRecordingStart + getCameraConfiguration().getVideoDuration());
        else
            setRecordingEnd(-1);
    }


    @Override
    public CameraConfiguration getCameraConfiguration() {
        return getIntent().getParcelableExtra(CameraIntentKey.CAMERA_CONFIG_INTENT_KEY);
    }

    @Override
    public MaterialCameraStyleConfiguration getCameraStyleConfiguration() {
        return getIntent().getParcelableExtra(CameraIntentKey.MATERIAL_STYLE_INTENT_KEY);
    }

    @Override
    public CameraOrientation getCameraOrientation() {
        return getIntent().getParcelableExtra(CameraIntentKey.CAMERA_ORIENTATION_INTENT_KEY);
    }

    @Override
    public long getRecordingStart() {
        return mRecordingStart;
    }

    @Override
    public void setRecordingEnd(long end) {
        mRecordingEnd = end;
    }

    @Override
    public long getRecordingEnd() {
        return mRecordingEnd;
    }

    @Override
    public void setCameraPosition(int position) {
        mCameraPosition = position;
    }

    @Override
    public void toggleCameraPosition() {
        if (getCurrentCameraPosition() == Cameras.CAMERA_POSITION_FRONT) {
            // Front, go to back if possible
            if (getBackCamera() != null)
                setCameraPosition(Cameras.CAMERA_POSITION_BACK);
        } else {
            // Back, go to front if possible
            if (getFrontCamera() != null)
                setCameraPosition(Cameras.CAMERA_POSITION_FRONT);
        }
    }

    @Override
    public int getCurrentCameraPosition() {
        return mCameraPosition;
    }

    @Override
    public Object getCurrentCameraId() {
        if (getCurrentCameraPosition() == Cameras.CAMERA_POSITION_FRONT)
            return getFrontCamera();
        else return getBackCamera();
    }

    @Override
    public void setFrontCamera(Object id) {
        mFrontCameraId = id;
    }

    @Override
    public Object getFrontCamera() {
        return mFrontCameraId;
    }

    @Override
    public void setBackCamera(Object id) {
        mBackCameraId = id;
    }

    @Override
    public Object getBackCamera() {
        return mBackCameraId;
    }

    private void showInitialRecorder() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, createFragment())
                .commit();
    }

    @Override
    public final void onRetry(@Nullable Uri outputUri) {
        if (outputUri != null)
            deleteOutputFile(outputUri);

        if (!getCameraStyleConfiguration().isAutoSubmit() || getCameraStyleConfiguration().isRestartTimerOnRetry())
            setRecordingStart(-1);

        if (getCameraStyleConfiguration().isRetryExists()) {
            setResult(RESULT_OK, new Intent()
                    .putExtra(Media.MEDIA_STATUS_EXTRA, Media.MEDIA_STATUS_RETRY));
            finish();
            return;
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.container, createFragment())
                .commit();
    }

    @Override
    public final void onShowPreview(@Nullable final Uri outputUri, MediaType mediaType, boolean countdownIsAtZero) {
        if ((getCameraStyleConfiguration().isAutoSubmit() && (countdownIsAtZero || !getCameraStyleConfiguration().isAllowRetry() || !getCameraConfiguration().hasVideoDuration())) || outputUri == null) {
            if (outputUri == null) {
                setResult(RESULT_CANCELED, new Intent().putExtra(Media.MEDIA_ERROR_EXTRA,
                        new TimeLimitReachedException()));
                finish();
                return;
            }
            useMedia(outputUri, mediaType);
        } else {
            if (!getCameraConfiguration().hasVideoDuration() || !getCameraStyleConfiguration().isContinueTimerInPlayback()) {
                // No countdown or countdown should not continue through playback, reset timer to 0
                setRecordingStart(-1);
            }
            Fragment frag = PlaybackVideoFragment.newInstance(outputUri, getCameraStyleConfiguration().isAllowRetry(),
                    getCameraStyleConfiguration().getPrimaryColor());

            getFragmentManager().beginTransaction()
                    .replace(R.id.container, frag)
                    .commit();
        }
    }

    @Override
    public void onShowStillshot(Uri outputUri, MediaType mediaType) {
        if (getCameraStyleConfiguration().isAutoSubmit()) {
            useMedia(outputUri, mediaType);
        } else {
            Fragment frag = StillshotPreviewFragment.newInstance(outputUri, getCameraStyleConfiguration().isAllowRetry(),
                    getIntent().getIntExtra(CameraIntentKey.PRIMARY_COLOR, 0));

            getFragmentManager().beginTransaction()
                    .replace(R.id.container, frag)
                    .commit();
        }
    }

    private boolean deleteOutputFile(@Nullable Uri uri) {
        if (uri != null)
            return new File(uri.getPath()).delete();
        else
            return false;
    }

    @Override
    protected final void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_RC)
            showInitialRecorder();
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mRequestingPermission = false;
        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            new MaterialDialog.Builder(this)
                    .title(R.string.mcam_permissions_needed)
                    .content(R.string.mcam_video_perm_warning)
                    .positiveText(android.R.string.ok)
                    .dismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                        }
                    }).show();
        } else {
            showInitialRecorder();
        }
    }

    @Override
    public final void useMedia(Uri uri, MediaType mediaType) {
        if (uri != null) {
            setResult(Activity.RESULT_OK, getIntent()
                    .putExtra(Media.MEDIA_STATUS_EXTRA, Media.MEDIA_STATUS_RECORDED)
                    .setDataAndType(uri, mediaType.withoutParameters()));
        }
        finish();
    }

    @Override
    public void setDidRecord(boolean didRecord) {
        mDidRecord = didRecord;
    }

    @Override
    public boolean didRecord() {
        return mDidRecord;
    }

    @Override
    @Cameras.FlashMode
    public int getFlashMode() {
        return mFlashMode;
    }

    @Override
    @SuppressWarnings("WrongConstant")
    public void toggleFlashMode() {
        if (mFlashModes != null) {
            mFlashMode = mFlashModes.get((mFlashModes.indexOf(mFlashMode) + 1) % mFlashModes.size());
        }
    }

    @Override
    public void setFlashModes(List<Integer> modes) {
        mFlashModes = modes;
    }

    @Override
    public boolean shouldHideFlash() {
        return !useStillshot() || mFlashModes == null;
    }

    @Override
    public boolean useStillshot() {
        return getCameraConfiguration().getMediaAction() == Media.MEDIA_ACTION_PHOTO;
    }
}