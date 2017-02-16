package com.teramatrix.xfusionhero.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teramatrix.xfusionhero.CustomDonutProgress;
import com.teramatrix.xfusionhero.R;
import com.teramatrix.xfusionhero.model.ComponentTestCycle;
import com.teramatrix.xfusionhero.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

import static com.google.android.gms.analytics.internal.zzy.e;
import static okhttp3.Protocol.get;

/**
 * Created by arun.singh on 10/20/2016.
 */
public class MainDashboardFragment extends Fragment implements HomeFragment.ChildFragment, View.OnClickListener {
    private View view;
    private CustomDonutProgress donutProgress;
    HashMap<String, String> dashboardKPIValues;
    HashMap<String, ComponentTestCycle> componentTestCycleHashMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main_dashboard, null);
            intViews();
            System.out.println(" Test my component: onCreateView");
        }
        return view;
    }

    private void intViews() {
        donutProgress = (CustomDonutProgress) view.findViewById(R.id.testing_planner_indicator);
        donutProgress.setMax(25);
        donutProgress.setProgress(10);
//        donutProgress.setTextSize(60);
        donutProgress.setSuffixText("");
        donutProgress.setTextSize(0);
        donutProgress.setInnerBottomText("TOTAL");
        donutProgress.setInnerBottomTextSize(0);
        donutProgress.setTextColor(getActivity().getResources().getColor(R.color.color_nam_menu_dark_gray));
        donutProgress.setInnerBottomTextColor(getActivity().getResources().getColor(R.color.color_nam_menu_light_gray));

        view.findViewById(R.id.lay_side_stand).setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("MainDashboardFragment: onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("MainDashboardFragment: onPause");
    }

    @Override
    public void receiveData(HashMap<String, String> dashboardKPIValues) {

        try {
            this.dashboardKPIValues = dashboardKPIValues;
            System.out.println(" Test my component: receiveData");
            //Retriving Dashboad Performance KPI
            String total_components_in_planner = dashboardKPIValues.get("total_components_in_planner");
            String total_components_under_testing = dashboardKPIValues.get("total_components_under_testing");
            String total_unplanned_components = dashboardKPIValues.get("total_unplanned_components");
            String total_tested_components = dashboardKPIValues.get("total_tested_components");
            final String ro_horn_testing_status = dashboardKPIValues.get("ro_horn_testing_status");
            final String ro_relay_testing_status = dashboardKPIValues.get("ro_relay_testing_status");
            final String ro_sidestand_testing_status = dashboardKPIValues.get("ro_sidestand_testing_status");
            final String ro_keyset_testing_status = dashboardKPIValues.get("ro_keyset_testing_status");
            final String dust_testing_status = dashboardKPIValues.get("dust_testing_status");
            final String shower_testing_status = dashboardKPIValues.get("shower_testing_status");
            String planner_name = dashboardKPIValues.get("planner_name");
            String planner_number = dashboardKPIValues.get("planner_number");
            String planner_version = dashboardKPIValues.get("planner_version");

//        Setting Dashboad Performance KPI

            ((TextView) view.findViewById(R.id.txt_total_componenetin_planner_value)).setText(total_components_in_planner + "");
            int val_1 = Integer.parseInt(total_components_under_testing);
            int val_2 = Integer.parseInt(total_unplanned_components);
            int val_3 = Integer.parseInt(total_tested_components);
            donutProgress.setMax(val_1 + val_2 + val_3);
            donutProgress.setProgress(val_1 + val_2 + val_3);
            donutProgress.setDonutProgress(val_1 + val_2 + val_3, val_1, val_2, val_3);

            ((TextView) view.findViewById(R.id.txt_component_under_test)).setText(total_components_under_testing);
            ((TextView) view.findViewById(R.id.txt_component_unplanned)).setText(total_unplanned_components);
            ((TextView) view.findViewById(R.id.txt_component_tested)).setText(total_tested_components);

            try {

                AppUtils.placeTextORIcon(getActivity(), ((TextView) view.findViewById(R.id.txt_testing_progress_horn)), (ImageView) view.findViewById(R.id.img_horn), ro_horn_testing_status);
                AppUtils.placeTextORIcon(getActivity(), ((TextView) view.findViewById(R.id.txt_testing_progress_relay)), (ImageView) view.findViewById(R.id.img_relay), ro_relay_testing_status);
                AppUtils.placeTextORIcon(getActivity(), ((TextView) view.findViewById(R.id.txt_testing_progress_keyset)), (ImageView) view.findViewById(R.id.img_keyset), ro_keyset_testing_status);
                AppUtils.placeTextORIcon(getActivity(), ((TextView) view.findViewById(R.id.txt_testing_progress_side_stand_switch)), (ImageView) view.findViewById(R.id.img_side_stand), ro_sidestand_testing_status);
                AppUtils.placeTextORIcon(getActivity(), ((TextView) view.findViewById(R.id.txt_testing_progress_dust)), (ImageView) view.findViewById(R.id.img_dust), dust_testing_status);
                AppUtils.placeTextORIcon(getActivity(), ((TextView) view.findViewById(R.id.txt_testing_progress_shower)), (ImageView) view.findViewById(R.id.img_shower), shower_testing_status);


            } catch (Exception e) {
                e.printStackTrace();
            }
            ((TextView) view.findViewById(R.id.txt_report_heading_top)).setText(planner_name + " " + planner_number + ".V" + planner_version);
            ((TextView) view.findViewById(R.id.txt_report_heading)).setVisibility(View.INVISIBLE);

            //Equipment Engagement Status

            String equipment_engagement_status = dashboardKPIValues.get("equipment_engagement_status");

            try {

                JSONObject jsonObject = new JSONObject(equipment_engagement_status);
                if (jsonObject.has("valid")) {
                    String valid = jsonObject.getString("valid");
                    if (valid.equalsIgnoreCase("true")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("object");

                        LinearLayout euipment_engagment_list = (LinearLayout) view.findViewById(R.id.equipment_status_host_layout);
                        euipment_engagment_list.removeAllViews();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String equipment = jsonObject1.getString("equipment");
                            String total_hours_month = jsonObject1.getString("total_hours_month");
                            String completed_hours = jsonObject1.getString("completed_hours");
                            String total_testing_hours = jsonObject1.getString("total_testing_hours");
                            String test_name = jsonObject1.getString("test_name");


                            View row = LayoutInflater.from(getActivity()).inflate(R.layout.equipment_enagement_status_row,null);
                            ((TextView)row.findViewById(R.id.equipment_name)).setText(equipment);
                            ((TextView)row.findViewById(R.id.txt_horn_total_testing_hours)).setText(total_testing_hours+"");
                            ((TextView)row.findViewById(R.id.txt_horn_total_completed_hours)).setText(completed_hours+"");
                            ((TextView)row.findViewById(R.id.equipment_test_name)).setText(test_name.toUpperCase()+"");

                            euipment_engagment_list.addView(row);
                        }

                    } else {
                    }
                } else {

                }
            } catch (JSONException ee) {
                ee.printStackTrace();
            }



            //Setting progress bar for Testing components
            View bar_testing_horn = view.findViewById(R.id.bar_testing_horn);
            LinearLayout.LayoutParams layoutParams_bar_testing_horn = (LinearLayout.LayoutParams) bar_testing_horn.getLayoutParams();
            layoutParams_bar_testing_horn.weight = Float.parseFloat(ro_horn_testing_status);
            bar_testing_horn.setLayoutParams(layoutParams_bar_testing_horn);

            View bar_testing_relay = view.findViewById(R.id.bar_testing_relay);
            LinearLayout.LayoutParams layoutParams_bar_testing_relay = (LinearLayout.LayoutParams) bar_testing_relay.getLayoutParams();
            layoutParams_bar_testing_relay.weight = Float.parseFloat(ro_relay_testing_status);
            bar_testing_relay.setLayoutParams(layoutParams_bar_testing_relay);

            View bar_testing_keyset = view.findViewById(R.id.bar_testing_keyset);
            LinearLayout.LayoutParams layoutParams_bar_testing_keyset = (LinearLayout.LayoutParams) bar_testing_keyset.getLayoutParams();
            layoutParams_bar_testing_keyset.weight = Float.parseFloat(ro_keyset_testing_status);
            bar_testing_keyset.setLayoutParams(layoutParams_bar_testing_keyset);

            View bar_testing_side_stand = view.findViewById(R.id.bar_testing_side_stand);
            LinearLayout.LayoutParams layoutParams_bar_testing_ss = (LinearLayout.LayoutParams) bar_testing_side_stand.getLayoutParams();
            layoutParams_bar_testing_ss.weight = Float.parseFloat(ro_sidestand_testing_status);
            bar_testing_side_stand.setLayoutParams(layoutParams_bar_testing_ss);

            View bar_testing_dust = view.findViewById(R.id.bar_testing_dust);
            LinearLayout.LayoutParams layoutParams_bar_testing_dust = (LinearLayout.LayoutParams) bar_testing_dust.getLayoutParams();
            layoutParams_bar_testing_dust.weight = Float.parseFloat(dust_testing_status);
            bar_testing_dust.setLayoutParams(layoutParams_bar_testing_dust);

            View bar_testing_shower = view.findViewById(R.id.bar_testing_shower);
            LinearLayout.LayoutParams layoutParams_bar_testing_shower = (LinearLayout.LayoutParams) bar_testing_shower.getLayoutParams();
            layoutParams_bar_testing_shower.weight = Float.parseFloat(shower_testing_status);
            bar_testing_shower.setLayoutParams(layoutParams_bar_testing_shower);


            //Set RO Logs KPIs on main dashboard

            if (componentTestCycleHashMap != null) {
                ComponentTestCycle componentTestCycle = componentTestCycleHashMap.get("SIDE STAND SWITCH");
                if (componentTestCycle != null) {
                    ((TextView) view.findViewById(R.id.txt_side_stand_cycle_value)).setText(NumberFormat.getNumberInstance(Locale.US).format(componentTestCycle.max_test_cycle_value) + "");
                    ((TextView) view.findViewById(R.id.txt_side_stand_cycle_up_count)).setText(componentTestCycle.count_total_component_in_max_cycle + " UNITS");
                    ((TextView) view.findViewById(R.id.txt_side_stand_cycle_down_count)).setText(componentTestCycle.Count_total_component_in_not_max_cycle + " UNITS");
                } else {
                    ((TextView) view.findViewById(R.id.txt_side_stand_cycle_value)).setText("0");
                    ((TextView) view.findViewById(R.id.txt_side_stand_cycle_up_count)).setText("0 Units");
                    ((TextView) view.findViewById(R.id.txt_side_stand_cycle_down_count)).setText("0 Units");
                }

                componentTestCycle = componentTestCycleHashMap.get("KEYSET")==null?componentTestCycleHashMap.get("LOCK"):componentTestCycleHashMap.get("KEYSET");

                if (componentTestCycle != null) {

                    ((TextView) view.findViewById(R.id.txt_keyset_cycle_value)).setText(NumberFormat.getNumberInstance(Locale.US).format(componentTestCycle.max_test_cycle_value) + "");
                    ((TextView) view.findViewById(R.id.txt_keyset_cycle_up_count)).setText(componentTestCycle.count_total_component_in_max_cycle + " Units");
                    ((TextView) view.findViewById(R.id.txt_keyset_cycle_down_count)).setText(componentTestCycle.Count_total_component_in_not_max_cycle + " Units");
                } else {
                    ((TextView) view.findViewById(R.id.txt_keyset_cycle_value)).setText("0");
                    ((TextView) view.findViewById(R.id.txt_keyset_cycle_up_count)).setText("0 Units");
                    ((TextView) view.findViewById(R.id.txt_keyset_cycle_down_count)).setText("0 Units");
                }
                componentTestCycle = componentTestCycleHashMap.get("RELAY");
                if (componentTestCycle != null) {
                    ((TextView) view.findViewById(R.id.txt_relay_cycle)).setText(NumberFormat.getNumberInstance(Locale.US).format(componentTestCycle.max_test_cycle_value) + "");
                    ((TextView) view.findViewById(R.id.txt_relay_cycle_up_count)).setText(componentTestCycle.count_total_component_in_max_cycle + " Units");
                    ((TextView) view.findViewById(R.id.txt_relay_cycle_down_count)).setText(componentTestCycle.Count_total_component_in_not_max_cycle + " Units");
                } else {
                    ((TextView) view.findViewById(R.id.txt_relay_cycle)).setText("0");
                    ((TextView) view.findViewById(R.id.txt_relay_cycle_up_count)).setText("0 Units");
                    ((TextView) view.findViewById(R.id.txt_relay_cycle_down_count)).setText("0 Units");
                }
                componentTestCycle = componentTestCycleHashMap.get("HORN");
                if (componentTestCycle != null) {
                    ((TextView) view.findViewById(R.id.txt_horn_cycle)).setText(NumberFormat.getNumberInstance(Locale.US).format(componentTestCycle.max_test_cycle_value) + "");
                    ((TextView) view.findViewById(R.id.txt_horn_cycle_up_count)).setText(componentTestCycle.count_total_component_in_max_cycle + " Units");
                    ((TextView) view.findViewById(R.id.txt_horn_cycle_down_count)).setText(componentTestCycle.Count_total_component_in_not_max_cycle + " Units");
                } else {
                    ((TextView) view.findViewById(R.id.txt_horn_cycle)).setText("0");
                    ((TextView) view.findViewById(R.id.txt_horn_cycle_up_count)).setText("0 Units");
                    ((TextView) view.findViewById(R.id.txt_horn_cycle_down_count)).setText("0 Units");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRoLogsData(HashMap<String, ComponentTestCycle> componentTestCycleHashMap) {
        this.componentTestCycleHashMap = componentTestCycleHashMap;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.lay_side_stand: {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                RLogsFragmnet rLogsFragmnet = new RLogsFragmnet();
                Bundle bundle = new Bundle();
                bundle.putSerializable("rl_logs", componentTestCycleHashMap);
                rLogsFragmnet.setArguments(bundle);
                fragmentTransaction.replace(R.id.content_main, rLogsFragmnet).commit();
            }
        }
    }
}
