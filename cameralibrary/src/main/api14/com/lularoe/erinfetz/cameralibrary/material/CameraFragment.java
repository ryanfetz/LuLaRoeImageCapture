package com.lularoe.erinfetz.cameralibrary.material;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lularoe.erinfetz.cameralibrary.R;
import com.lularoe.erinfetz.cameralibrary.base.CameraProfileProvider;
import com.lularoe.erinfetz.cameralibrary.base.material.MaterialMediaCaptureContext;
import com.lularoe.erinfetz.cameralibrary.ui.CameraPreview;
import com.lularoe.erinfetz.cameralibrary.util.DisplayOrientation;
import com.lularoe.erinfetz.cameralibrary.util.ManufacturerUtil;

import java.io.File;
import java.util.Collections;
import java.util.List;


import static com.lularoe.erinfetz.cameralibrary.types.Cameras.*;

import com.lularoe.erinfetz.cameralibrary.base.material.BaseCameraFragment;
import com.lularoe.erinfetz.cameralibrary.util.SizeUtil;
import com.lularoe.erinfetz.core.graphics.Size;
import com.lularoe.erinfetz.core.media.Degrees;
import com.lularoe.erinfetz.core.media.ImageSaver;
import com.lularoe.erinfetz.core.storage.files.MediaType;

/**
 *
 */
