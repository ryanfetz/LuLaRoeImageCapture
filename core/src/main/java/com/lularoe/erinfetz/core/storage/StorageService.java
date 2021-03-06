package com.lularoe.erinfetz.core.storage;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.lularoe.erinfetz.core.storage.dir.DirectoryProvider;
import com.lularoe.erinfetz.core.storage.dir.StorageDirectory;
import com.lularoe.erinfetz.core.storage.files.FileExtensions;
import com.lularoe.erinfetz.core.storage.files.FileInfoProvider;
import com.lularoe.erinfetz.core.storage.files.MediaType;
import com.lularoe.erinfetz.core.storage.files.StoredFile;

import java.io.File;
import java.io.IOException;

public class StorageService {
    private static final String TAG = StorageService.class.getSimpleName();
    private final StorageUriProvider uriProvider;
    private final DirectoryProvider directoryProvider;
    private final FileExtensions fe;

    public StorageService(Context context, DirectoryProvider directoryProvider, StorageUriProvider uriProvider){
        this.uriProvider=uriProvider;
        this.directoryProvider = directoryProvider;
        this.fe = new FileExtensions(context);
    }

    public StorageUriProvider getUriProvider() {
        return uriProvider;
    }

    public static boolean isExternalMediaMounted(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    public StoredFile createFile(@NonNull FileInfoProvider fnProvider, @NonNull MediaType fileType, StorageDirectory dir) throws IllegalAccessException, IOException {
        FileInfoProvider.FileInfo fileInfo = fnProvider.provide();

        File file = File.createTempFile(fileInfo.name(), fe.toFileExtension(fileType,true), dir.getDirectory());
        Log.v(TAG, file.getAbsolutePath());

        Uri uri = getUri(file);
        Log.v(TAG, uri.toString());

        return new StoredFile(dir, file, fileType, uri, fileInfo.contentValues());
    }

    public StoredFile createFile(@NonNull FileInfoProvider fnProvider, @NonNull MediaType fileType, @NonNull String directoryType) throws IllegalAccessException, IOException {
        StorageDirectory dir = getDirectory(directoryType);

        return createFile(fnProvider, fileType, dir);
    }

    public StoredFile createFile(@NonNull FileInfoProvider fnProvider, @NonNull MediaType fileType, @NonNull String directoryType, @NonNull String subDirectory) throws IllegalAccessException, IOException {
        StorageDirectory dir = getDirectory(directoryType, subDirectory);

        return createFile(fnProvider, fileType, dir);
    }

    public Uri getUri(@NonNull File f) throws IllegalAccessException {
        Uri uri = null;
        try{
            uri = uriProvider.uri(f);
        }catch(IllegalArgumentException e){
            Log.e(TAG, e.getMessage(),e);

            throw new IllegalAccessException(String.format("Failed accessing %s via file uri provider.",f.getAbsolutePath()));
        }
        return uri;
    }

    public StorageDirectory getDirectory(@NonNull String type) throws IllegalAccessException {
        File d = directoryProvider.getDirectory(type);

        if(!ensureDirectory(d)){
            throw new IllegalAccessException(String.format("Failed accessing %s", d.getAbsolutePath()));
        }

        Log.v(TAG, d.getAbsolutePath());

        Uri uri = getUri(d);

        Log.v(TAG, uri.toString());

        return new StorageDirectory(d, uri);
    }

    public StorageDirectory getDirectory(@NonNull String type, @NonNull String subDirectory) throws IllegalAccessException {

        File d = directoryProvider.getDirectory(type, subDirectory);

        if(!ensureDirectory(d)){
            throw new IllegalAccessException(String.format("Failed accessing %s", d.getAbsolutePath()));
        }

        Log.v(TAG, d.getAbsolutePath());

        Uri uri = getUri(d);

        Log.v(TAG, uri.toString());

        return new StorageDirectory(d, uri);
    }

    public static boolean ensureDirectory(File storageDir){
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