package com.lularoe.erinfetz.imagecapture;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

import com.lularoe.erinfetz.imagecapture.storage.StoredImageFile;

import java.io.File;

public class MediaScannerUtils {
    private static final String TAG = MediaScannerUtils.class.getSimpleName();
    public MediaScannerUtils(){

    }

    public static void scanMediaIntentBroadcast(Context context, StoredImageFile f){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        mediaScanIntent.setData(f.getUri());
        context.sendBroadcast(mediaScanIntent);
    }

    public static void scanMediaBroadcast(Context context, StoredImageFile f){
        scanMediaBroadcast(context,new String[]{ f.getUri().getPath()});
    }

    public static void scanMediaBroadcast(Context context, String[] paths){
        MediaScannerConnection.scanFile(context, paths, null, new MediaScannerConnection.OnScanCompletedListener() {

            public void onScanCompleted(String path, Uri uri) {
                Log.e(TAG, "Scanned " + path + ":");
                Log.e(TAG, "-> uri=" + uri);
            }
        });
    }
}
