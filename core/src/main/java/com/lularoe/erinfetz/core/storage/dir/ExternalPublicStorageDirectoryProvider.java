package com.lularoe.erinfetz.core.storage.dir;

import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.lularoe.erinfetz.core.storage.StorageUriProvider;

import java.io.File;

public class ExternalPublicStorageDirectoryProvider implements DirectoryProvider {
    @Override
    public File getDirectory() {
        return Environment.getExternalStorageDirectory();
    }

    @Override
    public File getDirectory(String type) {
        return Environment.getExternalStoragePublicDirectory(type);
    }

    @Override
    public File getDirectory(String type, String sub) {
        return new File(Environment.getExternalStoragePublicDirectory(type), sub);
    }
}
