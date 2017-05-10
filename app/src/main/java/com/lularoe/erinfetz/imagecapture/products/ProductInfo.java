package com.lularoe.erinfetz.imagecapture.products;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Button;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ProductInfo implements Parcelable {
    private final ProductName name;
    private final ProductPrice price;
    private final List<ProductSize> sizes;

    private static final String DATE_FORMAT = "yyyyMMdd HHmmss";

    ProductInfo(ProductName name, ProductPrice price, List<ProductSize> sizes){
        this.name = name;
        this.price = price;
        this.sizes = sizes;
    }

    public String title(String size, Date date){
        if(Strings.isNullOrEmpty(size)){
            return String.format("%s (%s) %s", name.getShortName(), price, new SimpleDateFormat(DATE_FORMAT).format(date));
        }
        return String.format("%s %s (%s) %s", name.getShortName(), size, price, new SimpleDateFormat(DATE_FORMAT).format(date));
    }

    public String displayName(String size, Date date){
        if(Strings.isNullOrEmpty(size)){
            return String.format("%s %s", name.getShortName(), new SimpleDateFormat(DATE_FORMAT).format(date));
        }
        return String.format("%s %s %s", name.getShortName(), size, new SimpleDateFormat(DATE_FORMAT).format(date));
    }

    public String description(String size, Date date){
        if(Strings.isNullOrEmpty(size)){
            return String.format("%s (%s) %s", name.getName(), price, new SimpleDateFormat(DATE_FORMAT).format(date));
        }
        return String.format("%s %s (%s) %s", name.getName(), size, price, new SimpleDateFormat(DATE_FORMAT).format(date));
    }

    public List<String> sizes(){
        if(this.sizes.isEmpty()){
            return Lists.newArrayList();
        }
        List<String> s = Lists.newArrayList();
        for(ProductSize ps : sizes){
            s.add(ps.get());
        }
        return s;
    }
    public boolean hasSizes(){
        if(this.sizes.isEmpty()){
            return false;
        }
        return true;
    }

    public ProductName getName() {
        return name;
    }

    public ProductPrice getPrice() {
        return price;
    }

    public List<ProductSize> getSizes() {
        return Collections.unmodifiableList(sizes);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("price", price)
                .add("sizes", sizes)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductInfo that = (ProductInfo) o;

        return Objects.equal(name, that.name) &&
                Objects.equal(price, that.price) &&
                Objects.equal(sizes, that.sizes);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName(),getPrice(), getSizes());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(name, flags);
        out.writeParcelable(price, flags);
        out.writeList(sizes);
    }

    private ProductInfo(Parcel in) {
        name = in.readParcelable(ProductName.class.getClassLoader());
        price = in.readParcelable(ProductPrice.class.getClassLoader());
        sizes = new ArrayList<>();
        in.readList(sizes,ProductSize.class.getClassLoader());
    }

    public static final Parcelable.Creator<ProductInfo> CREATOR
            = new Parcelable.Creator<ProductInfo>() {
        public ProductInfo createFromParcel (Parcel in) {
            return new ProductInfo(in);
        }

        public ProductInfo[] newArray ( int size) {
            return new ProductInfo[size];
        }
    };

    public static Builder builder(){
        return new Builder();
    }
    public static class Builder{
        private String name;
        private String shortName;
        private int price;
        private final List<ProductSize> sizes;

        public Builder(){
            this.sizes = Lists.newArrayList();
        }

        public String name(){
            return name;
        }
        public String shortName(){
            return shortName;
        }
        public int price(){
            return price;
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }
        public Builder shortName(String shortName){
            this.shortName = shortName;
            return this;
        }
        public Builder price(int price){
            this.price = price;
            return this;
        }

        public Builder size(String size){
            this.sizes.add(new ProductSize(size));
            return this;
        }

        public Builder size(ProductSize size){
            this.sizes.add(size);
            return this;
        }

        public ProductInfo build(){
            return new ProductInfo(new ProductName(name, Strings.isNullOrEmpty(shortName)?name:shortName), new ProductPrice(price), sizes);
        }
    }
}