package com.lularoe.erinfetz.core.graphics;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Comparator;

public class Size implements Comparable<Size>, Parcelable {

    private final int mWidth;
    private final int mHeight;

    public Size(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (o instanceof Size) {
            Size size = (Size) o;
            return mWidth == size.mWidth && mHeight == size.mHeight;
        }
        return false;
    }

    @Override
    public String toString() {
        return mWidth + "x" + mHeight;
    }

    @Override
    public int hashCode() {
        return mHeight ^ ((mWidth << (Integer.SIZE / 2)) | (mWidth >>> (Integer.SIZE / 2)));
    }

    @Override
    public int compareTo(@NonNull Size another) {
        return mWidth * mHeight - another.mWidth * another.mHeight;
    }

    public static Size of(int w, int h) {
        return new Size(w,h);
    }

    public static Size parseSize(@NonNull String string)
            throws NumberFormatException {

        int sep_ix = string.indexOf('*');
        if (sep_ix < 0) {
            sep_ix = string.indexOf('x');
        }
        if (sep_ix < 0) {
            throw new NumberFormatException("Invalid Size: \"" + string + "\"");
        }
        try {
            return new Size(Integer.parseInt(string.substring(0, sep_ix)),
                    Integer.parseInt(string.substring(sep_ix + 1)));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid Size: \"" + string + "\"");
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mWidth);
        dest.writeInt(mHeight);
    }

    public static final Parcelable.Creator<Size> CREATOR = new Parcelable.Creator<Size>() {

        @Override
        public Size createFromParcel(Parcel source) {
            int mWidth = source.readInt();
            int mHeight = source.readInt();
            return Size.of(mWidth, mHeight);
        }

        @Override
        public Size[] newArray(int size) {
            return new Size[size];
        }

    };
}
