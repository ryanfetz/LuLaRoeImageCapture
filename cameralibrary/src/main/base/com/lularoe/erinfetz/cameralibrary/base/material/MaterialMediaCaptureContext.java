package com.lularoe.erinfetz.cameralibrary.base.material;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.lularoe.erinfetz.cameralibrary.base.CameraOrientation;
import com.lularoe.erinfetz.cameralibrary.types.Cameras;
import com.lularoe.erinfetz.cameralibrary.base.CameraConfiguration;
import com.lularoe.erinfetz.core.storage.files.MediaType;

import java.util.List;

public interface MaterialMediaCaptureContext {
    void onRetry(@Nullable Uri outputUri);

    void onShowPreview(@Nullable Uri outputUri, MediaType mediaType, boolean countdownIsAtZero);

    void onShowStillshot(Uri outputUri, MediaType mediaType);

    void useMedia(Uri uri, MediaType mediaType);

    void setRecordingStart(long start);

    void setRecordingEnd(long end);

    long getRecordingStart();

    long getRecordingEnd();

    CameraOrientation getCameraOrientation();

    CameraConfiguration getCameraConfiguration();

    MaterialCameraStyleConfiguration getCameraStyleConfiguration();

    Object getFrontCamera();
    void setFrontCamera(Object id);

    Object getBackCamera();
    void setBackCamera(Object id);

    Object getCurrentCameraId();

    @Cameras.FlashMode
    int getFlashMode();
    void setFlashModes(List<Integer> modes);

    boolean shouldHideFlash();

    void toggleFlashMode();


    boolean didRecord();
    void setDidRecord(boolean didRecord);

    @Cameras.CameraPosition
    int getCurrentCameraPosition();

    void setCameraPosition(@Cameras.CameraPosition int position);

    void toggleCameraPosition();

    boolean useStillshot();
}
