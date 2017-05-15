package com.lularoe.erinfetz.cameralibrary.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.media.CamcorderProfile;
import android.os.Build;

import com.google.common.base.Strings;
import com.lularoe.erinfetz.cameralibrary.base.material.MaterialMediaCaptureContext;
import com.lularoe.erinfetz.cameralibrary.types.Media;
import com.lularoe.erinfetz.cameralibrary.util.SizeUtil;

import java.util.ArrayList;
import java.util.List;

import static com.lularoe.erinfetz.cameralibrary.types.Media.*;
import static com.lularoe.erinfetz.cameralibrary.types.Cameras.*;

public class CameraProfileProvider {
    private CameraProfileProvider(){

    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static CamcorderProfile getCamcorderProfile(String cameraId, long maximumFileSize, int minimumDurationInSeconds) {
        if (Strings.isNullOrEmpty(cameraId)) {
            return null;
        }
        int cameraIdInt = Integer.parseInt(cameraId);
        return getCamcorderProfile(cameraIdInt, maximumFileSize, minimumDurationInSeconds);
    }

    public static CamcorderProfile getCamcorderProfile(int currentCameraId, long maximumFileSize, int minimumDurationInSeconds) {
        if (maximumFileSize <= 0)
            return CamcorderProfile.get(currentCameraId, MEDIA_QUALITY_HIGHEST);

        int[] qualities = new int[]{MEDIA_QUALITY_HIGHEST,
                MEDIA_QUALITY_HIGH, MEDIA_QUALITY_MEDIUM,
                MEDIA_QUALITY_LOW, MEDIA_QUALITY_LOWEST};

        CamcorderProfile camcorderProfile;
        for (int i = 0; i < qualities.length; ++i) {
            camcorderProfile = getCamcorderProfile(qualities[i], currentCameraId);
            double fileSize = SizeUtil.calculateApproximateVideoSize(camcorderProfile, minimumDurationInSeconds);

            if (fileSize > maximumFileSize) {
                long minimumRequiredBitRate = SizeUtil.calculateMinimumRequiredBitRate(camcorderProfile, maximumFileSize, minimumDurationInSeconds);

                if (minimumRequiredBitRate >= camcorderProfile.videoBitRate / 4 && minimumRequiredBitRate <= camcorderProfile.videoBitRate) {
                    camcorderProfile.videoBitRate = (int) minimumRequiredBitRate;
                    return camcorderProfile;
                }
            } else return camcorderProfile;
        }
        return getCamcorderProfile(MEDIA_QUALITY_LOWEST, currentCameraId);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static CamcorderProfile getCamcorderProfile(@Media.MediaQuality int mediaQuality, String cameraId) {
        if (Strings.isNullOrEmpty(cameraId)) {
            return null;
        }
        int cameraIdInt = Integer.parseInt(cameraId);
        return getCamcorderProfile(mediaQuality, cameraIdInt);
    }

    public static CamcorderProfile getCamcorderProfile(@Media.MediaQuality int mediaQuality, int cameraId) {
        if (Build.VERSION.SDK_INT > 10) {
            if (mediaQuality == MEDIA_QUALITY_HIGHEST) {
                return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_HIGH);
            } else if (mediaQuality == MEDIA_QUALITY_HIGH) {
                if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_1080P)) {
                    return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_1080P);
                } else if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_720P)) {
                    return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_720P);
                } else {
                    return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_HIGH);
                }
            } else if (mediaQuality == MEDIA_QUALITY_MEDIUM) {
                if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_720P)) {
                    return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_720P);
                } else if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_480P)) {
                    return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_480P);
                } else {
                    return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_LOW);
                }
            } else if (mediaQuality == MEDIA_QUALITY_LOW) {
                if (CamcorderProfile.hasProfile(cameraId, CamcorderProfile.QUALITY_480P)) {
                    return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_480P);
                } else {
                    return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_LOW);
                }
            } else if (mediaQuality == MEDIA_QUALITY_LOWEST) {
                return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_LOW);
            } else {
                return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_HIGH);
            }
        } else {
            if (mediaQuality == MEDIA_QUALITY_HIGHEST) {
                return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_HIGH);
            } else if (mediaQuality == MEDIA_QUALITY_HIGH) {
                return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_HIGH);
            } else if (mediaQuality == MEDIA_QUALITY_MEDIUM) {
                return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_LOW);
            } else if (mediaQuality == MEDIA_QUALITY_LOW) {
                return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_LOW);
            } else if (mediaQuality == MEDIA_QUALITY_LOWEST) {
                return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_LOW);
            } else {
                return CamcorderProfile.get(cameraId, CamcorderProfile.QUALITY_HIGH);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static List<Integer> getSupportedFlashModes(Context context, Camera.Parameters parameters) {
        //check has system feature for flash
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            List<String> modes = parameters.getSupportedFlashModes();
            if (modes == null || (modes.size() == 1 && modes.get(0).equals(Camera.Parameters.FLASH_MODE_OFF))) {
                return null; //not supported
            } else {
                ArrayList<Integer> flashModes = new ArrayList<>();
                for (String mode : modes) {
                    switch (mode) {
                        case Camera.Parameters.FLASH_MODE_AUTO:
                            if (!flashModes.contains(FLASH_MODE_AUTO))
                                flashModes.add(FLASH_MODE_AUTO);
                            break;
                        case Camera.Parameters.FLASH_MODE_ON:
                            if (!flashModes.contains(FLASH_MODE_ALWAYS_ON))
                                flashModes.add(FLASH_MODE_ALWAYS_ON);
                            break;
                        case Camera.Parameters.FLASH_MODE_OFF:
                            if (!flashModes.contains(FLASH_MODE_OFF))
                                flashModes.add(FLASH_MODE_OFF);
                            break;
                        default:
                            break;
                    }
                }
                return flashModes;
            }
        } else {
            return null; //not supported
        }
    }

    // TODO: Take a hard look at how this works
    // Camera2
    public static List<Integer> getSupportedFlashModes(Context context, CameraCharacteristics characteristics) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return null; //doesn't support camera2
        } else if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            Boolean flashAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
            if (flashAvailable == null || !flashAvailable)
                return null;

            int[] modes = characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES);
            if (modes == null || (modes.length == 1 && modes[0] == CameraCharacteristics.CONTROL_AE_MODE_OFF)) {
                return null; //not supported
            } else {
                ArrayList<Integer> flashModes = new ArrayList<>(3);
                for (int mode : modes) {
                    switch (mode) {
                        case CameraCharacteristics.CONTROL_AE_MODE_ON_AUTO_FLASH:
                            if (!flashModes.contains(FLASH_MODE_AUTO))
                                flashModes.add(FLASH_MODE_AUTO);
                            break;
                        case CameraCharacteristics.CONTROL_AE_MODE_ON_ALWAYS_FLASH:
                            if (!flashModes.contains(FLASH_MODE_ALWAYS_ON))
                                flashModes.add(FLASH_MODE_ALWAYS_ON);
                            break;
                        case CameraCharacteristics.CONTROL_AE_MODE_ON:
                            if (!flashModes.contains(FLASH_MODE_OFF))
                                flashModes.add(FLASH_MODE_OFF);
                        default:
                            break;
                    }
                }
                return flashModes;
            }
        }
        return null; //not supported
    }
}
