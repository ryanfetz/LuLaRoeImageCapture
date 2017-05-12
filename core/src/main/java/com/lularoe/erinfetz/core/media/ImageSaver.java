package com.lularoe.erinfetz.core.media;

import android.annotation.TargetApi;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ImageSaver {
    public static final String TAG = ImageSaver.class.getSimpleName();

    public interface ImageSaverCallback {
        void onImageSaved(File imagePath);

        void onImageSaveError(File imagePath, Exception e);
    }


    private final File file;
    private ImageSaverCallback imageSaverCallback;

    private ImageSaver(@NonNull File file){
        this.file= file;
    }

    public ImageSaver callback(@NonNull ImageSaverCallback c){
        this.imageSaverCallback = c;
        return this;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public ImageReader.OnImageAvailableListener buildListener() {
        return new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Image image = reader.acquireNextImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                final byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);

                FileOutputStream output = null;
                try {
                    output = new FileOutputStream(file);
                    output.write(bytes);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                    imageSaverCallback.onImageSaveError(file, e);
                } finally {

                    image.close();
                    if (null != output) {
                        try {
                            output.flush();
                            output.close();
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }
                imageSaverCallback.onImageSaved(file);
            }
        };
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public Runnable buildRunnable(final Image image){
        return new Runnable() {
            @Override
            public void run() {
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                FileOutputStream output = null;
                try {
                    output = new FileOutputStream(file);
                    output.write(bytes);
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(),e);
                    imageSaverCallback.onImageSaveError(file, e);
                } finally {
                    image.close();
                    if (null != output) {
                        try {
                            output.flush();
                            output.close();
                        } catch (IOException e) {
                            Log.e(TAG, "Can't release image or close the output stream.");
                        }
                    }
                }
                imageSaverCallback.onImageSaved(file);
            }
        };
    }

    public void start(final byte[] input){
        final Handler handler = new Handler();
        new Thread() {
            @Override
            public void run() {
                FileOutputStream outputStream =null;
                try {
                    outputStream = new FileOutputStream(file);
                    outputStream.write(input);
                    outputStream.flush();
                    outputStream.close();
                    outputStream=null;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageSaverCallback.onImageSaved(null);
                        }
                    });
                } catch (final Exception e) {
                    Log.e(TAG, e.getMessage(),e);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageSaverCallback.onImageSaveError(file, e);
                        }
                    });
                }finally{
                    if(outputStream!=null){
                        try{
                            outputStream.flush();
                            outputStream.close();
                        }catch(Throwable e){

                        }
                    }
                }
            }
        }.start();
    }
}
