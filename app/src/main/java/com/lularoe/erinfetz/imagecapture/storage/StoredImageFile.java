package com.lularoe.erinfetz.imagecapture.storage;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * Created by erinf_000 on 5/6/2017.
 */

public class StoredImageFile implements Parcelable {
    private StorageDirectory directory;
    private File file;
    private Uri uri;

    public StoredImageFile(StorageDirectory d, File f, Uri uri){
        this.directory = d;
        this.file = f;
        this.uri = uri;
    }

    public StorageDirectory getDirectory() {
        return directory;
    }

    public Uri getUri(){return uri;}

    public File getFile() {
        return file;
    }

    public Uri getUri(Context context) {
        return uri;
    }

    @Override
    public String toString() {
        return "ImageFileInfo{" +
                "directory=" + directory +
                ", file=" + file +
                ", uri=" + uri +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(directory, flags);
        out.writeString(file.getAbsolutePath());
        out.writeParcelable(uri, flags);
    }
    private StoredImageFile(Parcel in) {
        directory = in.readParcelable(StorageDirectory.class.getClassLoader());
        file = new File(in.readString());
        uri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Parcelable.Creator<StoredImageFile> CREATOR
              = new Parcelable.Creator<StoredImageFile>() {
        public StoredImageFile createFromParcel (Parcel in) {
            return new StoredImageFile(in);
        }

        public StoredImageFile[] newArray ( int size) {
            return new StoredImageFile[size];
        }
    };
}
