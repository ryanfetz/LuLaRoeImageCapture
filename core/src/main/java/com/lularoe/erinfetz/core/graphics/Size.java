package com.lularoe.erinfetz.core.graphics;

import android.annotation.TargetApi;
import android.hardware.Camera;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Size(android.util.Size size) {
        this.mWidth = size.getWidth();
        this.mHeight = size.getHeight();
    }

    @SuppressWarnings("deprecation")
    public Size(Camera.Size size) {
        this.mWidth = size.width;
        this.mHeight = size.height;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static List<Size> fromList2(List<android.util.Size> sizes) {
        if (sizes == null) return null;
        List<Size> result = new ArrayList<>(sizes.size());

        for (android.util.Size size : sizes) {
            result.add(new Size(size));
        }

        return result;
    }

    @SuppressWarnings("deprecation")
    public static List<Size> fromList(List<Camera.Size> sizes) {
        if (sizes == null) return null;
        List<Size> result = new ArrayList<>(sizes.size());

        for (Camera.Size size : sizes) {
            result.add(new Size(size));
        }

        return result;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Size[] fromArray2(android.util.Size[] sizes) {
        if (sizes == null) return null;
        Size[] result = new Size[sizes.length];

        for (int i = 0; i < sizes.length; ++i) {
            result[i] = new Size(sizes[i]);
        }

        return result;
    }

    @SuppressWarnings("deprecation")
    public static Size[] fromArray(Camera.Size[] sizes) {
        if (sizes == null) return null;
        Size[] result = new Size[sizes.length];

        for (int i = 0; i < sizes.length; ++i) {
            result[i] = new Size(sizes[i]);
        }

        return result;
    }

    public static Comparator<Size> byArea(){
        return new Comparator<Size> (){
            @Override
            public int compare(Size lhs, Size rhs) {
                // We cast here to ensure the multiplications won't overflow
                return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                        (long) rhs.getWidth() * rhs.getHeight());
            }
        };
    }

    public static Comparator<Size> bySize(){
        return new Comparator<Size>() {
            @Override
            public int compare(Size lhs, Size rhs) {
                if (lhs.getHeight() * lhs.getWidth() > rhs.getHeight() * rhs.getWidth())
                    return -1;
                return 1;

            }
        };
    }
}
