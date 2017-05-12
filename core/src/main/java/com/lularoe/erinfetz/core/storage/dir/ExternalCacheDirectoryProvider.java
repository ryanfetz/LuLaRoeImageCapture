package com.lularoe.erinfetz.core.storage.dir;

import android.content.Context;
import android.os.Build;

import java.io.File;

public class ExternalCacheDirectoryProvider implements DirectoryProvider  {

    private final Context context;

    public ExternalCacheDirectoryProvider(Context context){
        this.context = context;
    }

    @Override
    public File getDirectory() {
        return context
                .getExternalCacheDir();
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
