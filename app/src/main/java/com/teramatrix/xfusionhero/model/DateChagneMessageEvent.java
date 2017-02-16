package com.teramatrix.xfusionhero.model;

/**
 * Created by arun.singh on 11/5/2016.
 */
public class DateChagneMessageEvent {

    public final String startDate;
    public final String endDate;

    public DateChagneMessageEvent(String startDate,String endDate)
    {
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
