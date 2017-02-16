package com.teramatrix.xfusionhero.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.teramatrix.xfusionhero.DateSelectionActivity;
import com.teramatrix.xfusionhero.LoginActivity;
import com.teramatrix.xfusionhero.MainActivity;
import com.teramatrix.xfusionhero.R;
import com.teramatrix.xfusionhero.controller.RESTClient;
import com.teramatrix.xfusionhero.model.ComponentTestCycle;
import com.teramatrix.xfusionhero.model.ComponentTestLog;
import com.teramatrix.xfusionhero.model.DateChagneMessageEvent;
import com.teramatrix.xfusionhero.utils.AppUtils;
import com.teramatrix.xfusionhero.utils.GeneralUtils;
import com.teramatrix.xfusionhero.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by arun.singh on 10/20/2016.
 */
public class HomeFragment extends Fragment {

    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AlertDialog dialog;
    private okhttp3.Call call;
    private HashMap<String, String> dashboardKPIValues = new HashMap<String, String>();


    ChildFragment performanceDashboardFragment;
    ChildFragment mainDashboardFragment;

    private String startDate;
    private String endDate;
    private int opened_tab_position;

    interface ChildFragment {
        public void receiveData(HashMap<String, String> dashboardKPIValues);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        System.out.println("Home onCreate");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (opened_tab_position == 0) {
            //Set default date as subtitle for performance dashboard fragment
            AppUtils.setAppBarSubTitle_default(getActivity());
            AppUtils.enableClickEventOnToolbar(getActivity());
        } else if (opened_tab_position == 1) {
            //Set "Current" as subtitle for Main dashboard fragment
            AppUtils.setAppBarSubTitile_update(getActivity(), "Current");
            AppUtils.disableClickEventOnToolbar(getActivity());
        }
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, null);
            initViews();

            if (GeneralUtils.isConnected(getActivity(), R.layout.pop_up_dialog))
                getDashboard_Performance_KPIData();
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("xFusion Hero");


