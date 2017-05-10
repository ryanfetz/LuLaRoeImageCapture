package com.lularoe.erinfetz.core.storage.files;

import com.lularoe.erinfetz.core.ContentValuesBuilder;

public interface FileInfoProvider {
    FileInfo provide();

    public static interface FileInfo{
        String name();
        ContentValuesBuilder contentValues();
    }
}