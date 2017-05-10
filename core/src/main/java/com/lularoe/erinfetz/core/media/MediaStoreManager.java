package com.lularoe.erinfetz.core.media;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.lularoe.erinfetz.core.ContentValuesBuilder;
import com.lularoe.erinfetz.core.storage.files.StoredFile;

import java.io.File;

public class MediaStoreManager {
    private static final String TAG = MediaStoreManager.class.getSimpleName();

    private final ContentResolver contentResolver;

    public MediaStoreManager(ContentResolver contentResolver){
        this.contentResolver = contentResolver;
    }

    public Uri insert(StoredFile file) {
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, file.getMetadata());
    }

    public boolean delete(File file){
        // Set up the projection (we only need the ID)
        String[] projection = { MediaStore.Images.Media._ID };

        // Match on the file path
        String selection = MediaStore.Images.Media.DATA + " = ?";
        String[] selectionArgs = new String[] { file.getAbsolutePath() };

        // Query for the ID of the media matching the file path
        Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor c = null;
        try {
            c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
            if (c.moveToFirst()) {
                // We found the ID. Deleting the item via the content provider will also remove the file
                long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                int rows = contentResolver.delete(deleteUri, null, null);
                Log.v(TAG, String.format("Deleted File %s, Id: %s, Rows: %s", file.getAbsolutePath(),  id, rows));
                return true;
            } else {
                // File not found in media store DB
                return false;
            }
        }finally {
            if(c!=null) {
                c.close();
            }
        }
    }
}
