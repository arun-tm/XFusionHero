package com.teramatrix.xfusionhero;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.teramatrix.xfusionhero.utils.GeneralUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by arun.singh on 11/4/2016.
 */
public class DateSelectionActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    private String selected_start_date;
    private String selected_end_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_selection);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        findViewById(R.id.lay_radio_btn_last_30_days).setOnClickListener(this);
        findViewById(R.id.lay_radio_btn_this_month).setOnClickListener(this);
        findViewById(R.id.lay_radio_btn_last_month).setOnClickListener(this);
        findViewById(R.id.lay_radio_btn_custom).setOnClickListener(this);
        findViewById(R.id.lay_start_date).setOnClickListener(this);
        findViewById(R.id.lay_end_date).setOnClickListener(this);

        findViewById(R.id.radio_btn_last_30_days).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_start_date = GeneralUtils.getLastThirtyDaysStartDate("yyyy-MM-dd");
                selected_end_date = GeneralUtils.getTodayDate("yyyy-MM-dd");
                setRadioButtonSelected(v.getId());

            }
        });
        findViewById(R.id.radio_btn_this_month).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_start_date = GeneralUtils.getCurrentMonthStartingDate("yyyy-MM-dd");
                selected_end_date = GeneralUtils.getCurrentMonthEndDate("yyyy-MM-dd");
                setRadioButtonSelected(v.getId());
            }
        });
        findViewById(R.id.radio_btn_last_month).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_start_date = GeneralUtils.getLastMonthStartingDate("yyyy-MM-dd");
                selected_end_date = GeneralUtils.getLastMonthEndDate("yyyy-MM-dd");
                setRadioButtonSelected(v.getId());
            }
        });
        findViewById(R.id.radio_btn_custom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_start_date = null;
                selected_end_date = null;
                setRadioButtonSelected(v.getId());
            }
        });

        //set date values
        ((TextView) findViewById(R.id.txt_last_thirty_day_range)).setText(GeneralUtils.getLastThirtyDaysStartDate("MMM dd") + " - " + GeneralUtils.getTodayDate("MMM dd"));
        ((TextView) findViewById(R.id.txt_this_month_name)).setText(GeneralUtils.getTodayDate("MMMM"));
        ((TextView) findViewById(R.id.txt_last_month_name)).setText(GeneralUtils.getLastMonthStartingDate("MMMM"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            menu.findItem(R.id.action_done).setVisible(true);
            menu.findItem(R.id.action_refresh).setVisible(false);
            super.onPrepareOptionsMenu(menu);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_done:

                if (selected_start_date == null) {
                    Toast.makeText(DateSelectionActivity.this, "Select any option", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (selected_end_date == null) {
                    Toast.makeText(DateSelectionActivity.this, "Select end date", Toast.LENGTH_SHORT).show();
                    return true;
                }
                Intent intent = new Intent();
                intent.putExtra("startDate", selected_start_date);
                intent.putExtra("endDate", selected_end_date);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.lay_radio_btn_last_30_days:
                selected_start_date = GeneralUtils.getLastThirtyDaysStartDate("yyyy-MM-dd");
                selected_end_date = GeneralUtils.getTodayDate("yyyy-MM-dd");
                setRadioButtonSelected(R.id.radio_btn_last_30_days);
                break;
            case R.id.lay_radio_btn_this_month:
                selected_start_date = GeneralUtils.getCurrentMonthStartingDate("yyyy-MM-dd");
                selected_end_date = GeneralUtils.getCurrentMonthEndDate("yyyy-MM-dd");
                setRadioButtonSelected(R.id.radio_btn_this_month);
                break;
            case R.id.lay_radio_btn_last_month:
                selected_start_date = GeneralUtils.getLastMonthStartingDate("yyyy-MM-dd");
                selected_end_date = GeneralUtils.getLastMonthEndDate("yyyy-MM-dd");
                setRadioButtonSelected(R.id.radio_btn_last_month);
                break;
            case R.id.lay_radio_btn_custom:
                selected_start_date = null;
                selected_end_date  =null;
                setRadioButtonSelected(R.id.radio_btn_custom);
                break;
            case R.id.lay_start_date:
                openPicker("start_date", ((TextView) findViewById(R.id.txt_start_date)).getText().toString());
                break;
            case R.id.lay_end_date:
                openPicker("end_date", ((TextView) findViewById(R.id.txt_end_date)).getText().toString());
                break;
        }
    }

    private void setRadioButtonSelected(int view_id) {
        ((com.rey.material.widget.RadioButton) findViewById(R.id.radio_btn_last_30_days)).setChecked(false);
        ((com.rey.material.widget.RadioButton) findViewById(R.id.radio_btn_this_month)).setChecked(false);
        ((com.rey.material.widget.RadioButton) findViewById(R.id.radio_btn_last_month)).setChecked(false);
        ((com.rey.material.widget.RadioButton) findViewById(R.id.radio_btn_custom)).setChecked(false);

        ((com.rey.material.widget.RadioButton) findViewById(view_id)).setChecked(true);


        System.out.println("Start day: " + selected_start_date + " End Date: " + selected_end_date);

    }

    private void openPicker(String tag, String current) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat formatWithoutTime = new SimpleDateFormat("dd MMM yyyy");
        try {
            if (current != null) {
                try {
                    Date d = formatWithoutTime.parse(current);
                    c.setTime(d);
                } catch (ParseException ee) {
                    ee.printStackTrace();
                }
            }
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

            //Set Max and Min date for both dates (start date and end date)
            datePickerDialog.setMaxDate(Calendar.getInstance());
            if (tag.equalsIgnoreCase("start_date")) {
                Calendar max_d = Calendar.getInstance();
                try {
                    max_d.setTime(formatWithoutTime.parse(((TextView) findViewById(R.id.txt_end_date)).getText().toString()));
                    datePickerDialog.setMaxDate(max_d);
                } catch (Exception e) {
                    datePickerDialog.setMaxDate(max_d);
                }

            } else if (tag.equalsIgnoreCase("end_date")) {
                Calendar max_d = Calendar.getInstance();
                max_d.setTime(formatWithoutTime.parse(((TextView) findViewById(R.id.txt_start_date)).getText().toString()));
                datePickerDialog.setMinDate(max_d);
                datePickerDialog.setMaxDate(Calendar.getInstance());
            }

            datePickerDialog.show(getFragmentManager(), tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String pattern = dayOfMonth + " " + GeneralUtils.getMonth(monthOfYear) + " " + year;
        if (view.getTag().equalsIgnoreCase("start_date")) {
            ((TextView) findViewById(R.id.txt_start_date)).setText(pattern);
            selected_start_date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
            selected_end_date = null;
        } else if (view.getTag().equalsIgnoreCase("end_date")) {
            ((TextView) findViewById(R.id.txt_end_date)).setText(pattern);
            selected_end_date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
        }
        setRadioButtonSelected(R.id.radio_btn_custom);
    }
}
