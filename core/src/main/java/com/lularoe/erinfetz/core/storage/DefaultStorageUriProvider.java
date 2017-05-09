package com.lularoe.erinfetz.core.storage;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import java.io.File;

public class DefaultStorageUriProvider implements StorageUriProvider{
    private final String fileProvider;
    private final Context context;

    public DefaultStorageUriProvider(Context context, String fileProvider){
        this.fileProvider = fileProvider;
        this.context= context;
    }

    @Override
    public Uri uri(String filePath) {
        return uri(new File(filePath));
    }

    @Override
    public Uri uri(File file) {
        return FileProvider.getUriForFile(context, fileProvider, file);
    }
}
