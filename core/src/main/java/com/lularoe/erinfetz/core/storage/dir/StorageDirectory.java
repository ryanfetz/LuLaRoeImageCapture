package com.lularoe.erinfetz.core.storage.dir;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.lularoe.erinfetz.core.storage.StorageUriProvider;

import java.io.File;

public class StorageDirectory implements Parcelable {
    private final File directory;
    private final Uri uri;

    public StorageDirectory(@NonNull File directory, @NonNull Uri uri){
        this.directory = directory;
        this.uri = uri;
    }

    StorageDirectory(Parcel in) {
        directory = new File(in.readString());
        uri = in.readParcelable(Uri.class.getClassLoader());
    }

    public final File getDirectory(){
        return directory;
    }

    public final Uri getUri(){
        return uri;
    }

    public final String getAbsolutePath(){
        return getDirectory().getAbsolutePath();
    }

    public final boolean exists(){
        return directory.exists();
    }

    public final boolean mkdirs(){
        return directory.mkdirs();
    }

    @Override
    public String toString(){
        return MoreObjects.toStringHelper(this)
                .add("directory", getAbsolutePath())
                .add("uri", uri)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StorageDirectory that = (StorageDirectory) o;

        return Objects.equal(directory, that.directory) &&
                Objects.equal(uri, that.directory);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClass().getSimpleName(), directory, uri);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(directory.getAbsolutePath());
        out.writeParcelable(uri, flags);
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