@SuppressWarnings("deprecation")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class CameraFragment extends BaseCameraFragment implements View.OnClickListener {

    public static final String TAG = CameraFragment.class.getSimpleName();

    CameraPreview mPreviewView;
    RelativeLayout mPreviewFrame;

    private Size mVideoSize;
    private Camera mCamera;
    private Point mWindowSize;
    private int mDisplayOrientation;
    private boolean mIsAutoFocusing;
    List<Integer> mFlashModes;

    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPreviewFrame = (RelativeLayout) view.findViewById(R.id.rootFrame);
        mPreviewFrame.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            mPreviewView.getHolder().getSurface().release();
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage(),e);
        }
        mPreviewFrame = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        openCamera();
    }

    @Override
    public void onPause() {
        if (mCamera != null) mCamera.lock();
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rootFrame) {
            if (mCamera == null || mIsAutoFocusing) return;
            try {
                mIsAutoFocusing = true;
                mCamera.cancelAutoFocus();
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        mIsAutoFocusing = false;
                        if (!success)
                            Toast.makeText(getActivity(), "Unable to auto-focus!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Throwable t) {
                Log.e(TAG, t.getMessage(),t);
            }
        } else {
            super.onClick(view);
        }
    }

    @Override
    public void openCamera() {
        final Activity activity = getActivity();
        if (null == activity || activity.isFinishing()) return;

        MaterialMediaCaptureContext mmcc = (MaterialMediaCaptureContext)activity;
        try {
            final int mBackCameraId = mmcc.getBackCamera() != null ? (Integer) mmcc.getBackCamera() : -1;
            final int mFrontCameraId = mmcc.getFrontCamera() != null ? (Integer) mmcc.getFrontCamera() : -1;
            if (mBackCameraId == -1 || mFrontCameraId == -1) {
                int numberOfCameras = Camera.getNumberOfCameras();
                if (numberOfCameras == 0) {
                    throwError(new Exception("No cameras are available on this device."));
                    return;
                }

                for (int i = 0; i < numberOfCameras; i++) {
                    //noinspection ConstantConditions
                    if (mFrontCameraId != -1 && mBackCameraId != -1) break;
                    Camera.CameraInfo info = new Camera.CameraInfo();
                    Camera.getCameraInfo(i, info);
                    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT && mFrontCameraId == -1) {
                        mmcc.setFrontCamera(i);
                    } else if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK && mBackCameraId == -1) {
                        mmcc.setBackCamera(i);
                    }
                }
            }

            switch (getCurrentCameraPosition()) {
                case CAMERA_POSITION_FRONT:
                    setImageRes(mButtonFacing, mmcc.getCameraStyleConfiguration().getIconRearCamera());
                    break;
                case CAMERA_POSITION_BACK:
                    setImageRes(mButtonFacing, mmcc.getCameraStyleConfiguration().getIconFrontCamera());
                    break;
                case CAMERA_POSITION_UNKNOWN:
                default:
                    if (mmcc.getCameraStyleConfiguration().isDefaultToFrontFacing()) {
                        // Check front facing first
                        if (mmcc.getFrontCamera() != null && (Integer) mmcc.getFrontCamera() != -1) {
                            setImageRes(mButtonFacing, mmcc.getCameraStyleConfiguration().getIconRearCamera());
                            mmcc.setCameraPosition(CAMERA_POSITION_FRONT);
                        } else {
                            setImageRes(mButtonFacing, mmcc.getCameraStyleConfiguration().getIconFrontCamera());
                            if (mmcc.getBackCamera() != null && (Integer) mmcc.getBackCamera() != -1)
                                mmcc.setCameraPosition(CAMERA_POSITION_BACK);
                            else mmcc.setCameraPosition(CAMERA_POSITION_UNKNOWN);
                        }
                    } else {
                        // Check back facing first
                        if (mmcc.getBackCamera() != null && (Integer) mmcc.getBackCamera() != -1) {
                            setImageRes(mButtonFacing, mmcc.getCameraStyleConfiguration().getIconFrontCamera());
                            mmcc.setCameraPosition(CAMERA_POSITION_BACK);
                        } else {
                            setImageRes(mButtonFacing, mmcc.getCameraStyleConfiguration().getIconRearCamera());
                            if (mmcc.getFrontCamera() != null && (Integer) mmcc.getFrontCamera() != -1)
                                mmcc.setCameraPosition(CAMERA_POSITION_FRONT);
                            else mmcc.setCameraPosition(CAMERA_POSITION_UNKNOWN);
                        }
                    }
                    break;
            }

            if (mWindowSize == null)
                mWindowSize = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(mWindowSize);
            final int toOpen = getCurrentCameraId();
            mCamera = Camera.open(toOpen == -1 ? 0 : toOpen);
            Camera.Parameters parameters = mCamera.getParameters();
            List<Size> videoSizes = Size.fromList(parameters.getSupportedVideoSizes());
            if (videoSizes == null || videoSizes.size() == 0)
                videoSizes = Size.fromList(parameters.getSupportedPreviewSizes());

            mVideoSize = SizeUtil.chooseVideoSize(mmcc.getCameraConfiguration().getVideoPreferredHeight(), mmcc.getCameraConfiguration().getVideoPreferredAspect(), videoSizes);

            Size previewSize = SizeUtil.chooseOptimalSize(Size.fromList(parameters.getSupportedPreviewSizes()),
                    mWindowSize.x, mWindowSize.y, mVideoSize);

            if(previewSize==null){
                previewSize = mVideoSize;
            }

            if (ManufacturerUtil.isSamsungGalaxyS3()) {
                parameters.setPreviewSize(ManufacturerUtil.SAMSUNG_S3_PREVIEW_WIDTH,
                        ManufacturerUtil.SAMSUNG_S3_PREVIEW_HEIGHT);
            } else {
                parameters.setPreviewSize(previewSize.getWidth(), previewSize.getHeight());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    parameters.setRecordingHint(true);
            }

            Size mStillShotSize = getHighestSupportedStillShotSize(Size.fromList(parameters.getSupportedPictureSizes()));
            parameters.setPictureSize(mStillShotSize.getWidth(), mStillShotSize.getHeight());

            setCameraDisplayOrientation(parameters);
            mCamera.setParameters(parameters);

            // NOTE: onFlashModesLoaded should not be called while modifying camera parameters as
            //       the flash parameters set in setupFlashMode will then be overwritten
            mFlashModes = CameraProfileProvider.getSupportedFlashModes(this.getActivity(), parameters);
            mmcc.setFlashModes(mFlashModes);
            onFlashModesLoaded();

            createPreview();
            mMediaRecorder = new MediaRecorder();

            onCameraOpened();
        } catch (IllegalStateException e) {
            Log.e(TAG, e.getMessage(),e);
            throwError(new Exception("Cannot access the camera.", e));
        } catch (RuntimeException e2) {
            Log.e(TAG, e2.getMessage(),e2);
            throwError(new Exception("Cannot access the camera, you may need to restart your device.", e2));
        }
    }

    private Size getHighestSupportedStillShotSize(List<Size> supportedPictureSizes) {
        Collections.sort(supportedPictureSizes, Size.bySize());
        Size maxSize = supportedPictureSizes.get(0);
        Log.d("CameraFragment", "Using resolution: " + maxSize.getWidth() + "x" + maxSize.getHeight());
        return maxSize;
    }

    @SuppressWarnings("WrongConstant")
    private void setCameraDisplayOrientation(Camera.Parameters parameters) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(getCurrentCameraId(), info);
        final int deviceOrientation = DisplayOrientation.getDisplayRotation(getActivity());
        mDisplayOrientation = DisplayOrientation.getDisplayOrientation(
                info.orientation, deviceOrientation, info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT);
        Log.d(TAG, String.format("Orientations: Sensor = %d˚, Device = %d˚, Display = %d˚",
                info.orientation, deviceOrientation, mDisplayOrientation));

        int previewOrientation;
        int jpegOrientation;
        if (ManufacturerUtil.isChromium()) {
            previewOrientation = 0;
            jpegOrientation = 0;
        } else {
            jpegOrientation = previewOrientation = mDisplayOrientation;

            if (DisplayOrientation.isPortrait(deviceOrientation) && getCurrentCameraPosition() == CAMERA_POSITION_FRONT)
                previewOrientation = Degrees.mirror(mDisplayOrientation);
        }

        parameters.setRotation(jpegOrientation);
        mCamera.setDisplayOrientation(previewOrientation);
    }

    private void createPreview() {
        Activity activity = getActivity();
        if (activity == null) return;
        if (mWindowSize == null)
            mWindowSize = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(mWindowSize);
        mPreviewView = new CameraPreview(getActivity(), mCamera);
        if (mPreviewFrame.getChildCount() > 0 && mPreviewFrame.getChildAt(0) instanceof CameraPreview)
            mPreviewFrame.removeViewAt(0);
        mPreviewFrame.addView(mPreviewView, 0);
        mPreviewView.setAspectRatio(mWindowSize.x, mWindowSize.y);
    }

    @Override
    public void closeCamera() {
        try {
            if (mCamera != null) {
                try {
                    mCamera.lock();
                } catch (Throwable ignored) {
                }
                mCamera.release();
                mCamera = null;
            }
        } catch (IllegalStateException e) {
            throwError(new Exception("Illegal state while trying to close camera.", e));
        }
    }

    private boolean prepareMediaRecorder() {
        try {
            final Activity activity = getActivity();
            if (null == activity) return false;
            final MaterialMediaCaptureContext mmcc = (MaterialMediaCaptureContext) activity;

            setCameraDisplayOrientation(mCamera.getParameters());
            mMediaRecorder = new MediaRecorder();
            mCamera.stopPreview();
            mCamera.unlock();
            mMediaRecorder.setCamera(mCamera);

            boolean canUseAudio = true;
            boolean audioEnabled = !mmcc.getCameraConfiguration().isAudioDisabled();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                canUseAudio = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;

            if (canUseAudio && audioEnabled) {
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            } else if (audioEnabled) {
                Toast.makeText(getActivity(), R.string.mcam_no_audio_access, Toast.LENGTH_LONG).show();
            }
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

            final CamcorderProfile profile = CameraProfileProvider.getCamcorderProfile(mmcc.getCameraConfiguration().getMediaQuality(), getCurrentCameraId());
            mMediaRecorder.setOutputFormat(profile.fileFormat);
            mMediaRecorder.setVideoFrameRate(mmcc.getCameraConfiguration().videoFrameRate(profile.videoFrameRate));
            mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
            mMediaRecorder.setVideoEncodingBitRate(mmcc.getCameraConfiguration().videoEncodingBitRate(profile.videoBitRate));
            mMediaRecorder.setVideoEncoder(profile.videoCodec);

            if (canUseAudio && audioEnabled) {
                mMediaRecorder.setAudioEncodingBitRate(mmcc.getCameraConfiguration().audioEncodingBitRate(profile.audioBitRate));
                mMediaRecorder.setAudioChannels(profile.audioChannels);
                mMediaRecorder.setAudioSamplingRate(profile.audioSampleRate);
                mMediaRecorder.setAudioEncoder(profile.audioCodec);
            }

            mOutputUri = getOutputMediaFile();
            //mOutputUri = uri.toString();
            mMediaRecorder.setOutputFile(mOutputUri.getPath());

            if (mmcc.getCameraConfiguration().getMaxFileSize() > 0) {
                mMediaRecorder.setMaxFileSize(mmcc.getCameraConfiguration().getMaxFileSize());
                mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                    @Override
                    public void onInfo(MediaRecorder mediaRecorder, int what, int extra) {
                        if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED) {
                            Toast.makeText(getActivity(), R.string.mcam_file_size_limit_reached, Toast.LENGTH_SHORT).show();
                            stopRecordingVideo(false);
                        }
                    }
                });
            }

            mMediaRecorder.setOrientationHint(mDisplayOrientation);
            mMediaRecorder.setPreviewDisplay(mPreviewView.getHolder().getSurface());

            try {
                mMediaRecorder.prepare();
                return true;
            } catch (Throwable e) {
                throwError(new Exception("Failed to prepare the media recorder: " + e.getMessage(), e));
                return false;
            }
        } catch (Throwable t) {
            try {
                mCamera.lock();
            } catch (IllegalStateException e) {
                Log.e(TAG, e.getMessage(),e);
                throwError(new Exception("Failed to re-lock camera: " + e.getMessage(), e));
                return false;
            }
            Log.e(TAG, t.getMessage(),t);
            throwError(new Exception("Failed to begin recording: " + t.getMessage(), t));
            return false;
        }
    }

    @Override
    public boolean startRecordingVideo() {
        super.startRecordingVideo();
        if (prepareMediaRecorder()) {
            try {
                // UI
                setImageRes(mButtonVideo, mInterface.getCameraStyleConfiguration().getIconStop());
                if (!ManufacturerUtil.isChromium())
                    mButtonFacing.setVisibility(View.GONE);

                // Only start counter if count down wasn't already started
                if (!mInterface.getCameraConfiguration().hasVideoDuration()) {
                    mInterface.setRecordingStart(System.currentTimeMillis());
                    startCounter();
                }

                // Start recording
                mMediaRecorder.start();

                mButtonVideo.setEnabled(false);
                mButtonVideo.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mButtonVideo.setEnabled(true);
                    }
                }, 200);

                return true;
            } catch (Throwable t) {
                Log.e(TAG, t.getMessage(),t);
                mInterface.setRecordingStart(-1);
                stopRecordingVideo(false);
                throwError(new Exception("Failed to start recording: " + t.getMessage(), t));
            }
        }
        return false;
    }

    @Override
    public void stopRecordingVideo(final boolean reachedZero) {
        super.stopRecordingVideo(reachedZero);

        if (mInterface.getCameraConfiguration().hasVideoDuration() && mInterface.getCameraStyleConfiguration().isAutoSubmit() &&
                (mInterface.getRecordingStart() < 0 || mMediaRecorder == null)) {
            stopCounter();
            if (mCamera != null) {
                try {
                    mCamera.lock();
                } catch (Throwable t) {
                    Log.e(TAG, t.getMessage(),t);
                }
            }
            releaseRecorder();
            closeCamera();
            mButtonFacing.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mInterface.onShowPreview(mOutputUri, MediaType.VIDEO_MP4, reachedZero);
                }
            }, 100);
            return;
        }

        if (mCamera != null)
            mCamera.lock();
        releaseRecorder();
        closeCamera();

        if (!mInterface.didRecord())
            mOutputUri = null;

        setImageRes(mButtonVideo, mInterface.getCameraStyleConfiguration().getIconRecord());
        if (!ManufacturerUtil.isChromium())
            mButtonFacing.setVisibility(View.VISIBLE);
        if (mInterface.getRecordingStart() > -1 && getActivity() != null)
            mInterface.onShowPreview(mOutputUri,  MediaType.VIDEO_MP4, reachedZero);

        stopCounter();
    }

    private void setupFlashMode() {
        String flashMode = null;
        switch (mInterface.getFlashMode()) {
            case FLASH_MODE_AUTO:
                flashMode = Camera.Parameters.FLASH_MODE_AUTO;
                break;
            case FLASH_MODE_ALWAYS_ON:
                flashMode = Camera.Parameters.FLASH_MODE_ON;
                break;
            case FLASH_MODE_OFF:
                flashMode = Camera.Parameters.FLASH_MODE_OFF;
            default:
                break;
        }
        if (flashMode != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFlashMode(flashMode);
            mCamera.setParameters(parameters);
        }
    }

    @Override
    public void onPreferencesUpdated() {
        setupFlashMode();
    }

    @Override
    public void takeStillshot() {
        Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
            public void onShutter() {
                //Log.d(TAG, "onShutter'd");
            }
        };
        Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                //Log.d(TAG, "onPictureTaken - raw. Raw is null: " + (data == null));
            }
        };
        Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(final byte[] data, Camera camera) {
                //Log.d(TAG, "onPictureTaken - jpeg, size: " + data.length);
                //final File outputPic = getOutputPictureFile();
                // lets save the image to disk
                mOutputUri = getOutputMediaFile();
                ImageSaver.create(new File(mOutputUri.getPath())).callback(new ImageSaver.ImageSaverCallback() {
                    @Override
                    public void onImageSaved(File imagePath) {
                        Log.d(TAG, "Picture saved to disk - jpeg, size: " + data.length);
                        //mOutputUri = Uri.fromFile(outputPic).toString();
                        mInterface.onShowStillshot(mOutputUri, MediaType.IMAGE_JPEG);
                        //mCamera.startPreview();
                        mButtonStillshot.setEnabled(true);
                    }

                    @Override
                    public void onImageSaveError(File imagePath, Exception e) {

                        throwError(e);
                    }
                }).start(data);
            }
        };

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            // We could have configurable shutter sound here
//            mCamera.enableShutterSound(false);
//        }

        mButtonStillshot.setEnabled(false);
        mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }
}