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

import com.teramatrix.xfusionhero.R;
import com.teramatrix.xfusionhero.model.ComponentTestCycle;
import com.teramatrix.xfusionhero.model.ComponentTestLog;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by arun.singh on 11/2/2016.
 */
public class ComponentROLogs extends Fragment {

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
            ComponentTestCycle componentTestCycle = (ComponentTestCycle) getArguments().getSerializable("logs");
            intViews(componentTestCycle);
        }
        return view;
    }

    private void intViews(ComponentTestCycle componentTestCycle) {

        if (componentTestCycle != null) {
            final ArrayList<ComponentTestLog> componentTestLogs = componentTestCycle.componentTestLogArrayList;

            ListView listView = (ListView) view.findViewById(R.id.list_view_alert);
            if (componentTestLogs != null && componentTestLogs.size() > 0) {
                listView.setVisibility(View.VISIBLE);
                view.findViewById(R.id.txt_empty_state).setVisibility(View.GONE);
                listView.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return componentTestLogs.size();
                    }

                    @Override
                    public Object getItem(int i) {
                        return componentTestLogs.get(i);
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
                            convertView = LayoutInflater.from(getActivity()).inflate(R.layout.ro_logs_row, null);
                            viewHolder.alia = (TextView) convertView.findViewById(R.id.txt_alias);
                            viewHolder.value = (TextView) convertView.findViewById(R.id.txt_value);
                            viewHolder.lay_header = convertView.findViewById(R.id.lay_header);
                            convertView.setTag(viewHolder);
                        } else {
                            viewHolder = (ViewHolder) convertView.getTag();
                        }

                        viewHolder.lay_header.setVisibility((position == 0) ? View.VISIBLE : View.GONE);
                        viewHolder.alia.setText(componentTestLogs.get(position).alias);
                        viewHolder.value.setText(NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(componentTestLogs.get(position).value))+"");

                        return convertView;
                    }

                    class ViewHolder {
                        TextView alia;
                        TextView value;
                        View lay_header;
                    }

                });
            } else {
                listView.setVisibility(View.GONE);
                view.findViewById(R.id.txt_empty_state).setVisibility(View.VISIBLE);
            }
        }


    }
}
