package com.lularoe.erinfetz.imagecapture;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.lularoe.erinfetz.imagecapture.products.ProductInfo;
import com.lularoe.erinfetz.imagecapture.products.ProductsResourceParser;

import java.util.Collections;
import java.util.List;
import java.util.Map;


public class Products {

    private final Map<String, ProductInfo> products;

    public Products(Resources res) {
        products = ProductsResourceParser.getProducts(res, R.xml.products);
    }

    public List<String> styles(){

        List<String> s = Lists.newArrayList(products.keySet());
        Collections.sort(s);
        return s;
    }

    public ProductInfo get(@NonNull String name){
        return products.get(name);
    }

    public boolean hasSizes(@NonNull String productStyle){
        ProductInfo p = get(productStyle);
        if(p==null)return false;
        return p.hasSizes();
    }

    public List<String> sizes(@NonNull String productStyle){
        ProductInfo p = get(productStyle);
        if(p==null){
            return null;
        }
        return p.sizes();
    }

    public String shortName(@NonNull String productStyle){
        ProductInfo p = get(productStyle);
        if(p==null)return null;
        return p.getName().getShortName();
    }

    public int price(@NonNull String productStyle){
        ProductInfo p = get(productStyle);
        if(p==null)return 0;
        return p.getPrice().get();
    }
}
