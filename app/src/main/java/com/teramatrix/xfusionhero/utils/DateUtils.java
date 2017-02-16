package com.teramatrix.xfusionhero.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by arun.singh on 11/10/2016.
 */
public class DateUtils {


    public static String getCurrentUtcTime3() {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormatGmt.format(new Date());
    }

    public static String convertUtcTimeToIST(String UtcTime) {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        try {
            Date date = dateFormatGmt.parse(UtcTime);
            //Add 5:30 hours to UTC Time
            Date newDate = new Date(date.getTime() + TimeUnit.MINUTES.toMillis(330));
            String ist_string = dateFormatGmt.format(newDate.getTime());
            return ist_string;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getCurrentIST_Time() {
        return convertUtcTimeToIST(getCurrentUtcTime3());
    }
}
