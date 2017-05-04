package com.lularoe.erinfetz.imagecapture;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.google.common.collect.*;

import java.util.*;

public class Products {

    private final Resources resources;

    public Products(Resources res) {
        resources=res;
    }

    public List<String> styles(){
        return this.arrayResource(R.array.product_styles);
    }

    @NonNull
    private List<String> arrayResource(int id){
        return Arrays.asList(resources.getStringArray(id));
    }
    @NonNull
    private String stringResource(int id){
        return resources.getString(id);
    }

    public boolean hasSizes(String productStyle){
        if(arrayResource(R.array.no_size).contains(productStyle)){
            return false;
        }

        return true;
    }

    public List<String> sizes(String productStyle){
        if(arrayResource(R.array.no_size).contains(productStyle)){
            return Lists.newArrayList(stringResource(R.string.size_NA));
        }

        if(stringResource(R.string.product_Bianka).equals(productStyle)){
            return arrayResource(R.array.bianka_sizes);
        }

        if(arrayResource(R.array.kids_styles).contains(productStyle)){
            return filter(arrayResource(R.array.default_kid_sizes), productStyle, R.array.no_14_sizes, R.string.size_14);
        }


        List<String> sizes = arrayResource(R.array.default_adult_sizes);
        sizes = filter(sizes, productStyle, R.array.no_xxs, R.string.size_XXS);
        sizes = filter(sizes, productStyle, R.array.no_xs, R.string.size_XS);
        sizes = filter(sizes, productStyle, R.array.no_s, R.string.size_S);
        sizes = filter(sizes, productStyle, R.array.no_m, R.string.size_M);
        sizes = filter(sizes, productStyle, R.array.no_xl, R.string.size_XL);
        sizes = filter(sizes, productStyle, R.array.no_2xl, R.string.size_2XL);
        sizes = filter(sizes, productStyle, R.array.no_3xl, R.string.size_3XL);

        return sizes;
    }

    public String shortName(String productStyle){
        if(!productStyle.contains(" ")){
            return productStyle;
        }

        if(productStyle.contains("Mommy &amp; Me - ") || productStyle.contains("Mommy & Me - ")){
            String s = productStyle.replace("Mommy &amp; Me - ","MNM_").replace("Mommy & Me - ","MNM_");
            return s.replace(" ", "_").replace("/","");
        }

        if(productStyle.contains("Leggings - ")){
            return productStyle.replace("Leggings - ", "Leggings_")
                    .replace("&amp;", "_")
                    .replace("&", "_")
                    .replace(" ", "_")
                    .replace("/","");
        }

        return productStyle
                .replace(" - ", "_")
                .replace("-", "_")
                .replace(" ", "_")
                .replace("/","");
    }

    private List<String> filter(List<String> sizes, String productStyle, int listId, int itemId){
        if(arrayResource(listId).contains(productStyle)){
            sizes.remove(stringResource(itemId));
        }
        return sizes;
    }
}
