package com.lularoe.erinfetz.imagecapture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.Map;

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
            return getImage(R.drawable.adeline_MAP1);
        }else if(productStyle.equals("Amelia")){
            return getImage(R.drawable.Amelia_MAP1);
        }else if(productStyle.equals("Ana")){
            return getImage(R.drawable.Ana_MAP1);
        }else if(productStyle.equals("Azure")){
            return getImage(R.drawable.Azure_MAP1);
        }else if(productStyle.equals("Bianka")){
            return getImage(R.drawable.Bianka_MAP1);
        }else if(productStyle.equals("Carly")){
            return getImage(R.drawable.Carly_MAP1);
        }else if(productStyle.equals("Cassie")){
            return getImage(R.drawable.Cassie_MAP1);
        }else if(productStyle.equals("Classic T")){
            return getImage(R.drawable.classict_MAP1);
        }else if(productStyle.equals("Gracie")){
            return getImage(R.drawable.gracie_MAP1);
        }else if(productStyle.equals("Irma")){
            return getImage(R.drawable.Irma_MAP1);
        }else if(productStyle.equals("Jade")){
            return getImage(R.drawable.Jade_MAP1);
        }else if(productStyle.equals("Jill")){
            return getImage(R.drawable.JILL_MAP1);
        }else if(productStyle.equals("Jordan")){
            return getImage(R.drawable.Jordan_MAP1);
        }else if(productStyle.equals("Joy")){
            return getImage(R.drawable.JOY_MAP1);
        }else if(productStyle.equals("Julia")){
            return getImage(R.drawable.Julia_MAP1);
        }else if(productStyle.equals("Kids Azure")){
            return getImage(R.drawable.kidsazure_MAP);
        }else if(productStyle.equals("Leggings - Tween")){
            return getImage(R.drawable.TWEEN_MAP);
        }else if(productStyle.equals("Leggings - One Size")){
            return getImage(R.drawable.onesize_MAP1);
        }else if(productStyle.equals("Leggings - Tall &amp; Curvy")||productStyle.equals("Leggings - Tall & Curvy")){
            return getImage(R.drawable.tallcurvy_MAP1);
        }else if(productStyle.equals("Leggings - Kids S/M")){
            return getImage(R.drawable.smleggingd_MAP);
        }else if(productStyle.equals("Leggings - Kids L/XL")){
            return getImage(R.drawable.lxl_leggingd_MAP);
        }else if(productStyle.equals("Lindsay")){
            return getImage(R.drawable.lindsay_MAP1);
        }else if(productStyle.equals("Lola")){
            return getImage(R.drawable.lola_MAP);
        }else if(productStyle.equals("Lucy")){
            return getImage(R.drawable.Lucy_MAP);
        }else if(productStyle.equals("Madison")){
            return getImage(R.drawable.Madison_MAP);
        }else if(productStyle.equals("Mae")){
            return getImage(R.drawable.mae_MAP);
        }else if(productStyle.equals("Mark")){
            return getImage(R.drawable.Mark_MAP);
        }else if(productStyle.equals("Maxi")){
            return getImage(R.drawable.Maxi_MAP);
        }else if(productStyle.equals("Mimi")){
            return getImage(R.drawable.MiMi_MAP);
        }else if(productStyle.equals("Monroe")){
            return getImage(R.drawable.monroe_MAP1);
        }else if(productStyle.equals("Nicole")){
            return getImage(R.drawable.Nicole_MAP1);
        }else if(productStyle.equals("Patrick")){
            return getImage(R.drawable.patrick_MAP);
        }else if(productStyle.equals("Perfect T")){
            return getImage(R.drawable.perfectt_MAP1);
        }else if(productStyle.equals("Randy")){
            return getImage(R.drawable.Randy_MAP1);
        }else if(productStyle.equals("Sarah")){
            return getImage(R.drawable.sarah_MAP1);
        }else if(productStyle.equals("Scarlett")){
            return getImage(R.drawable.mnm_Scarlet_MAP);
        }else if(productStyle.equals("Sloan")){
            return getImage(R.drawable.sloan_MAP);
        }else if(productStyle.equals("Mommy &amp; Me - Gracie")||productStyle.equals("Mommy & Me - Gracie")){
            return getImage(R.drawable.mnm_gracie_MAP1);
        }else if(productStyle.equals("Mommy &amp; Me - Irma")||productStyle.equals("Mommy & Me - Irma")){
            return getImage(R.drawable.mnm_Irma_MAP1);
        }else if(productStyle.equals("Mommy &amp; Me - Adeline")||productStyle.equals("Mommy & Me - Adeline")){
            return getImage(R.drawable.mnm_adeline_MAP1);
        }else if(productStyle.equals("Mommy &amp; Me - Nicole")||productStyle.equals("Mommy & Me - Nicole")){
            return getImage(R.drawable.mnm_Nicole_MAP1);
        }else if(productStyle.equals("Mommy &amp; Me - Scarlett")||productStyle.equals("Mommy & Me - Scarlett")){
            return getImage(R.drawable.mnm_Scarlet_MAP);
        }else if(productStyle.equals("Mommy &amp; Me - Carly")||productStyle.equals("Mommy & Me - Carly")){
            return getImage(R.drawable.mnm_Carly_MAP1);
        }else if(productStyle.equals("Mommy &amp; Me - Tween")||productStyle.equals("Mommy & Me - Tween")){
            return getImage(R.drawable.mnm_TWEEN_MAP);
        }else if(productStyle.equals("Mommy &amp; Me - OS")||productStyle.equals("Mommy & Me - OS")){
            return getImage(R.drawable.mnm_onesize_MAP1);
        }else if(productStyle.equals("Mommy &amp; Me - TC")||productStyle.equals("Mommy & Me - TC")){
            return getImage(R.drawable.mnm_tallcurvy_MAP1);
        }else if(productStyle.equals("Mommy &amp; Me - Kids S/M")||productStyle.equals("Mommy & Me - Kids S/M")){
            return getImage(R.drawable.mnm_smleggingd_MAP);
        }else if(productStyle.equals("Mommy &amp; Me - Kids L/XL")||productStyle.equals("Mommy & Me - Kids L/XL")){
            return getImage(R.drawable.mnm_lxl_leggingd_MAP);
        }else{
            return null;
        }
    }
}