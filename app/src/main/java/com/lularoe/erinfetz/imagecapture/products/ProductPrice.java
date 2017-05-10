package com.lularoe.erinfetz.imagecapture.products;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.Objects;

public class ProductPrice implements Parcelable{
    private int price;

    ProductPrice(int price){
        this.price = price;
    }

    public int get() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductPrice that = (ProductPrice) o;

        return Objects.equal(price, that.price);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(price);
    }

    @Override
    public String toString() {
        return "$"+price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(price);
    }

    private ProductPrice(Parcel in) {
        price= in.readInt();
    }

    public static final Parcelable.Creator<ProductPrice> CREATOR
            = new Parcelable.Creator<ProductPrice>() {
        public ProductPrice createFromParcel (Parcel in) {
            return new ProductPrice(in);
        }

        public ProductPrice[] newArray ( int size) {
            return new ProductPrice[size];
        }
    };
}
