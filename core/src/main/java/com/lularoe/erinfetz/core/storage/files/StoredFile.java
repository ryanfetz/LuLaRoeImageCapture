package com.lularoe.erinfetz.core.storage.files;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.lularoe.erinfetz.core.ContentValuesBuilder;
import com.lularoe.erinfetz.core.storage.dir.StorageDirectory;

import java.io.File;
import java.util.Date;

public class StoredFile implements Parcelable {
    private final StorageDirectory directory;
    private final File file;
    private final MediaType mediaType;
    private final Uri uri;
    private final ContentValues metadata;

    public StoredFile(@NonNull StorageDirectory d, @NonNull File f, @NonNull MediaType mediaType, @NonNull Uri uri, @NonNull ContentValues metadata){
        this.directory = d;
        this.mediaType = mediaType;
        this.file = f;
        this.uri = uri;
        this.metadata = metadata;
    }
    public StoredFile(@NonNull StorageDirectory d, @NonNull File f, @NonNull MediaType mediaType, @NonNull Uri uri, @NonNull ContentValuesBuilder metadata){

        this(d, f, mediaType, uri, metadata.filePath(f.getAbsolutePath()).mimeType(mediaType).build());
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public StorageDirectory getDirectory() {
        return directory;
    }

    public Uri getUri(){return uri;}

    public File getFile() {
        return file;
    }

    public ContentValues getMetadata(){return metadata;}

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
                .add("mediaType", getMediaType())
                .add("directory", getDirectory())
                .add("uri", getUri())
                .add("metadata", getMetadata())
                .toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoredFile that = (StoredFile) o;

        return Objects.equal(directory, that.directory) &&
                Objects.equal(uri, that.directory) &&
                Objects.equal(file, that.file) &&
                Objects.equal(mediaType, that.mediaType)&&
                Objects.equal(metadata, that.metadata);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClass().getSimpleName(), directory, uri, file, mediaType, metadata);
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
        out.writeString(mediaType.toString());
        out.writeParcelable(metadata,flags);
    }

    private StoredFile(Parcel in) {
        directory = in.readParcelable(StorageDirectory.class.getClassLoader());
        file = new File(in.readString());
        uri = in.readParcelable(Uri.class.getClassLoader());
        mediaType = MediaType.parse(in.readString());
        metadata = in.readParcelable(ContentValues.class.getClassLoader());
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
