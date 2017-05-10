package com.lularoe.erinfetz.imagecapture.products;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.common.base.Objects;

public class ProductSize implements Parcelable{
    private final String size;

    ProductSize(@NonNull String size){
        this.size = size;
    }

    public String get() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductSize that = (ProductSize) o;

        return Objects.equal(size, that.size);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(size);
    }

    @Override
    public String toString() {
        return get();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(size);
    }

    private ProductSize(Parcel in) {
        size= in.readString();
    }

    public static final Parcelable.Creator<ProductSize> CREATOR
            = new Parcelable.Creator<ProductSize>() {
        public ProductSize createFromParcel (Parcel in) {
            return new ProductSize(in);
        }

        public ProductSize[] newArray ( int size) {
            return new ProductSize[size];
        }
    };
}
