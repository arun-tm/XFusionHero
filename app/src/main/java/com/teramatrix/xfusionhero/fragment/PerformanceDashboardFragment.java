package com.teramatrix.xfusionhero.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.teramatrix.xfusionhero.CustomDonutProgress;
import com.teramatrix.xfusionhero.R;
import com.teramatrix.xfusionhero.controller.RESTClient;
import com.teramatrix.xfusionhero.utils.GeneralUtils;
import com.teramatrix.xfusionhero.utils.SPUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by arun.singh on 10/20/2016.
 */
public class PerformanceDashboardFragment extends Fragment implements HomeFragment.ChildFragment {

    private View view;
    private CustomDonutProgress donutProgress;
    private okhttp3.Call call;

    class AlertAndNotificationWeeklyCounts {
        String week_start_date = "10 Oct 2016";
        int alert_count;
        int notification_count;

        public AlertAndNotificationWeeklyCounts(String week_start_date, int alert_count, int notification_count) {
            this.week_start_date = week_start_date;
            this.alert_count = alert_count;
            this.notification_count = notification_count;
        }

    }

    ArrayList<AlertAndNotificationWeeklyCounts> alertAndNotificationWeeklyCountses = new ArrayList<AlertAndNotificationWeeklyCounts>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_performance_dashboard, null);
            intViews();
        }
        return view;
    }

    private void intViews() {
        donutProgress = (CustomDonutProgress) view.findViewById(R.id.fuel_progress_indicator);
            /*donutProgress.setMax(Integer.parseInt(fuel_max_value));*/
        donutProgress.setMax(100);
        donutProgress.setProgress(30);
        donutProgress.setTextSize(60);
        donutProgress.setTextColor(getActivity().getResources().getColor(R.color.color_nam_menu_dark_gray));
        donutProgress.setSuffixText("");
        donutProgress.setInnerBottomText("FINAL REPORT");
        donutProgress.setInnerBottomTextColor(getActivity().getResources().getColor(R.color.color_nam_menu_light_gray));
        donutProgress.setInnerBottomTextSize(27);

        loadAlertNotificationBarChart(alertAndNotificationWeeklyCountses);
    }

    private void loadAlertNotificationBarChart(ArrayList<AlertAndNotificationWeeklyCounts> alertAndNotificationWeeklyCountses) {

        try {
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.alert_notification_view);
            linearLayout.removeAllViews();

            for (int i = 0; i < alertAndNotificationWeeklyCountses.size(); i++) {
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.alert_notification_bar, null);

                //Set Alert bar height and value
                View alert_bar = v.findViewById(R.id.alert_bar);
                LinearLayout.LayoutParams layoutParams_alert_bar = (LinearLayout.LayoutParams) alert_bar.getLayoutParams();

                if (alertAndNotificationWeeklyCountses.size() > i) {
                    layoutParams_alert_bar.weight = (float) alertAndNotificationWeeklyCountses.get(i).alert_count;

                    if (alertAndNotificationWeeklyCountses.get(i).alert_count == 0) {
                        ((TextView) v.findViewById(R.id.alert_bar_value)).setVisibility(View.INVISIBLE);
                    }
                    else
                        ((TextView) v.findViewById(R.id.alert_bar_value)).setText(alertAndNotificationWeeklyCountses.get(i).alert_count + "");

                } else {
                    layoutParams_alert_bar.weight = 0f;
                    ((TextView) v.findViewById(R.id.alert_bar_value)).setVisibility(View.INVISIBLE);
                }
                alert_bar.setLayoutParams(layoutParams_alert_bar);


                //Set Notification bar height and value
                View notification_bar = v.findViewById(R.id.notification_bar);
                LinearLayout.LayoutParams layoutParams_notifiation_bar = (LinearLayout.LayoutParams) notification_bar.getLayoutParams();
                if (alertAndNotificationWeeklyCountses.size() > i) {
                    layoutParams_notifiation_bar.weight = alertAndNotificationWeeklyCountses.get(i).notification_count;

                    if (alertAndNotificationWeeklyCountses.get(i).notification_count == 0) {
                        ((TextView) v.findViewById(R.id.notification_bar_value)).setVisibility(View.INVISIBLE);
                    }
                    else
                        ((TextView) v.findViewById(R.id.notification_bar_value)).setText(alertAndNotificationWeeklyCountses.get(i).notification_count + "");

                } else {
                    layoutParams_notifiation_bar.weight = 0f;
                    ((TextView) v.findViewById(R.id.notification_bar_value)).setVisibility(View.INVISIBLE);
                }
                notification_bar.setLayoutParams(layoutParams_notifiation_bar);


                //Set Date range in bar chart
                if (alertAndNotificationWeeklyCountses.size() > i) {
                    if (alertAndNotificationWeeklyCountses.get(i) != null) {
                        String s = alertAndNotificationWeeklyCountses.get(i).week_start_date;
                        String f = GeneralUtils.format_DateString_From_Millisecond_To_Another_Pattern(s, "dd MMM yyyy");
                        ((TextView) v.findViewById(R.id.date_range)).setText(f);
                    }
                }else
                {
                    ((TextView) v.findViewById(R.id.date_range)).setText("dd MMM yyyy");
                }
                linearLayout.addView(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void receiveData(HashMap<String, String> dashboardKPIValues) {


        //Retriving Dashboad Performance KPI
        String total_master_part_list = dashboardKPIValues.get("total_component_in_inventory");
        String total_daily_report = dashboardKPIValues.get("total_daily_report");
        String total_ok_final_report = dashboardKPIValues.get("total_ok_final_report");
        String total_NG_final_report = dashboardKPIValues.get("total_NG_final_report");
        String total_final_report = dashboardKPIValues.get("total_final_report");
        String total_punched_in_component = dashboardKPIValues.get("total_punched_in_component");
        String total_punched_out_component = dashboardKPIValues.get("total_punched_out_component");
        String total_disposed = dashboardKPIValues.get("total_disposed");
        String total_notification = dashboardKPIValues.get("total_notification");
        String total_alert = dashboardKPIValues.get("total_alert");


        //Setting Dashboad Performance KPI
        ((TextView) view.findViewById(R.id.txt_total_master_part_list)).setText(total_master_part_list);
        ((TextView) view.findViewById(R.id.txt_total_daily_report)).setText(total_daily_report);

        ((TextView) view.findViewById(R.id.txt_report_ok)).setText(total_ok_final_report);
        ((TextView) view.findViewById(R.id.txt_report_ng)).setText(total_NG_final_report);
        ((TextView) view.findViewById(R.id.txt_report_not_submitted)).setText((Integer.parseInt(total_final_report) - (Integer.parseInt(total_ok_final_report) + Integer.parseInt(total_NG_final_report))) + "");

        ((TextView) view.findViewById(R.id.txt_component_punched_in)).setText(total_punched_in_component);
        ((TextView) view.findViewById(R.id.txt_component_punched_out)).setText(total_punched_out_component);
        ((TextView) view.findViewById(R.id.txt_component_disposed_off)).setText(total_disposed);

        ((TextView) view.findViewById(R.id.txt_total_notification)).setText(total_notification);
        ((TextView) view.findViewById(R.id.txt_total_alert)).setText(total_alert);

        donutProgress.setMax(Integer.parseInt(total_final_report));
        donutProgress.setProgress(Integer.parseInt(total_final_report));
        int val_1 = Integer.parseInt(total_ok_final_report);
        int val_2 = Integer.parseInt(total_NG_final_report);
        int val_3 = (Integer.parseInt(total_final_report) - (Integer.parseInt(total_ok_final_report) + Integer.parseInt(total_NG_final_report)));
        donutProgress.setDonutProgress(Integer.parseInt(total_final_report), val_1, val_2, val_3);
//        donutProgress.setDonutProgress(12, 6, 4, 2);

        //set progress bars for
        int punched_in = Integer.parseInt(total_punched_in_component);
        int punched_out = Integer.parseInt(total_punched_out_component);
        int disposed_off = Integer.parseInt(total_disposed);

        int max = punched_in + punched_out + disposed_off;

        View punched_in_bar = view.findViewById(R.id.bar_punched_in);
        LinearLayout.LayoutParams layoutParams_punched_in = (LinearLayout.LayoutParams) punched_in_bar.getLayoutParams();

        System.out.println("Weight " + (float) punched_in / (float) max * 10);
        layoutParams_punched_in.weight = (float) punched_in / (float) max * 10;
        punched_in_bar.setLayoutParams(layoutParams_punched_in);


        View punched_out_bar = view.findViewById(R.id.bar_punched_out);
        LinearLayout.LayoutParams layoutParams_punched_out = (LinearLayout.LayoutParams) punched_out_bar.getLayoutParams();
        System.out.println("Weight " + (float) punched_out / (float) max * 10);
        layoutParams_punched_out.weight = (float) punched_out / (float) max * 10;
        punched_out_bar.setLayoutParams(layoutParams_punched_out);

        View bar_to_be_disposed_off = view.findViewById(R.id.bar_to_be_disposed_off);
        LinearLayout.LayoutParams layoutParams_bar_to_be_disposed_off = (LinearLayout.LayoutParams) bar_to_be_disposed_off.getLayoutParams();
        System.out.println("Weight " + (float) disposed_off / (float) max * 10);
        layoutParams_bar_to_be_disposed_off.weight = (float) disposed_off / (float) max * 10;
        bar_to_be_disposed_off.setLayoutParams(layoutParams_bar_to_be_disposed_off);


        //Get weekly counts  of Alert and notification from api and draw on bar chart
        getDashboard_Performance_Alerts_weekly_count();
    }

    private void getDashboard_Performance_Alerts_weekly_count() {
//        dialog.show();
        SPUtils spUtils = new SPUtils(getActivity());
        String body = "token=" + new SPUtils(getActivity()).getToken() +
                "&from_date =" + spUtils.getString(SPUtils.API_PARAM_START_DATE) +
                "&end_date:=" + spUtils.getString(SPUtils.API_PARAM_END_DATE) ;

        call = RESTClient.dashboard_Performance_alerts_weekly_count(getActivity(), body, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                System.out.println("onFailure");
//                dialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {

                        alertAndNotificationWeeklyCountses.clear();
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.has("valid")) {
                            String valid = jsonObject.getString("valid");
                            if (valid.equalsIgnoreCase("true")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("object");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String week_start_date = jsonObject1.getString("week_start_date");
                                    String alert_count = jsonObject1.getString("count");

                                    alertAndNotificationWeeklyCountses.add(new AlertAndNotificationWeeklyCounts(week_start_date, Integer.parseInt(alert_count), 0));
                                }

                                getDashboard_Performance_Notifications_weekly_count();
                            } else {
                            }
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
//                        dialog.dismiss();
                    }
                } else {
//                    dialog.dismiss();
                }
            }
        });
    }

    private void getDashboard_Performance_Notifications_weekly_count() {
//        dialog.show();
        SPUtils spUtils = new SPUtils(getActivity());
        String body = "token=" + new SPUtils(getActivity()).getToken() +
                "&from_date =" + spUtils.getString(SPUtils.API_PARAM_START_DATE) +
                "&end_date:=" + spUtils.getString(SPUtils.API_PARAM_END_DATE);

        call = RESTClient.dashboard_Performance_notification_weekly_count(getActivity(), body, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                System.out.println("onFailure");
//                dialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {

                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.has("valid")) {
                            String valid = jsonObject.getString("valid");
                            if (valid.equalsIgnoreCase("true")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("object");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String week_start_date = jsonObject1.getString("week_start_date");
                                    String notification_count = jsonObject1.getString("count");

                                    alertAndNotificationWeeklyCountses.get(i).notification_count = Integer.parseInt(notification_count);
                                }
                            } else {
                            }
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
//                        dialog.dismiss();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadAlertNotificationBarChart(alertAndNotificationWeeklyCountses);
                            }
                        });
                    }
                } else {
//                    dialog.dismiss();
                }
            }
        });
    }

}
