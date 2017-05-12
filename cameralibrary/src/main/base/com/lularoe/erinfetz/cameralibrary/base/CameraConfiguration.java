package com.lularoe.erinfetz.cameralibrary.base;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.widget.Button;

import com.lularoe.erinfetz.cameralibrary.CameraActivityManager;
import com.lularoe.erinfetz.cameralibrary.types.Cameras;
import com.lularoe.erinfetz.cameralibrary.types.Media;
import com.lularoe.erinfetz.core.DateTimeUtils;

public class CameraConfiguration implements Parcelable {

    public static final int UNDEFINIED_INTEGER = -1;
    public static final int UNDEFINIED_LONG = -1;

    public static final String INTENT_KEY = "cameraConfig";

    public static final int DEFAULT_VIDEO_PREF_HEIGHT = 720;
    public static final float DEFAULT_VIDEO_PREF_ASPECT = 4f / 3f;

    protected long videoDuration = UNDEFINIED_LONG;
    protected long minimumVideoDuration = UNDEFINIED_LONG;

    protected int videoEncodingBitRate = UNDEFINIED_INTEGER;
    protected int audioEncodingBitRate = UNDEFINIED_INTEGER;
    protected int videoFrameRate = UNDEFINIED_INTEGER;
    protected int videoPreferredHeight = DEFAULT_VIDEO_PREF_HEIGHT;
    protected float videoPreferredAspect = DEFAULT_VIDEO_PREF_ASPECT;

    protected long maxFileSize = UNDEFINIED_LONG;

    private long autoRecord = UNDEFINIED_LONG;

    @Media.MediaAction
    protected int mediaAction = Media.MEDIA_ACTION_PHOTO;

    @Media.MediaQuality
    protected int mediaQuality = Media.MEDIA_QUALITY_HIGHEST;

    @Cameras.CameraPosition
    protected int cameraPosition = Cameras.CAMERA_POSITION_BACK;

    @Cameras.FlashMode
    protected int flashMode = Cameras.FLASH_MODE_AUTO;

    private boolean audioDisabled = false;
    private boolean forceCamera1 = false;

    public long getVideoDuration() {
        return videoDuration;
    }

    public long getMinimumVideoDuration() {
        return minimumVideoDuration;
    }

    public int getVideoEncodingBitRate() {
        return videoEncodingBitRate;
    }

    public int getAudioEncodingBitRate() {
        return audioEncodingBitRate;
    }

    public int getVideoFrameRate() {
        return videoFrameRate;
    }

    public int getVideoPreferredHeight() {
        return videoPreferredHeight;
    }

    public float getVideoPreferredAspect() {
        return videoPreferredAspect;
    }

    public int videoEncodingBitRate(int defaultVal){
        if(videoEncodingBitRate== UNDEFINIED_INTEGER){
            return defaultVal;
        }
        return videoEncodingBitRate;
    }

    public int audioEncodingBitRate(int defaultVal){
        if(audioEncodingBitRate== UNDEFINIED_INTEGER){
            return defaultVal;
        }
        return audioEncodingBitRate;
    }

