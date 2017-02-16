package com.teramatrix.xfusionhero.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.teramatrix.xfusionhero.DateSelectionActivity;
import com.teramatrix.xfusionhero.MainActivity;
import com.teramatrix.xfusionhero.R;

/**
 * Created by arun.singh on 11/7/2016.
 */
public class AppUtils {


    public static void setAppBarSubTitle_default(Context context) {
        SPUtils spUtils = new SPUtils(context);
        String startDate = spUtils.getString(SPUtils.API_PARAM_START_DATE);
        String endDate = spUtils.getString(SPUtils.API_PARAM_END_DATE);
        if (startDate.isEmpty() || endDate.isEmpty()) {
            startDate = GeneralUtils.getCurrentMonthStartingDate("yyyy-MM-dd");
            endDate = GeneralUtils.getTodayDate("yyyy-MM-dd");
            spUtils.setValue(SPUtils.API_PARAM_START_DATE, startDate);
            spUtils.setValue(SPUtils.API_PARAM_END_DATE, endDate);
        }
        ((MainActivity) context).setAppBarSubTitile(GeneralUtils.format_DateString_From_One_Pattern_To_Another_Pattern(startDate, "yyy-MM-dd", "MMM dd") + " - " + GeneralUtils.format_DateString_From_One_Pattern_To_Another_Pattern(endDate, "yyy-MM-dd", "MMM dd"));
    }

    public static void setAppBarSubTitile_update_Default(Context context, String start_date, String end_date) {
        SPUtils spUtils = new SPUtils(context);
        spUtils.setValue(SPUtils.API_PARAM_START_DATE, start_date);
        spUtils.setValue(SPUtils.API_PARAM_END_DATE, end_date);
        setAppBarSubTitle_default(context);
    }

    public static void setAppBarSubTitile_update(Context context, String start_date, String end_date) {
        ((MainActivity) context).setAppBarSubTitile(GeneralUtils.format_DateString_From_One_Pattern_To_Another_Pattern(start_date, "yyy-MM-dd", "MMM dd") + " - " + GeneralUtils.format_DateString_From_One_Pattern_To_Another_Pattern(end_date, "yyy-MM-dd", "MMM dd"));

    }

    public static void setAppBarSubTitile_update(Context context, String subTitle) {
        ((MainActivity) context).setAppBarSubTitile(subTitle);

    }

    //Set appbar toolbar click event
    public static void enableClickEventOnToolbar(final Activity activity) {
        activity.findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivityForResult(new Intent(activity, DateSelectionActivity.class), MainActivity.DATE_CHANGE_EVENT_CODE);
            }
        });
    }

    public static void disableClickEventOnToolbar(final Activity activity) {
        activity.findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    public static int getIconForNotification(int notification_code) {
        switch (notification_code) {
            case 1004:
            case 1005:
            case 1015:
            case 1016:
            case 1020:
            case 1021:
            case 1022:
            case 1023:
            case 1024:
            case 1028:
            case 1029:
            case 1030:
            case 1035:
            case 1036:
            case 1037: {

                return R.mipmap.ic_settings_remote_black_24dp;
            }
            case 1001:
            case 1002:
            case 1003:
            case 1010:
            case 1031:
            case 1013: {

                return R.mipmap.ic_date_range_black_24dp;
            }
            default: {
                return R.mipmap.ic_description_black_24dp;
            }

        }
    }

    public static int getIconForAlert(String action_taken) {
        if (action_taken == null) {
//            return R.mipmap.ic_error_outline_black_24dp;
        } else if (action_taken.equalsIgnoreCase("Ignore")) {
//            return R.mipmap.ic_error_outline_black_24dp;
        } else if (action_taken.equalsIgnoreCase("UNPLANNED")) {
//            return R.mipmap.ic_error_outline_black_24dp;

        }


        return R.drawable.shape_hollow_circle_14dp;
    }


    public static void placeTextORIcon(Context context, TextView textView, ImageView imageView, String percent_text) {
        try {
            float percent = Float.parseFloat(percent_text);
            imageView.setAlpha(0.5f);
            if (percent < 0) {

                textView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                if (percent == -1) {

                    imageView.setImageResource(R.mipmap.ic_block_black_24dp);
                } else if (percent == -2) {
                    imageView.setImageResource(R.mipmap.ic_warning_black_24dp);
                }
            } else {

                textView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                textView.setText(percent_text + "%");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
