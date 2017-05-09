package com.lularoe.erinfetz.core.storage.files;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.common.base.MoreObjects;
import com.lularoe.erinfetz.core.storage.dir.StorageDirectory;

import java.io.File;

public class StoredFile implements Parcelable {
    private final StorageDirectory directory;
    private final File file;
    private final Uri uri;

    public StoredFile(@NonNull StorageDirectory d, @NonNull File f, @NonNull Uri uri){
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

    public final String getAbsolutePath(){
        return getFile().getAbsolutePath();
    }

    public final boolean exists(){
        return file.exists();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("file", getAbsolutePath())
                .add("directory", getDirectory())
                .add("uri", getUri())
                .toString();
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

    private StoredFile(Parcel in) {
        directory = in.readParcelable(StorageDirectory.class.getClassLoader());
        file = new File(in.readString());
        uri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Parcelable.Creator<StoredFile> CREATOR
            = new Parcelable.Creator<StoredFile>() {
        public StoredFile createFromParcel (Parcel in) {
            return new StoredFile(in);
        }

        public StoredFile[] newArray ( int size) {
            return new StoredFile[size];
        }
    };
}
