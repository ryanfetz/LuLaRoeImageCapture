package com.lularoe.erinfetz.cameralibrary.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lularoe.erinfetz.cameralibrary.R;
import com.lularoe.erinfetz.cameralibrary.util.DisplayOrientation;
import com.lularoe.erinfetz.cameralibrary.util.SizeUtil;
import com.lularoe.erinfetz.core.graphics.Size;

/**
 *
 */
public class VideoStreamView extends SurfaceView implements SurfaceHolder.Callback,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnVideoSizeChangedListener {

    public static final String TAG = VideoStreamView.class.getSimpleName();

    @Override
    public void onPrepared(MediaPlayer mp) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                start(mContext);
                if (!mAutoPlay) pause();
            }
        }, 250);
        if (mCallback != null)
            mCallback.onPrepared(mp);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mCallback != null)
            mCallback.onCompleted();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mCallback.onError(mp, what, extra);
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (mCallback != null)
            mCallback.onBuffer(percent);
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        requestLayout();
    }

    public interface Callback {
        void onPrepared(MediaPlayer mp);

        void onCompleted();

        void onError(MediaPlayer mp, int what, int extra);

        void onBuffer(int percent);
    }

    public VideoStreamView(Context context) {
        super(context);
        initPlayer();
    }

    public VideoStreamView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPlayer();
    }

    public VideoStreamView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPlayer();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoStreamView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPlayer();
    }

    public void saveInstanceState(Bundle to) {
        to.putParcelable("uri", mUri);
    }

    public void restoreInstanceState(Bundle from, Callback callback) {
        if (from != null) {
            mUri = from.getParcelable("uri");
            mCallback = callback;
        }
    }

    private void initPlayer() {
        if (isInEditMode()) return;
        else if (mPlayer != null) {
            if (mPlayer.isPlaying())
                mPlayer.stop();
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    private Activity mContext;
    protected Uri mUri;
    private Callback mCallback;
    protected MediaPlayer mPlayer;
    private boolean mAutoPlay;

    public void setAutoPlay(boolean autoPlay) {
        mAutoPlay = autoPlay;
    }

    public void setURI(@NonNull Activity context, @NonNull Uri uri, @NonNull Callback callback) {
        mContext = context;
        mUri = uri;
        mCallback = callback;
        initPlayer();
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnBufferingUpdateListener(this);
        mPlayer.setOnVideoSizeChangedListener(this);
        try {
            mPlayer.setDataSource(context, uri);
            mPlayer.prepareAsync();
        } catch (Throwable e) {
            Log.d(TAG, "Failed to setDataSource/prepareAsync: " + e.getMessage());
            Log.e(TAG, e.getMessage(),e);
            new MaterialDialog.Builder(mContext)
                    .title(R.string.mcam_error)
                    .content(e.getMessage())
                    .positiveText(android.R.string.ok)
                    .show();
        }
    }

    public boolean start(Activity context) {
        mContext = context;
        if (mPlayer == null) {
            initPlayer();
            setURI(mContext, mUri, mCallback);
            return false;
        }
        try {
            mPlayer.setDisplay(getHolder());
            mPlayer.start();
        } catch (IllegalArgumentException | IllegalStateException e) {
            Log.e(TAG, e.getMessage(),e);
        }
        return true;
    }

    public void seekTo(int msec) {
        if (mPlayer == null) return;
        mPlayer.seekTo(msec);
    }

    public int getCurrentPosition() {
        if (mPlayer == null)
            return -1;
        final int currentPosition = mPlayer.getCurrentPosition();
        int currentPositionAdjusted = currentPosition - 500;
        if (currentPositionAdjusted < 0) currentPositionAdjusted = 0;
        return currentPositionAdjusted;
    }

    public int getDuration() {
        if (mPlayer == null)
            return -1;
        return mPlayer.getDuration();
    }

    public boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    public void pause() {
        if (mPlayer != null)
            mPlayer.pause();
    }

    public void stop() {
        if (mPlayer != null)
            mPlayer.stop();
    }

    public void release() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying())
                mPlayer.stop();
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mPlayer.setDisplay(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        holder.addCallback(this);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        holder.removeCallback(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mPlayer != null)
            mPlayer.release();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        try {
            if (mPlayer != null) {
                final float videoWidth = (float) mPlayer.getVideoWidth();
                final float videoHeight = (float) mPlayer.getVideoHeight();
                if (videoWidth == 0 || videoHeight == 0) {
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                    return;
                }
                Size dimensions = SizeUtil.getDimensions(DisplayOrientation.getScreenOrientation(mContext), videoWidth, videoHeight, this);
                setMeasuredDimension(dimensions.getWidth(), dimensions.getHeight());
            }
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage(),e);
        }
    }
}