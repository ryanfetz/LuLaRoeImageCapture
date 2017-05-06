package com.lularoe.erinfetz.imagecapture;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StorageUtils {

    private static final String TAG = StorageUtils.class.getSimpleName();

    public static final String FILE_PROVIDER ="com.lularoe.erinfetz.imagecapture.fileprovider";

    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    private static final String CAMERA_DIRECTORY = "/dcim/";

    private StorageUtils(){

    }

    public static ImageFileInfo createImageFile(Context context, String type, String album, String product, String size) throws IOException {
        // Create an image file name

        File storageDir = null;
        ImageFileInfo.StorageType st = ImageFileInfo.StorageType.EXTERNAL_PUBLIC;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //st = ImageFileInfo.StorageType.EXTERNAL_PUBLIC;
            //storageDir = getExternalStoragePublicDirectory(type, album);
            //if(!storageDir.exists()){
                //st = ImageFileInfo.StorageType.EXTERNAL_CAMERA;
                //storageDir = getExternalStorageDirectory(album);
                //if(!storageDir.exists()){
                    st = ImageFileInfo.StorageType.EXTERNAL;
                    storageDir= getExternalFilesDir(context, type, album);
                //}
            //}

            Log.v(TAG, "External Storage Directory is " + storageDir.getAbsolutePath());

        } else {
            Log.v(TAG, "External storage is not mounted READ/WRITE.");
            st = ImageFileInfo.StorageType.INTERNAL;
            storageDir = getInternalFilesDir(context, type, album);

            Log.v(TAG, "Internal Storage Directory is " + storageDir.getAbsolutePath());

        }

        String timeStamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());

        String imageFileName = size==null?
                String.format("%s_%s", product, timeStamp) :
                String.format("%s_%s_%s", product, size, timeStamp);

        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, storageDir);
        Log.v(TAG, storageDir.getAbsolutePath());
        Log.v(TAG, imageF.getAbsolutePath());
        Log.v(TAG, st.toString());
        return new ImageFileInfo(storageDir, imageF, st);
    }

    public static Uri getFileURI(Context context, File photoFile){
        return FileProvider.getUriForFile(context, FILE_PROVIDER, photoFile);
    }

    public static File getInternalFilesDir(Context context, String type, String albumName){
        File storageDir = new File(context.getFilesDir(), albumName);
        if(createMediaStorageDir(storageDir)){
            Log.v(TAG, "Found/Created directory "+storageDir.getAbsolutePath());
        }else{
            Log.e(TAG, "Failed creating/finding directory "+storageDir.getAbsolutePath());
        }
        return storageDir;
    }

    public static File getExternalFilesDir(Context context, String type, String albumName){
        File storageDir = new File(context.getExternalFilesDir(type),albumName);
        if(createMediaStorageDir(storageDir)){
            Log.v(TAG, "Found/Created directory "+storageDir.getAbsolutePath());
        }else{
            Log.e(TAG, "Failed creating/finding directory "+storageDir.getAbsolutePath());
        }
        return storageDir;
    }

    public static File getExternalStorageDirectory(String albumName){

        File storageDir = new File(Environment.getExternalStorageDirectory()
                + CAMERA_DIRECTORY
                + albumName);

        if(createMediaStorageDir(storageDir)){
            Log.v(TAG, "Found/Created directory "+storageDir.getAbsolutePath());
        }else{
            Log.e(TAG, "Failed creating/finding directory "+storageDir.getAbsolutePath());
        }

        return storageDir;
    }

    public static File getExternalStoragePublicDirectory(String type, String albumName){
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(type),
                albumName);

        if(createMediaStorageDir(storageDir)){
            Log.v(TAG, "Found/Created directory "+storageDir.getAbsolutePath());
        }else{
            Log.e(TAG, "Failed creating/finding directory "+storageDir.getAbsolutePath());
        }

        return storageDir;
    }

    public static boolean createMediaStorageDir(File storageDir){
        if(storageDir==null){
            return false;
        }

        if (storageDir != null) {
            if (! storageDir.mkdirs()) {
                if (! storageDir.exists()){
                    Log.e(TAG, "failed to create directory " + storageDir.getAbsolutePath());
                    return false;
                }
            }
        }

        return storageDir.exists();
    }
}

