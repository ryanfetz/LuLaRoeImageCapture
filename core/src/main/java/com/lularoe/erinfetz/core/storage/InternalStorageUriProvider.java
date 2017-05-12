package com.lularoe.erinfetz.core.storage;

import android.net.Uri;

import java.io.File;

public class InternalStorageUriProvider implements StorageUriProvider {
    @Override
    public Uri uri(String filePath) {
        return Uri.fromFile(new File(filePath));
    }

    @Override
    public Uri uri(File file) {
        return Uri.fromFile(file);
    }
}
