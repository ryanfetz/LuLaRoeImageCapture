package com.lularoe.erinfetz.core.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class ImageLoader {
    public static final String TAG = ImageLoader.class.getSimpleName();

    private Context context;
    private String inputFile;
    private ImageLoaderListener listener;

    private ImageLoader(@NonNull Context context) {
        this.context = context;
    }

    public static ImageLoader create(@NonNull Context context){
        return new ImageLoader(context);
    }

    public ImageLoader file(String inputFile) {
        this.inputFile = inputFile;
        return this;
    }
    public ImageLoader listener(ImageLoaderListener listener) {
        this.listener = listener;
        return this;
    }

    public static interface ImageLoaderListener{
        public void onImageLoaded(Bitmap bitmap, String inputFile);
        public void onImageLoadError(String inputFile, Exception e);
    }


    public AsyncTask<Void, Void, Boolean> task(final ImageView target) {
        return new BitmapLoadTask(target, inputFile, listener);
    }

    public void into(final ImageView target) {
        ViewTreeObserver viewTreeObserver = target.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                target.getViewTreeObserver().removeOnPreDrawListener(this);

                new ImageLoaderThread(context, inputFile, listener).start();

                return true;
            }
        });
    }

    private static class BitmapLoadTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<ImageLoaderListener> listener;
        private String inputFile;

        private Bitmap bitmap;
        private Exception exception;

        private int imageViewHeight;
        private int imageViewWidth;

        BitmapLoadTask(ImageView target, String inputFile, ImageLoaderListener listener) {
            this.inputFile = inputFile;
            imageViewHeight = target.getHeight();
            imageViewWidth = target.getWidth();
            this.listener = new WeakReference<>(listener);
            //imageViewHeight = target.getMeasuredHeight();
            //imageViewWidth = target.getMeasuredWidth();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                bitmap = BitmapTransformations
                        .rotatedBitmap(imageViewWidth, imageViewHeight)
                        .apply(inputFile);
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Failed to load bitmap", e);
                this.exception = e;
                return false;
            } catch (OutOfMemoryError e) {
                Log.e(TAG, "Failed to load bitmap - OutOfMemoryError", e);
                this.exception = new RuntimeException(e);
                return false;
            } catch (Throwable e) {
                Log.e(TAG, "Failed to load bitmap - Throwable", e);
                this.exception = new RuntimeException(e);
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean b) {
            ImageLoaderListener l = listener.get();
            if(b) {
                if (l != null) {
                    l.onImageLoaded(bitmap, inputFile);
                }
            }else{
                if(l!=null && exception!=null){
                    l.onImageLoadError(inputFile,exception);
                }
            }
        }
    }


    private static class ImageLoaderThread extends Thread {
        private WeakReference<Context> context;
        private WeakReference<ImageLoaderListener> listener;
        private String inputFile;

        private ImageLoaderThread(Context context, String inputFile, ImageLoaderListener listener) {
            this.context = new WeakReference<>(context);
            this.listener = new WeakReference<>(listener);
            this.inputFile = inputFile;
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            ImageLoaderListener l  = listener.get();
            try{
                Context c = context.get();
                if(c!=null) {

                    WindowManager windowManager = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
                    Display display = windowManager.getDefaultDisplay();

                    Point size = new Point();
                    display.getSize(size);
                    int imageViewHeight = size.y;
                    int imageViewWidth = size.x;

                    final Bitmap resultBitmap = BitmapTransformations
                            .rotatedBitmap(imageViewWidth, imageViewHeight)
                            .apply(inputFile);

                    if(l!=null){
                        l.onImageLoaded(resultBitmap, inputFile);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to load bitmap", e);

                if(l!=null){
                    l.onImageLoadError(inputFile,e);
                }
            } catch (OutOfMemoryError e) {
                Log.e(TAG, "Failed to load bitmap - OutOfMemoryError", e);
                Exception exception = new RuntimeException(e);
                if(l!=null){
                    l.onImageLoadError(inputFile,exception);
                }
            } catch (Throwable e) {
                Log.e(TAG, "Failed to load bitmap - Throwable", e);
                Exception exception = new RuntimeException(e);
                if(l!=null){
                    l.onImageLoadError(inputFile,exception);
                }
            }
        }
    }
}
