package com.lularoe.erinfetz.imagecapture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ProductDetailImages{
    private static final String TAG = ProductDetailImages.class.getSimpleName();
    private static ProductDetailImages INSTANCE;
    private final Context context;

    private ProductDetailImages(Context context) {
        this.context = context;
    }

    public static ProductDetailImages getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ProductDetailImages(context);
        }
        return INSTANCE;
    }

    private Bitmap getImage(int id){
        return BitmapFactory.decodeResource(context.getResources(),id);
    }
    public Bitmap get(String productStyle){

        if(productStyle.equals("Adeline")){
            return getImage(R.drawable.sizingchart_adeline);
        }else if(productStyle.equals("Amelia")){
            return getImage(R.drawable.sizingchart_amelia17);
        }else if(productStyle.equals("Ana")){
            return getImage(R.drawable.sizingchart_ana17);
        }else if(productStyle.equals("Azure")){
            return getImage(R.drawable.sizingchart_azure17);
        }else if(productStyle.equals("Bianka")){
            return getImage(R.drawable.sizingchart_bianka_map);
        }else if(productStyle.equals("Carly")){
            return getImage(R.drawable.sizingchart_carly_map);
        }else if(productStyle.equals("Cassie")){
            return getImage(R.drawable.sizingchart_cassie17);
        }else if(productStyle.equals("Classic Tee")){
            return getImage(R.drawable.sizingchart_classic);
        }else if(productStyle.equals("Gracie")){
            return getImage(R.drawable.sizingchart_gracie_map);
        }else if(productStyle.equals("Irma")){
            return getImage(R.drawable.sizingchart_irma17);
        }else if(productStyle.equals("Jade")){
            return getImage(R.drawable.sizingchart_jade17);
        }else if(productStyle.equals("Jill")){
            return getImage(R.drawable.sizingchart_jill17);
        }else if(productStyle.equals("Jordan")){
            return getImage(R.drawable.sizingchart_jordan_map);
        }else if(productStyle.equals("Joy")){
            return getImage(R.drawable.sizingchart_joy17);
        }else if(productStyle.equals("Julia")){
            return getImage(R.drawable.sizingchart_julia17);
        }else if(productStyle.equals("Kids Azure")){
            return getImage(R.drawable.sizingchart_kidsazure_map);
        }else if(productStyle.equals("Leggings - Tween")){
            return getImage(R.drawable.sizingchart_kidsleggings_map);
        }else if(productStyle.equals("Leggings - One Size")){
            return getImage(R.drawable.sizingchart_leggings17);
        }else if(productStyle.equals("Leggings - Tall &amp; Curvy")||productStyle.equals("Leggings - Tall & Curvy")){
            return getImage(R.drawable.sizingchart_leggings17);
        }else if(productStyle.equals("Leggings - Kids S/M")){
            return getImage(R.drawable.sizingchart_kidsleggings_map);
        }else if(productStyle.equals("Leggings - Kids L/XL")){
            return getImage(R.drawable.sizingchart_kidsleggings_map);
        }else if(productStyle.equals("Lindsay")){
            return getImage(R.drawable.sizingchart_lindsay17);
        }else if(productStyle.equals("Lola")){
            return getImage(R.drawable.sizingchart_lola17);
        }else if(productStyle.equals("Lucy")){
            return getImage(R.drawable.sizingchart_lucy17);
        }else if(productStyle.equals("Madison")){
            return getImage(R.drawable.sizingchart_madison);
        }else if(productStyle.equals("Mae")){
            return getImage(R.drawable.sizingchart_mae_map);
        }else if(productStyle.equals("Mark")){
            return getImage(R.drawable.sizingchart_mark_map);
        }else if(productStyle.equals("Maxi")){
            return getImage(R.drawable.sizingchart_maxi17);
        }else if(productStyle.equals("Mimi")){
            return getImage(R.drawable.sizingchart_mimi);
        }else if(productStyle.equals("Monroe")){
            return getImage(R.drawable.sizingchart_monroemap);
        }else if(productStyle.equals("Nicole")){
            return getImage(R.drawable.sizingchart_nicole17);
        }else if(productStyle.equals("Patrick")){
            return getImage(R.drawable.sizingchart_patrick_map);
        }else if(productStyle.equals("Perfect Tee")){
            return getImage(R.drawable.sizingchart_perfectmap);
        }else if(productStyle.equals("Randy")){
            return getImage(R.drawable.sizingchart_randy17);
        }else if(productStyle.equals("Sarah")){
            return getImage(R.drawable.sizingchart_sarah_map);
        }else if(productStyle.equals("Scarlett")){
            return getImage(R.drawable.sizingchart_scarlett);
        }else if(productStyle.equals("Sloan")){
            return getImage(R.drawable.sizingchart_sloan_copy);
        }else if(productStyle.equals("Mommy &amp; Me - Gracie")||productStyle.equals("Mommy & Me - Gracie")){
            return getImage(R.drawable.mnm_sizingchart_gracie_map);
        }else if(productStyle.equals("Mommy &amp; Me - Irma")||productStyle.equals("Mommy & Me - Irma")){
            return getImage(R.drawable.sizingchart_irma17);
        }else if(productStyle.equals("Mommy &amp; Me - Adeline")||productStyle.equals("Mommy & Me - Adeline")){
            return getImage(R.drawable.mnm_sizingchart_adeline);
        }else if(productStyle.equals("Mommy &amp; Me - Nicole")||productStyle.equals("Mommy & Me - Nicole")){
            return getImage(R.drawable.sizingchart_nicole17);
        }else if(productStyle.equals("Mommy &amp; Me - Scarlett")||productStyle.equals("Mommy & Me - Scarlett")){
            return getImage(R.drawable.sizingchart_scarlett);
        }else if(productStyle.equals("Mommy &amp; Me - Carly")||productStyle.equals("Mommy & Me - Carly")){
            return getImage(R.drawable.mnm_sizingchart_carly_map);
        }else if(productStyle.equals("Mommy &amp; Me - Tween")||productStyle.equals("Mommy & Me - Tween")){
            return getImage(R.drawable.mnm_sizingchart_kidsleggings_map);
        }else if(productStyle.equals("Mommy &amp; Me - OS")||productStyle.equals("Mommy & Me - OS")){
            return getImage(R.drawable.sizingchart_leggings17);
        }else if(productStyle.equals("Mommy &amp; Me - TC")||productStyle.equals("Mommy & Me - TC")){
            return getImage(R.drawable.sizingchart_leggings17);
        }else if(productStyle.equals("Mommy &amp; Me - Kids S/M")||productStyle.equals("Mommy & Me - Kids S/M")){
            return getImage(R.drawable.mnm_sizingchart_kidsleggings_map);
        }else if(productStyle.equals("Mommy &amp; Me - Kids L/XL")||productStyle.equals("Mommy & Me - Kids L/XL")){
            return getImage(R.drawable.mnm_sizingchart_kidsleggings_map);
        }else{
            return null;
        }
    }
}
