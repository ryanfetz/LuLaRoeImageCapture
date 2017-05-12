package com.lularoe.erinfetz.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by ryan.fetz on 5/12/2017.
 */

public class DateTimeUtils {
    private DateTimeUtils(){

    }


    public static final int SECOND = 1000;
    public static final int MINUTE = 60 * SECOND;
    public static final int HOUR = 60 * MINUTE;
    public static final int DAY = 24 * HOUR;


    public static final float TIME_UNIT_SECONDS = 1000f;
    public static final float TIME_UNIT_MINUTE = 60f;
    public static final float TIME_UNIT_HOUR= 60f;
    public static final float TIME_UNIT_DAY = 24f;

    public static long secondsToMillis(float sec){
        return (long)(sec * TIME_UNIT_SECONDS);
    }

    public static long minutesToMillis(float min){
        return (int) (min * TIME_UNIT_SECONDS * TIME_UNIT_MINUTE);
    }

    public static String getDurationString(long durationMs) {
        return String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(durationMs),
                TimeUnit.MILLISECONDS.toSeconds(durationMs) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(durationMs))
        );
    }


    public static final String DATE_FORMAT_STRING = "yyyyMMdd_HHmmss";
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT=new SimpleDateFormat(DATE_FORMAT_STRING, Locale.getDefault());


    public static String timeStamp(){
        return DEFAULT_DATE_FORMAT.format(new Date());
    }

    public static String timeStamp(Date d){
        return DEFAULT_DATE_FORMAT.format(d);
    }
}
