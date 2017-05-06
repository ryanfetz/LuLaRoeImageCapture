package com.lularoe.erinfetz.imagecapture;

import android.content.Context;
import android.net.Uri;

import java.io.File;

/**
 * Created by erinf_000 on 5/6/2017.
 */

public class ImageFileInfo {
    private File directory;
    private File file;
    private StorageType type;

    public ImageFileInfo(){

    }

    public ImageFileInfo(File d, File f,StorageType t){
        this.directory = d;
        this.file = f;
        this.type = t;
    }

    public static enum StorageType{
        EXTERNAL_PUBLIC,
        EXTERNAL_CAMERA,
        EXTERNAL,
        INTERNAL
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Uri getUri(Context context) {
        if(file==null)
            return null;

//        if(type.equals(StorageType.EXTERNAL_PUBLIC)){
//            return Uri.fromFile(file);
//        }
//
//        if(type.equals(StorageType.EXTERNAL_CAMERA)){
//            return Uri.fromFile(file);
//        }

        return StorageUtils.getFileURI(context, file);
    }

    public StorageType getType() {
        return type;
    }

    public void setType(StorageType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ImageFileInfo{" +
                "directory=" + directory +
                ", file=" + file +
                ", type=" + type +
                '}';
    }
}
