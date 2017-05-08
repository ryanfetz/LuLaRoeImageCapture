package com.lularoe.erinfetz.imagecapture.storage;

import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StorageService {

    private static final String TAG = StorageService.class.getSimpleName();
    private final Context context;

    public static final String FILE_PROVIDER ="com.lularoe.erinfetz.imagecapture.fileprovider";

    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    private static final String CAMERA_DIRECTORY = "/dcim/";

    public StorageService(Context context){
        this.context = context;
    }

    public StorageDirectory getStorageDirectory(String type, String album){
        File storageDir = null;
        StorageDirectory.StorageType st = StorageDirectory.StorageType.EXTERNAL;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //st = ImageFileInfo.StorageType.EXTERNAL_PUBLIC;
            //storageDir = getExternalStoragePublicDirectory(type, album);
            //if(!storageDir.exists()){
            //st = ImageFileInfo.StorageType.EXTERNAL_CAMERA;
            //storageDir = getExternalStorageDirectory(album);
            //if(!storageDir.exists()){
            st = StorageDirectory.StorageType.EXTERNAL;
            storageDir= getExternalFilesDir( type, album);
            //}
            //}

            Log.v(TAG, "External Storage Directory is " + storageDir);

        } else {
            Log.v(TAG, "External storage is not mounted READ/WRITE.");
            st = StorageDirectory.StorageType.INTERNAL;
            storageDir = getInternalFilesDir( type, album);

            Log.v(TAG, "Internal Storage Directory is " + storageDir);

        }

        return new StorageDirectory(storageDir, st);
    }

    public StoredImageFile createImageFile(String type, String album, String product, String size) throws IOException {
        // Create an image file name

        StorageDirectory dir = getStorageDirectory(type, album);

        String timeStamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());

        String imageFileName = size==null?
                String.format("%s_%s", product, timeStamp) :
                String.format("%s_%s_%s", product, size, timeStamp);

        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, dir.getDirectory());
        Log.v(TAG, dir.getDirectory().getAbsolutePath());
        Log.v(TAG, imageF.getAbsolutePath());
        Log.v(TAG, dir.getType().toString());

        return new StoredImageFile(dir, imageF, getFileUri(imageF, dir.getType()));
    }

    public Uri getFileUri(File file, StorageDirectory.StorageType st){
        if(st.equals(StorageDirectory.StorageType.EXTERNAL_PUBLIC)) {
            return getExternalFileURI(file);
        }
        if(st.equals(StorageDirectory.StorageType.EXTERNAL_CAMERA)) {
            return getExternalFileURI(file);
        }

        return getFileURI(file);
    }

    public Uri getFileURI(File file) {
        return FileProvider.getUriForFile(context, FILE_PROVIDER, file);
    }

    public Uri getExternalFileURI(File file) {
        return Uri.fromFile(file);
    }

    public File getInternalFilesDir(String type, String albumName){
        File storageDir = new File(context.getFilesDir(), albumName);
        if(createMediaStorageDir(storageDir)){
            Log.v(TAG, "Found/Created directory "+storageDir.getAbsolutePath());
        }else{
            Log.e(TAG, "Failed creating/finding directory "+storageDir.getAbsolutePath());
        }
        return storageDir;
    }

    public File getExternalFilesDir(String type, String albumName){
        File storageDir = new File(context.getExternalFilesDir(type),albumName);
        if(createMediaStorageDir(storageDir)){
            Log.v(TAG, "Found/Created directory "+storageDir.getAbsolutePath());
        }else{
            Log.e(TAG, "Failed creating/finding directory "+storageDir.getAbsolutePath());
        }
        return storageDir;
    }

    public File getExternalStorageDirectory(String albumName){

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

    public File getExternalStoragePublicDirectory(String type, String albumName){
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

    public boolean createMediaStorageDir(File storageDir){
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
