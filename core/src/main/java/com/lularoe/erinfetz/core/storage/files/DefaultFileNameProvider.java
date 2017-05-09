package com.lularoe.erinfetz.core.storage.files;

import com.google.common.base.Strings;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultFileNameProvider implements FileNameProvider{
    private final String prefix;
    private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";

    public DefaultFileNameProvider(){
        this("");
    }
    public DefaultFileNameProvider(String prefix){
        this.prefix = prefix;
    }

    @Override
    public String provide() {

        String timeStamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());

        String fn = Strings.isNullOrEmpty(prefix)?
                timeStamp :
                String.format("%s_%s", prefix,timeStamp);

        return fn;
    }
}
