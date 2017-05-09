package com.lularoe.erinfetz.core.storage.dir;

import java.io.File;

public interface DirectoryProvider {
    File getDirectory();
    File getDirectory(String type);
    File getDirectory(String type, String sub);
}
