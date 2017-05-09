package com.lularoe.erinfetz.core.storage.dir;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;

import com.google.common.base.Strings;

import java.io.File;

public class InternalStorageDirectoryProvider implements DirectoryProvider {

    private final Context context;
    public InternalStorageDirectoryProvider(Context context){
        this.context = context;
    }

    @Override
    public File getDirectory() {
        return context.getFilesDir();
    }

    @Override
    public File getDirectory(String type) {
        return new File(context.getFilesDir(), type);
    }

    @Override
    public File getDirectory(String type, String sub) {
        return new File(getDirectory(type), sub);
    }
}
