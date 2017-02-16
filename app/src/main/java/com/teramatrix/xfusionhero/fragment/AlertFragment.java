package com.teramatrix.xfusionhero.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.teramatrix.xfusionhero.CustomDonutProgress;
import com.teramatrix.xfusionhero.R;
import com.teramatrix.xfusionhero.model.AlertModel;
import com.teramatrix.xfusionhero.utils.AppUtils;
import com.teramatrix.xfusionhero.utils.GeneralUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arun.singh on 10/20/2016.
 */
public class AlertFragment extends Fragment {

    private View view;
    private boolean is_action_taken_visible;
    private String item_type = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        item_type = getArguments().getString("item_type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_alert, null);
//            intViews();
        }
        return view;
    }

    public void reciveAlertData(final ArrayList<AlertAndNotificationFragmnet.Alert> alertArrayList, boolean is_action_taken_visible) {

        if (alertArrayList.size() > 0) {
            view.findViewById(R.id.txt_empty_state).setVisibility(View.GONE);
            view.findViewById(R.id.list_view_alert).setVisibility(View.VISIBLE);
            initlist(alertArrayList);
        } else {
            view.findViewById(R.id.list_view_alert).setVisibility(View.GONE);
            view.findViewById(R.id.txt_empty_state).setVisibility(View.VISIBLE);
        }
        this.is_action_taken_visible = is_action_taken_visible;
    }

    private void initlist(final ArrayList<AlertAndNotificationFragmnet.Alert> alertArrayList) {
        ListView listView = (ListView) view.findViewById(R.id.list_view_alert);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return alertArrayList.size();
            }

            @Override
            public Object getItem(int i) {
                return alertArrayList.get(i);
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
                    viewHolder.top_line = convertView.findViewById(R.id.view_top_line);
                    viewHolder.bottom_line = convertView.findViewById(R.id.view_bottom_line);
                    viewHolder.txt_sub_messag.setVisibility(View.VISIBLE);
                    viewHolder.icon = ((ImageView) convertView.findViewById(R.id.img_severity));
                    viewHolder.lay_action_taken = convertView.findViewById(R.id.lay_action_taken);
                    viewHolder.img_tool = (ImageView)convertView.findViewById(R.id.img_tool);

                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }


//                viewHolder.top_line.setVisibility(position==0?View.INVISIBLE:View.VISIBLE);
                viewHolder.bottom_line.setVisibility(position == alertArrayList.size() - 1 ? View.INVISIBLE : View.VISIBLE);

                String date = GeneralUtils.format_DateString_From_One_Pattern_To_Another_Pattern(alertArrayList.get(position).created_on, "MMM dd, yyyy hh:mm:ss a", "dd MMM yyyy");
                String time = GeneralUtils.format_DateString_From_One_Pattern_To_Another_Pattern(alertArrayList.get(position).created_on, "MMM dd, yyyy hh:mm:ss a", "hh:mm a");
                viewHolder.day.setText(date);
                viewHolder.time.setText(time);
                viewHolder.message.setText(alertArrayList.get(position).messsage.trim());

                viewHolder.lay_action_taken.setVisibility(is_action_taken_visible ? View.VISIBLE : View.INVISIBLE);
                viewHolder.txt_sub_messag.setText("" + alertArrayList.get(position).action_taken);

                int icon_imag_id = 0;
                if (item_type.equalsIgnoreCase("alert")) {
                    viewHolder.txt_sub_messag.setText("" + alertArrayList.get(position).action_taken.toUpperCase());
                    if (alertArrayList.get(position).action_taken.equalsIgnoreCase("Ignore")) {
                        viewHolder.txt_sub_messag.setTextColor(getActivity().getResources().getColor(R.color.color_holo_green_dark));
                    } else if (alertArrayList.get(position).action_taken.equalsIgnoreCase("unplanned")) {
                        viewHolder.txt_sub_messag.setTextColor(getActivity().getResources().getColor(R.color.color_holo_red_dark));
                    } else {
                        viewHolder.txt_sub_messag.setTextColor(getActivity().getResources().getColor(R.color.color_nam_menu_light_gray));
                    }
                    icon_imag_id = AppUtils.getIconForAlert(null);
                } else if (item_type.equalsIgnoreCase("notification")) {
                    icon_imag_id = AppUtils.getIconForNotification((int) (Float.parseFloat(alertArrayList.get(position).code)));
                }
                viewHolder.icon.setImageResource(icon_imag_id);

                return convertView;
            }

            class ViewHolder {
                TextView day;
                TextView time;
                TextView message;
                TextView txt_sub_messag;
                View top_line;
                View bottom_line;
                ImageView icon;
                View lay_action_taken;
                ImageView img_tool;
            }

        });

    }
}
