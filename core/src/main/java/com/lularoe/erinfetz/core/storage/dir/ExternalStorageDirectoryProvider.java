package com.lularoe.erinfetz.core.storage.dir;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;

import com.lularoe.erinfetz.core.storage.StorageUriProvider;

import java.io.File;

public class ExternalStorageDirectoryProvider implements DirectoryProvider {

    private final Context context;

    public ExternalStorageDirectoryProvider(Context context){
        this.context = context;
    }

    @Override
    public File getDirectory() {
        return context.getExternalFilesDir(null);
    }

    @Override
    public File getDirectory(String type) {
        return context.getExternalFilesDir(type);
    }

    @Override
    public File getDirectory(String type, String sub) {
        return new File(context.getExternalFilesDir(type),sub);
    }
}
