package com.lularoe.erinfetz.core.storage;

import android.net.Uri;

import java.io.File;

public interface StorageUriProvider {
    Uri uri(String filePath);
    Uri uri(File file);
}
