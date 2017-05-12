package com.lularoe.erinfetz.core.storage.dir;

import android.content.Context;

import java.io.File;

public class InternalCacheDirectoryProvider implements DirectoryProvider {

    private final Context context;

    public InternalCacheDirectoryProvider(Context context){
        this.context = context;
    }

    @Override
    public File getDirectory() {
        return context.getCacheDir();
    }

    @Override
    public File getDirectory(String type) {
        return new File(getDirectory(), type);
    }

    @Override
    public File getDirectory(String type, String sub) {
        return new File(getDirectory(type), sub);
    }
}
