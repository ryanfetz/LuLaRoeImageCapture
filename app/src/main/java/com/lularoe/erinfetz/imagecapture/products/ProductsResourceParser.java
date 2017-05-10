package com.lularoe.erinfetz.imagecapture.products;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.google.common.collect.Maps;
import com.lularoe.erinfetz.core.res.ResourceUtils;

import org.xmlpull.v1.XmlPullParser;

import java.util.Map;

public class ProductsResourceParser {
    private static final String TAG = ResourceUtils.class.getSimpleName();

    public static Map<String, ProductInfo> getProducts(Resources res, int resId) {
        Map<String,ProductInfo> map = null;
        XmlResourceParser parser =null;
        ProductInfo.Builder builder = null;
        try{
            parser = res.getXml(resId);
            int eventType = parser.getEventType();
            boolean insideSize=false;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d(TAG,"Start document");
                } else if (eventType == XmlPullParser.START_TAG) {

                    if (parser.getName().equals("products")) {
                        map = Maps.newHashMap();
                    } else if (parser.getName().equals("product")) {
                        builder = ProductInfo.builder();
                    } else if (parser.getName().equals("name")) {
                        builder.name( parser.getAttributeValue(null, "value"));
                        builder.shortName( parser.getAttributeValue(null, "short"));

                    } else if (parser.getName().equals("price")) {
                        builder.price(parser.getAttributeIntValue(null, "value", 0));
                    }else if (parser.getName().equals("sizes")) {

                    }else if (parser.getName().equals("size")) {
                        insideSize=true;
                    }

                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("product")) {
                        map.put(builder.name(), builder.build());
                        builder=null;
                    }else if (parser.getName().equals("size")) {
                        insideSize=false;
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    if(null != builder && insideSize){
                        builder.size(parser.getText().trim());
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(),e);
            return null;
        }finally{
            if(parser!=null){
                parser.close();
            }
        }
        return map;
    }
    /*
<products>
    <product>
        <name value="Adeline"/>
        <price value="30"/>
        <sizes>
            <size>2</size>
            <size>4</size>
            <size>6</size>
            <size>8</size>
            <size>10</size>
            <size>12</size>
        </sizes>
    </product>
    * */
}
