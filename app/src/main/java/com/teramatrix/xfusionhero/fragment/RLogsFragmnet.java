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
import android.view.View;
import android.view.ViewGroup;

import com.teramatrix.xfusionhero.R;
import com.teramatrix.xfusionhero.controller.RESTClient;
import com.teramatrix.xfusionhero.model.ComponentTestCycle;
import com.teramatrix.xfusionhero.utils.GeneralUtils;
import com.teramatrix.xfusionhero.utils.SPUtils;

import org.json.JSONArray;
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
public class RLogsFragmnet extends Fragment {

    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private HashMap<String, ComponentTestCycle> componentTestCycleHashMap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_alert_notification, null);
            componentTestCycleHashMap = (HashMap<String, ComponentTestCycle>)getArguments().getSerializable("rl_logs");
            initViews();
        }
        return view;
    }

    private void initViews() {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Ro Logs Status");

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

         getActivity().findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

             }
         });

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        ComponentROLogs componentROLogs_side_stand = new ComponentROLogs();
        Bundle bundle = new Bundle();
        bundle.putSerializable("logs",componentTestCycleHashMap.get("SIDE STAND SWITCH"));
        componentROLogs_side_stand.setArguments(bundle);

        ComponentROLogs componentROLogs_keyset = new ComponentROLogs();
        Bundle bundle2 = new Bundle();

        bundle2.putSerializable("logs",componentTestCycleHashMap.get("KEYSET")==null?componentTestCycleHashMap.get("LOCK"):componentTestCycleHashMap.get("KEYSET"));
        componentROLogs_keyset.setArguments(bundle2);

        ComponentROLogs componentROLogs_relay = new ComponentROLogs();
        Bundle bundle3 = new Bundle();
        bundle3.putSerializable("logs",componentTestCycleHashMap.get("RELAY"));
        componentROLogs_relay.setArguments(bundle3);


        ComponentROLogs componentROLogs_horn = new ComponentROLogs();
        Bundle bundle4 = new Bundle();
        bundle4.putSerializable("logs",componentTestCycleHashMap.get("HORN"));
        componentROLogs_horn.setArguments(bundle4);

        adapter.addFrag(componentROLogs_side_stand, "SIDE STAND");
        adapter.addFrag(componentROLogs_keyset, "KEYSET");
        adapter.addFrag(componentROLogs_relay, "RELAY");
        adapter.addFrag(componentROLogs_horn, "HORN");
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

}
