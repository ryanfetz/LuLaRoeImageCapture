package com.lularoe.erinfetz.core;

import android.content.ContentValues;
import android.provider.MediaStore;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.lularoe.erinfetz.core.storage.files.MediaType;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ContentValuesBuilder {
    private String title;
    private String displayName;
    private String description;
    private MediaType mimeType;
    private Date dateTaken;
    private String filePath;
    private HashMap<String, String> others;

    public static ContentValuesBuilder start(){
        return new ContentValuesBuilder();
    }

    private ContentValuesBuilder(){
        description="";
        others = new HashMap<String, String>();
    }

    public ContentValuesBuilder other(String key, String value){
        others.put(key, value);
        return this;
    }
    public ContentValuesBuilder title(String title){
        this.title = title;
        return this;
    }
    public ContentValuesBuilder displayName(String displayName){
        this.displayName = displayName;
        return this;
    }
    public ContentValuesBuilder description(String description){
        this.description = description;
        return this;
    }
    public ContentValuesBuilder mimeType(MediaType mimeType){
        this.mimeType = mimeType;
        return this;
    }
    public ContentValuesBuilder dateTaken(Date dateTaken){
        this.dateTaken = dateTaken;
        return this;
    }
    public ContentValuesBuilder filePath(String filePath){
        this.filePath = filePath;
        return this;
    }

    public ContentValues build(){
        Preconditions.checkNotNull(mimeType, "No mime type provided");

        if(Strings.isNullOrEmpty(title)) {
            if(Strings.isNullOrEmpty(filePath)) {
                throw new IllegalArgumentException(String.valueOf("No title or filePath provided"));
            }else{
                title = filePath;
            }
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, Strings.isNullOrEmpty(displayName)?title:displayName);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType.withoutParameters());
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());

        if(dateTaken==null){
            dateTaken = new Date();
        }
        values.put(MediaStore.Images.Media.DATE_TAKEN,dateTaken.getTime());

        if(!Strings.isNullOrEmpty(filePath)) {
            values.put(MediaStore.Images.Media.DATA, filePath.toString());
        }

        for(Map.Entry<String,String> o : others.entrySet()){
            values.put(o.getKey(),o.getValue());
        }
        return values;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("title", title)
                .add("displayName", displayName)
                .add("filePath", filePath)
                .add("mimeType", mimeType)
                .add("description", description)
                .add("dateTaken", dateTaken)
                .add("others", otherString())
                .toString();

    }

    private String otherString() {
        StringBuilder sb = new StringBuilder();
        for (String name : others.keySet()) {
            String value = others.get(name);
            if (sb.length() > 0) sb.append(" ");
            sb.append(name + "=" + value);
        }
        return sb.toString();
    }
}
