package com.lularoe.erinfetz.core.storage.files;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.lularoe.erinfetz.core.ContentValuesBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultFileInfoProvider implements FileInfoProvider {
    private final String prefix;
    protected final SimpleDateFormat dateFormat;

    protected static final String DATE_FORMAT = "yyyyMMdd_HHmmss";

    public DefaultFileInfoProvider(){
        this("");
    }
    public DefaultFileInfoProvider(String prefix){
        this(prefix, new SimpleDateFormat(DATE_FORMAT));
    }
    public DefaultFileInfoProvider(String prefix, SimpleDateFormat dateFormat){
        this.prefix = prefix;
        this.dateFormat = dateFormat;
    }

    @Override
    public FileInfo provide() {

        Date date = new Date();

        String timeStamp = dateFormat.format(date);

        String fn = Strings.isNullOrEmpty(prefix)?
                timeStamp :
                String.format("%s_%s", prefix,timeStamp);

        return new DefaultFileInfo(fn, ContentValuesBuilder.start().title(fn).dateTaken(date));
    }

    public static class DefaultFileInfo implements FileInfo{
        private final String name;
        private final ContentValuesBuilder contentValues;

        public DefaultFileInfo(String name, ContentValuesBuilder cv){
            this.name = name;
            this.contentValues = cv;
        }
        @Override
        public String name() {
            return name;
        }

        @Override
        public ContentValuesBuilder contentValues() {
            return contentValues;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("name", name)
                    .add("contentValues", contentValues)
                    .toString();
        }
    }
}
