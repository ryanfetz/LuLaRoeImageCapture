package com.lularoe.erinfetz.imagecapture.products;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.common.base.Objects;

public class ProductName implements Parcelable{
    private final String name;
    private final String shortName;

    ProductName(@NonNull String name, @NonNull String shortName){
        this.name = name;
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductName that = (ProductName) o;

        return Objects.equal(name, that.name) &&
                Objects.equal(shortName, that.shortName);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName(), getShortName());
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(shortName);
    }

    private ProductName(Parcel in) {
        name= in.readString();
        shortName=in.readString();
    }

    public static final Parcelable.Creator<ProductName> CREATOR
            = new Parcelable.Creator<ProductName>() {
        public ProductName createFromParcel (Parcel in) {
            return new ProductName(in);
        }

        public ProductName[] newArray ( int size) {
            return new ProductName[size];
        }
    };
}