        EventBus.getDefault().register(this);
        System.out.println("Home onCreateView");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("Home onStart");

    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("Home onResume");

    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("Home onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("Home onStop");
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
        System.out.println("Home onDestroyView");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MainActivity.DATE_CHANGE_EVENT_CODE) {
                String startDate = data.getStringExtra("startDate");
                String endDate = data.getStringExtra("endDate");
                EventBus.getDefault().post(new DateChagneMessageEvent(startDate, endDate));
            }
        }
    }

    //This method will be called when a DateChagneMessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDateChagneMessageEvent(DateChagneMessageEvent dateChagneMessageEvent) {
        if (GeneralUtils.isConnected(getActivity(), R.layout.pop_up_dialog)) {
            startDate = dateChagneMessageEvent.startDate;
            endDate = dateChagneMessageEvent.endDate;

            AppUtils.setAppBarSubTitile_update_Default(getActivity(), startDate, endDate);

            getDashboard_Performance_KPIData();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh: {

                try {
                /*Refresh Ticket Details. Call All Ticket APIs Again*/
                    if (GeneralUtils.isConnected(getActivity(), R.layout.pop_up_dialog)) {
                        getDashboard_Performance_KPIData();
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void initViews() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                opened_tab_position = tab.getPosition();
                //Change Subtitle for diffeerent page in view pager
                if (opened_tab_position == 1) {
                    //Set subtitle to "Current" when Main Dashboard is opened
                    AppUtils.setAppBarSubTitile_update(getActivity(), "Current");
                    AppUtils.disableClickEventOnToolbar(getActivity());
                } else if (opened_tab_position == 0) {
                    //Set subtitle to default dates when Performance Dashboard is opened
                    AppUtils.setAppBarSubTitle_default(getActivity());
                    AppUtils.enableClickEventOnToolbar(getActivity());
                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        dialog = new SpotsDialog(getActivity(), R.style.Custom);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());

        performanceDashboardFragment = new PerformanceDashboardFragment();
        mainDashboardFragment = new MainDashboardFragment();
        adapter.addFrag((Fragment) performanceDashboardFragment, "Performance Dashboard");
        adapter.addFrag((Fragment) mainDashboardFragment, "Main Dashboard");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void getDashboard_Performance_KPIData() {
        dialog.show();

        SPUtils spUtils = new SPUtils(getActivity());
        startDate = spUtils.getString(SPUtils.API_PARAM_START_DATE);
        endDate = spUtils.getString(SPUtils.API_PARAM_END_DATE);


        String body = "token=" + new SPUtils(getActivity()).getToken() +
                "&start_date=" + startDate +
                "&end_date=" + endDate;

        call = RESTClient.dashboard_PerformaceData(getActivity(), body, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                System.out.println("onFailure");
                dialog.dismiss();
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
                                    String kpi_name = jsonObject1.getString("name");
                                    String kpi_value = jsonObject1.getString("value");
                                    dashboardKPIValues.put(kpi_name, kpi_value);
                                }

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        performanceDashboardFragment.receiveData(dashboardKPIValues);
                                    }
                                });

                            } else {
                            }
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
//                        dialog.dismiss();
                        getDashboard_Main_MKPIData();
                    }
                } else {
                    dialog.dismiss();
                }
            }
        });
    }

    private void getDashboard_Main_MKPIData() {
        dialog.show();
        String body = "token=" + new SPUtils(getActivity()).getToken();

        call = RESTClient.dashboard_MainData(getActivity(), body, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                System.out.println("onFailure");
                dialog.dismiss();
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
                                    String kpi_name = jsonObject1.getString("name");
                                    String kpi_value = jsonObject1.getString("value");
                                    dashboardKPIValues.put(kpi_name, kpi_value);
                                }

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        mainDashboardFragment.receiveData(dashboardKPIValues);
                                    }
                                });

                            } else {
                            }
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
//                        dialog.dismiss();
//                        getDashboard_Main_Equipments_KPIData();
                        get_RO_Logs_data();
                    }
                } else {
                    dialog.dismiss();
                }
            }
        });
    }


    private void get_RO_Logs_data() {
        String body = "token=" + new SPUtils(getActivity()).getToken();
        call = RESTClient.getROLogs(getActivity(), body, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                System.out.println("onFailure");
                dialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {

                        HashMap<String, ComponentTestCycle> componentTestCycleHashMap = filterComponentROTestLogs(response.body().string());
                        ((MainDashboardFragment) mainDashboardFragment).setRoLogsData(componentTestCycleHashMap);
                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
//                        dialog.dismiss();
                        getDashboard_Main_Equipments_KPIData();
                    }
                } else {
                    dialog.dismiss();
                }
            }
        });
    }

    private void getDashboard_Main_Equipments_KPIData() {
//        dialog.show();
        String body = "token=" + new SPUtils(getActivity()).getToken();

        call = RESTClient.dashboard_MainData_Equipments(getActivity(), body, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                System.out.println("onFailure");
                dialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {

                        String res = response.body().string();
                        dashboardKPIValues.put("equipment_engagement_status" , res);

                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.has("valid")) {
                            /*String valid = jsonObject.getString("valid");
                            if (valid.equalsIgnoreCase("true")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("object");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String kpi_name = jsonObject1.getString("equipment")+"_"+jsonObject1.getString("test_name");
                                    String total_hours_month = jsonObject1.getString("total_hours_month");
                                    String completed_hours = jsonObject1.getString("completed_hours");
                                    String total_testing_hours = jsonObject1.getString("total_testing_hours");

                                    dashboardKPIValues.put(kpi_name + "_total_hours_month", total_hours_month);
                                    dashboardKPIValues.put(kpi_name + "_completed_hours", completed_hours);
                                    dashboardKPIValues.put(kpi_name + "_total_testing_hours", total_testing_hours);
                                }

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mainDashboardFragment.receiveData(dashboardKPIValues);
                                    }
                                });

                            } else {
                            }*/
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mainDashboardFragment.receiveData(dashboardKPIValues);
                                }
                            });
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
                        dialog.dismiss();
                    }
                } else {
                    dialog.dismiss();
                }
            }
        });
    }

    private HashMap<String, ComponentTestCycle> filterComponentROTestLogs(String ro_logs_json_string) {
        HashMap<String, ComponentTestCycle> componentTestCycleHashMap = new HashMap<>();

        try {
            JSONObject jsonObject = new JSONObject(ro_logs_json_string);

            if (jsonObject.getString("valid").equalsIgnoreCase("true")) {
                JSONArray jsonArray = jsonObject.getJSONArray("object");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonComponent = jsonArray.getJSONObject(i);
                    String component_name = jsonComponent.getString("component_name");
                    int component_test_cycle_value = Integer.parseInt(jsonComponent.getString("value").toString());
                    ComponentTestCycle componentTestCycle = componentTestCycleHashMap.get(component_name);
                    if (componentTestCycle == null) {
                        ComponentTestCycle new_componentTestCycle = new ComponentTestCycle();
                        new_componentTestCycle.max_test_cycle_value = component_test_cycle_value;
                        new_componentTestCycle.count_total_component_in_max_cycle = 1;
                        new_componentTestCycle.componentTestLogArrayList.add(new ComponentTestLog(jsonComponent.getString("id"), jsonComponent.getString("alias"), jsonComponent.getString("component_name"), jsonComponent.getString("slot"), jsonComponent.getString("value")));
                        componentTestCycleHashMap.put(component_name, new_componentTestCycle);
                    } else {
                        if (componentTestCycle.max_test_cycle_value == component_test_cycle_value) {
                            componentTestCycle.count_total_component_in_max_cycle++;

                        } else if (componentTestCycle.max_test_cycle_value < component_test_cycle_value) {
                            componentTestCycle.Count_total_component_in_not_max_cycle += componentTestCycle.count_total_component_in_max_cycle;
                            componentTestCycle.count_total_component_in_max_cycle = 1;
                            componentTestCycle.max_test_cycle_value = component_test_cycle_value;
                        } else {
                            componentTestCycle.Count_total_component_in_not_max_cycle++;
                        }
                        componentTestCycle.componentTestLogArrayList.add(new ComponentTestLog(jsonComponent.getString("id"), jsonComponent.getString("alias"), jsonComponent.getString("component_name"), jsonComponent.getString("slot"), jsonComponent.getString("value")));
                    }

                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return componentTestCycleHashMap;
    }
}
