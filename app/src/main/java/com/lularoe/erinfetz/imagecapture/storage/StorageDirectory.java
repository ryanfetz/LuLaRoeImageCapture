package com.lularoe.erinfetz.imagecapture.storage;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * Created by ryan.fetz on 5/8/2017.
 */

public class StorageDirectory implements Parcelable{
    private StorageType type;
    private File directory;

    public static enum StorageType{
        EXTERNAL_PUBLIC,
        EXTERNAL_CAMERA,
        EXTERNAL,
        INTERNAL
    }

    public StorageDirectory(File d, StorageType s){
        this.type = s;
        this.directory = d;
    }

    public StorageType getType() {
        return type;
    }

    public void setType(StorageType type) {
        this.type = type;
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    @Override
    public String toString() {
        return "StorageDirectory{" +
                "type=" + type +
                ", directory=" + directory.getAbsolutePath() +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(type.name());
        out.writeString(directory.getAbsolutePath());
    }
    private StorageDirectory(Parcel in) {
        type = StorageType.valueOf(in.readString());
        directory = new File(in.readString());
    }

    public static final Parcelable.Creator<StorageDirectory> CREATOR
            = new Parcelable.Creator<StorageDirectory>() {
        @Override
        public StorageDirectory createFromParcel (Parcel in) {
            return new StorageDirectory(in);
        }

        @Override
        public StorageDirectory[] newArray ( int size) {
            return new StorageDirectory[size];
        }
    };
}
