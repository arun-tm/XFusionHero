package com.teramatrix.xfusionhero.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.teramatrix.xfusionhero.MainActivity;
import com.teramatrix.xfusionhero.R;
import com.teramatrix.xfusionhero.controller.RESTClient;
import com.teramatrix.xfusionhero.model.DateChagneMessageEvent;
import com.teramatrix.xfusionhero.utils.AppUtils;
import com.teramatrix.xfusionhero.utils.GeneralUtils;
import com.teramatrix.xfusionhero.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by arun.singh on 10/20/2016.
 */
public class AlertAndNotificationFragmnet extends Fragment {

    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private okhttp3.Call call;
    private AlertDialog dialog;

    ArrayList<Alert> alertArrayList = new ArrayList<Alert>();
    ArrayList<Alert> notificationArrayList = new ArrayList<Alert>();
    private AlertFragment alertFragment;
    private AlertFragment notificationFragment;


    private String startDate;
    private String endDate;

    public class Alert {
        String messsage;
        String code;
        String code_id;
        String created_on;
        String action_taken;

        public Alert(String messsage, String code, String code_id, String created_on, String action_taken) {
            this.messsage = messsage;
            this.code = code;
            this.code_id = code_id;
            this.created_on = created_on;
            this.action_taken = action_taken;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_alert_notification, null);
            initViews();
        }
        AppUtils.setAppBarSubTitle_default(getActivity());
        AppUtils.enableClickEventOnToolbar(getActivity());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Alert And Notification");
    }

    @Override
    public void onStop() {
//        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //     This method will be called when a DateChagneMessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDateChagneMessageEvent(DateChagneMessageEvent dateChagneMessageEvent) {
        if (GeneralUtils.isConnected(getActivity(), R.layout.pop_up_dialog)) {
            startDate = dateChagneMessageEvent.startDate;
            endDate = dateChagneMessageEvent.endDate;
            ((MainActivity)getActivity()).setAppBarSubTitile(GeneralUtils.format_DateString_From_One_Pattern_To_Another_Pattern(startDate,"yyy-MM-dd","MMM dd")+" - "+GeneralUtils.format_DateString_From_One_Pattern_To_Another_Pattern(endDate,"yyy-MM-dd","MMM dd"));
            getAllAlerts();
        }
    }

    private void initViews() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        dialog = new SpotsDialog(getActivity(), R.style.Custom);

        //Get Or Set api start and end dates
        SPUtils spUtils = new SPUtils(getActivity());
        startDate =  spUtils.getString(SPUtils.API_PARAM_START_DATE);
        endDate =  spUtils.getString(SPUtils.API_PARAM_END_DATE);

        if (GeneralUtils.isConnected(getActivity(), R.layout.pop_up_dialog))
            getAllAlerts();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh: {

                try {
                    if (GeneralUtils.isConnected(getActivity(), R.layout.pop_up_dialog)) {
                        getAllAlerts();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        alertFragment = new AlertFragment();
        Bundle bundle_alert = new Bundle();
        bundle_alert.putString("item_type", "alert");
        alertFragment.setArguments(bundle_alert);
        adapter.addFrag(alertFragment, "All Alert");

        notificationFragment = new AlertFragment();
        Bundle bundle_notification = new Bundle();
        bundle_notification.putString("item_type", "notification");
        notificationFragment.setArguments(bundle_notification);
        adapter.addFrag(notificationFragment, "All Notification");

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

    private void getAllAlerts() {
        dialog.show();
        String body = "token=" + new SPUtils(getActivity()).getToken() +
                "&userKey=" + new SPUtils(getActivity()).getUserKey() +
                "&user_id=" + new SPUtils(getActivity()).getUserID() +
                "&from_date=" + startDate+" 00:00:00" +
                "&to_date=" + endDate+" 23:59:59" +
                "&filter_type=" + "all_alert";

        call = RESTClient.getAllAlerts(getActivity(), body, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                System.out.println("onFailure");
                dialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        alertArrayList.clear();
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.has("valid")) {
                            String valid = jsonObject.getString("valid");
                            if (valid.equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("object");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String messsage = jsonObject1.getString("alert");
                                    String created_on = jsonObject1.getString("created_on");
                                    String last_notification_action = "NO ACTION";
                                    if (jsonObject1.has("last_notification_action"))
                                        last_notification_action = jsonObject1.getString("last_notification_action");
                                    alertArrayList.add(new Alert(messsage, null, null, created_on, last_notification_action));
                                }
                            } else {
                            }
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
//                        dialog.dismiss();
                        getAllNotifications();
                    }
                } else {
                    dialog.dismiss();
                }
            }
        });
    }

    private void getAllNotifications() {
//        dialog.show();
        String body = "token=" + new SPUtils(getActivity()).getToken() +
                "&userKey=" + new SPUtils(getActivity()).getUserKey() +
                "&user_id=" + new SPUtils(getActivity()).getUserID() +
                "&from_date=" + startDate +" 00:00:00" +
                "&to_date=" + endDate+" 23:59:59" +
                "&filter_type=" + "all_notification";

        call = RESTClient.getAllNotifications(getActivity(), body, new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                System.out.println("onFailure");
                dialog.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        notificationArrayList.clear();
                        String res = response.body().string();
                        JSONObject jsonObject = new JSONObject(res);
                        if (jsonObject.has("valid")) {
                            String valid = jsonObject.getString("valid");
                            if (valid.equalsIgnoreCase("true")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("object");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    String messsage = jsonObject1.getString("notification");
                                    String code = jsonObject1.getString("notification_code_code");
                                    String code_id = "";
                                    String created_on = jsonObject1.getString("created_on");

                                    notificationArrayList.add(new Alert(messsage, code, code_id, created_on, null));
                                }

                            } else {
                            }
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {
                        dialog.dismiss();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alertFragment.reciveAlertData(alertArrayList, true);
                                notificationFragment.reciveAlertData(notificationArrayList, false);
                            }
                        });

                    }
                } else {
                    dialog.dismiss();
                }
            }
        });
    }

}
