package com.teramatrix.xfusionhero.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.Window;

import com.teramatrix.xfusionhero.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by arun.singh on 10/26/2016.
 */
public class GeneralUtils {

    public static String format_DateString_From_Millisecond_To_Another_Pattern(String date_milliseconds, String expected_pattern) {
        try {
            long timeStamp = Long.parseLong(date_milliseconds);
            Date date = new Date(timeStamp);
            SimpleDateFormat dateFormat_expected = new SimpleDateFormat(expected_pattern);
            return dateFormat_expected.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static Boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo i : info) {
                    if (i.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static Boolean isConnected(Context context,int res_layout) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo i : info) {
                    if (i.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        showCustomDialogPopUp(context,res_layout);
        return false;
    }
    public static String format_DateString_From_One_Pattern_To_Another_Pattern(String date_string, String current_pattern, String expected_pattern) {
        try {
            SimpleDateFormat dateFormat_current = new SimpleDateFormat(current_pattern);
            SimpleDateFormat dateFormat_expected = new SimpleDateFormat(expected_pattern);

            Date date = dateFormat_current.parse(date_string);

            return dateFormat_expected.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTodayDate(String pattern)
    {
        Calendar c = Calendar.getInstance();   // this takes current date
        return  format_DateString_From_Millisecond_To_Another_Pattern(c.getTime().getTime()+"",pattern);
    }
    //Get Current month start and end day
    public static String getCurrentMonthStartingDate(String pattern)
    {
        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);
        return  format_DateString_From_Millisecond_To_Another_Pattern(c.getTime().getTime()+"",pattern);
    }
    public static String getCurrentMonthEndDate(String pattern)
    {
        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return  format_DateString_From_Millisecond_To_Another_Pattern(c.getTime().getTime()+"",pattern);
    }
    //Get last month start and end day
    public static String getLastMonthStartingDate(String pattern)
    {
        Calendar c = Calendar.getInstance();   // this takes current date
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DATE, 1);
        return  format_DateString_From_Millisecond_To_Another_Pattern(c.getTime().getTime()+"",pattern);
    }
    public static String getLastMonthEndDate(String pattern)
    {
        Calendar c = Calendar.getInstance();   // this takes current date
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return  format_DateString_From_Millisecond_To_Another_Pattern(c.getTime().getTime()+"",pattern);
    }
    //Get last 30 days start day
    public static String getLastThirtyDaysStartDate(String pattern)
    {
        Calendar c = Calendar.getInstance();   // this takes current date
        c.add(Calendar.DATE, -30);
        return  format_DateString_From_Millisecond_To_Another_Pattern(c.getTime().getTime()+"",pattern);
    }

    public static void showCustomDialogPopUp(Context context, int res_layout) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(res_layout);
        dialog.findViewById(R.id.txt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public static String getMonth(int m) {
        switch (m) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
            default:
                return "";
        }
    }
}
