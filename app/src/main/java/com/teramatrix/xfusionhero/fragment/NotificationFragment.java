package com.teramatrix.xfusionhero.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.teramatrix.xfusionhero.CustomDonutProgress;
import com.teramatrix.xfusionhero.R;
import com.teramatrix.xfusionhero.model.AlertModel;

import java.util.ArrayList;

/**
 * Created by arun.singh on 10/20/2016.
 */
public class NotificationFragment extends Fragment {
    private View view;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_alert, null);
            intViews();
        }
        return view;
    }

    private void intViews() {

        final ArrayList<AlertModel> alertModelArrayList = new ArrayList<AlertModel>();
        for (int i = 0; i < 10; i++)
            alertModelArrayList.add(new AlertModel("25 Oct 2016", "12:23 PM", "Daily Report for 35860-KTC-9200 of vendor_code ANU running at slot no. 1,2,3,4,5 is generated.",""));


        ListView listView = (ListView) view.findViewById(R.id.list_view_alert);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return alertModelArrayList.size();
            }

            @Override
            public Object getItem(int i) {
                return alertModelArrayList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup viewGroup) {

                ViewHolder viewHolder = null;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.alert_row_layout, null);
                    viewHolder.day = (TextView) convertView.findViewById(R.id.txt_day);
                    viewHolder.time = (TextView) convertView.findViewById(R.id.txt_time);
                    viewHolder.message = (TextView) convertView.findViewById(R.id.txt_device_alias);
                    viewHolder.txt_sub_messag = (TextView) convertView.findViewById(R.id.txt_sub_messag);
                    viewHolder.txt_sub_messag.setVisibility(View.INVISIBLE);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                viewHolder.day.setText(alertModelArrayList.get(position).day);
                viewHolder.time.setText(alertModelArrayList.get(position).time);
                viewHolder.message.setText(alertModelArrayList.get(position).message);

                return convertView;
            }

            class ViewHolder {
                TextView day;
                TextView time;
                TextView message;
                TextView txt_sub_messag;
            }

        });

    }
}