    public int videoFrameRate(int defaultVal){
        if(videoFrameRate== UNDEFINIED_INTEGER){
            return defaultVal;
        }
        return videoFrameRate;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    @Media.MediaAction
    public int getMediaAction() {
        return mediaAction;
    }

    @Media.MediaQuality
    public int getMediaQuality() {
        return mediaQuality;
    }

    @Cameras.CameraPosition
    public int getCameraPosition() {
        return cameraPosition;
    }

    @Cameras.FlashMode
    public int getFlashMode() {
        return flashMode;
    }

    public boolean isAudioDisabled() {
        return audioDisabled;
    }

    public long getAutoRecord() {
        return autoRecord;
    }

    public boolean isForceCamera1() {
        return forceCamera1;
    }

    public static class Builder{
        private CameraConfiguration c;

        public Builder(){
            c = new CameraConfiguration();
        }

        public CameraConfiguration build(){
            return c;
        }

        public Builder mediaAction(@Media.MediaAction int mediaAction) {
            c.mediaAction = mediaAction;
            return this;
        }

        public Builder mediaQuality(@Media.MediaQuality int mediaQuality) {
            c.mediaQuality = mediaQuality;
            return this;
        }

        public Builder flashMode(@Cameras.FlashMode int flashMode) {
            c.flashMode = flashMode;
            return this;
        }

        public Builder cameraPosition(@Cameras.CameraPosition int camera){
            c.cameraPosition = camera;
            return this;
        }

        public Builder videoDuration(long lengthLimitMs) {
            c.videoDuration = lengthLimitMs;
            return this;
        }

        public Builder videoDurationSeconds(float lengthLimitSec) {
            return videoDuration(DateTimeUtils.secondsToMillis(lengthLimitSec));
        }

        public Builder videoDurationMinutes(float lengthLimitMin) {
            return videoDuration(DateTimeUtils.minutesToMillis(lengthLimitMin));
        }

        public Builder minimumVideoDuration(long lengthLimitMs) {
            c.videoDuration = lengthLimitMs;
            return this;
        }

        public Builder minimumVideoDurationSeconds(float lengthLimitSec) {
            return minimumVideoDuration(DateTimeUtils.secondsToMillis(lengthLimitSec));
        }

        public Builder minimumVideoDurationMinutes(float lengthLimitMin) {
            return minimumVideoDuration(DateTimeUtils.minutesToMillis(lengthLimitMin));
        }

        public Builder autoRecordWithDelayMs(@IntRange(from = -1, to = Long.MAX_VALUE) long delayMillis) {
            c.autoRecord = delayMillis;
            return this;
        }

        public Builder autoRecordWithDelaySec(@IntRange(from = -1, to = Long.MAX_VALUE) int delaySeconds) {
            c.autoRecord = delaySeconds * DateTimeUtils.SECOND;
            return this;
        }

        public Builder videoEncodingBitRate(@IntRange(from = 1, to = Integer.MAX_VALUE) int rate) {
            c.videoEncodingBitRate = rate;
            return this;
        }

        public Builder audioEncodingBitRate(@IntRange(from = 1, to = Integer.MAX_VALUE) int rate) {
            c.audioEncodingBitRate = rate;
            return this;
        }

        public Builder videoFrameRate(@IntRange(from = 1, to = Integer.MAX_VALUE) int rate) {
            c.videoFrameRate = rate;
            return this;
        }

        public Builder videoPreferredHeight(@IntRange(from = 1, to = Integer.MAX_VALUE) int height) {
            c.videoPreferredHeight = height;
            return this;
        }

        public Builder videoPreferredAspect(@FloatRange(from = 0.1, to = Float.MAX_VALUE) float ratio) {
            c.videoPreferredAspect = ratio;
            return this;
        }

        public Builder maxAllowedFileSize(long size) {
            c.maxFileSize = size;
            return this;
        }

        public Builder audioDisabled(boolean audioDisabled) {
            c.audioDisabled = audioDisabled;
            return this;
        }

        public Builder forceCamera1(boolean forceCamera1){
            c.forceCamera1 = forceCamera1;
            return this;
        }
    }

    protected CameraConfiguration(){

    }

    @SuppressWarnings("WrongConstant")
    protected CameraConfiguration(Parcel in) {
        mediaAction = in.readInt();
        videoEncodingBitRate = in.readInt();
        audioEncodingBitRate = in.readInt();
        videoFrameRate = in.readInt();
        videoPreferredHeight = in.readInt();
        videoPreferredAspect = in.readFloat();
        videoDuration = in.readLong();
        minimumVideoDuration = in.readLong();
        maxFileSize = in.readLong();
        mediaQuality = in.readInt();
        cameraPosition = in.readInt();
        flashMode = in.readInt();
        audioDisabled = in.readByte() != 0;
        forceCamera1 = in.readByte()!=0;
        autoRecord=in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mediaAction);
        dest.writeInt(videoEncodingBitRate);
        dest.writeInt(audioEncodingBitRate);
        dest.writeInt(videoFrameRate);
        dest.writeInt(videoPreferredHeight);
        dest.writeFloat(videoPreferredAspect);
        dest.writeLong(videoDuration);
        dest.writeLong(minimumVideoDuration);
        dest.writeLong(maxFileSize);
        dest.writeInt(mediaQuality);
        dest.writeInt(cameraPosition);
        dest.writeInt(flashMode);
        dest.writeByte((byte) (audioDisabled ? 1 : 0));
        dest.writeByte((byte)(forceCamera1?1:0));
        dest.writeLong(autoRecord);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CameraConfiguration> CREATOR = new Creator<CameraConfiguration>() {
        @Override
        public CameraConfiguration createFromParcel(Parcel in) {
            return new CameraConfiguration(in);
        }

        @Override
        public CameraConfiguration[] newArray(int size) {
            return new CameraConfiguration[size];
        }
    };
}
