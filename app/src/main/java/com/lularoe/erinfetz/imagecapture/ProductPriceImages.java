package com.lularoe.erinfetz.imagecapture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ProductPriceImages{
    private static final String TAG = ProductPriceImages.class.getSimpleName();
    private static ProductPriceImages INSTANCE;
    private final Context context;

    private ProductPriceImages(Context context) {
        this.context = context;
    }

    public static ProductPriceImages getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ProductPriceImages(context);
        }
        return INSTANCE;
    }

    private Bitmap getImage(int id){
        return BitmapFactory.decodeResource(context.getResources(),id);
    }
    public Bitmap get(String productStyle){

        if(productStyle.equals("Adeline")){
            return getImage(R.drawable.adeline_map1);
        }else if(productStyle.equals("Amelia")){
            return getImage(R.drawable.amelia_map1);
        }else if(productStyle.equals("Ana")){
            return getImage(R.drawable.ana_map1);
        }else if(productStyle.equals("Azure")){
            return getImage(R.drawable.azure_map1);
        }else if(productStyle.equals("Bianka")){
            return getImage(R.drawable.bianka_map1);
        }else if(productStyle.equals("Carly")){
            return getImage(R.drawable.carly_map1);
        }else if(productStyle.equals("Cassie")){
            return getImage(R.drawable.cassie_map1);
        }else if(productStyle.equals("Classic Tee")){
            return getImage(R.drawable.classict_map1);
        }else if(productStyle.equals("Gracie")){
            return getImage(R.drawable.gracie_map1);
        }else if(productStyle.equals("Irma")){
            return getImage(R.drawable.irma_map1);
        }else if(productStyle.equals("Jade")){
            return getImage(R.drawable.jade_map1);
        }else if(productStyle.equals("Jill")){
            return getImage(R.drawable.jill_map1);
        }else if(productStyle.equals("Jordan")){
            return getImage(R.drawable.jordan_map1);
        }else if(productStyle.equals("Joy")){
            return getImage(R.drawable.joy_map1);
        }else if(productStyle.equals("Julia")){
            return getImage(R.drawable.julia_map1);
        }else if(productStyle.equals("Kids Azure")){
            return getImage(R.drawable.kidsazure_map);
        }else if(productStyle.equals("Leggings - Tween")){
            return getImage(R.drawable.tween_map);
        }else if(productStyle.equals("Leggings - One Size")){
            return getImage(R.drawable.onesize_map1);
        }else if(productStyle.equals("Leggings - Tall &amp; Curvy")||productStyle.equals("Leggings - Tall & Curvy")){
            return getImage(R.drawable.tallcurvy_map1);
        }else if(productStyle.equals("Leggings - Kids S/M")){
            return getImage(R.drawable.smleggingd_map);
        }else if(productStyle.equals("Leggings - Kids L/XL")){
            return getImage(R.drawable.lxl_leggingd_map);
        }else if(productStyle.equals("Lindsay")){
            return getImage(R.drawable.lindsay_map1);
        }else if(productStyle.equals("Lola")){
            return getImage(R.drawable.lola_map);
        }else if(productStyle.equals("Lucy")){
            return getImage(R.drawable.lucy_map);
        }else if(productStyle.equals("Madison")){
            return getImage(R.drawable.madison_map);
        }else if(productStyle.equals("Mae")){
            return getImage(R.drawable.mae_map);
        }else if(productStyle.equals("Mark")){
            return getImage(R.drawable.mark_map);
        }else if(productStyle.equals("Maxi")){
            return getImage(R.drawable.maxi_map);
        }else if(productStyle.equals("Mimi")){
            return getImage(R.drawable.mimi_map);
        }else if(productStyle.equals("Monroe")){
            return getImage(R.drawable.monroe_map1);
        }else if(productStyle.equals("Nicole")){
            return getImage(R.drawable.nicole_map1);
        }else if(productStyle.equals("Patrick")){
            return getImage(R.drawable.patrick_map);
        }else if(productStyle.equals("Perfect Tee")){
            return getImage(R.drawable.perfectt_map1);
        }else if(productStyle.equals("Randy")){
            return getImage(R.drawable.randy_map1);
        }else if(productStyle.equals("Sarah")){
            return getImage(R.drawable.sarah_map1);
        }else if(productStyle.equals("Scarlett")){
            return getImage(R.drawable.mnm_scarlet_map);
        }else if(productStyle.equals("Sloan")){
            return getImage(R.drawable.sloan_map);
        }else if(productStyle.equals("Mommy &amp; Me - Gracie")||productStyle.equals("Mommy & Me - Gracie")){
            return getImage(R.drawable.mnm_gracie_map1);
        }else if(productStyle.equals("Mommy &amp; Me - Irma")||productStyle.equals("Mommy & Me - Irma")){
            return getImage(R.drawable.mnm_irma_map1);
        }else if(productStyle.equals("Mommy &amp; Me - Adeline")||productStyle.equals("Mommy & Me - Adeline")){
            return getImage(R.drawable.mnm_adeline_map1);
        }else if(productStyle.equals("Mommy &amp; Me - Nicole")||productStyle.equals("Mommy & Me - Nicole")){
            return getImage(R.drawable.mnm_nicole_map1);
        }else if(productStyle.equals("Mommy &amp; Me - Scarlett")||productStyle.equals("Mommy & Me - Scarlett")){
            return getImage(R.drawable.mnm_scarlet_map);
        }else if(productStyle.equals("Mommy &amp; Me - Carly")||productStyle.equals("Mommy & Me - Carly")){
            return getImage(R.drawable.mnm_carly_map1);
        }else if(productStyle.equals("Mommy &amp; Me - Tween")||productStyle.equals("Mommy & Me - Tween")){
            return getImage(R.drawable.mnm_tween_map);
        }else if(productStyle.equals("Mommy &amp; Me - OS")||productStyle.equals("Mommy & Me - OS")){
            return getImage(R.drawable.mnm_onesize_map1);
        }else if(productStyle.equals("Mommy &amp; Me - TC")||productStyle.equals("Mommy & Me - TC")){
            return getImage(R.drawable.mnm_tallcurvy_map1);
        }else if(productStyle.equals("Mommy &amp; Me - Kids S/M")||productStyle.equals("Mommy & Me - Kids S/M")){
            return getImage(R.drawable.mnm_smleggingd_map);
        }else if(productStyle.equals("Mommy &amp; Me - Kids L/XL")||productStyle.equals("Mommy & Me - Kids L/XL")){
            return getImage(R.drawable.mnm_lxl_leggingd_map);
        }else{
            return null;
        }
    }
}