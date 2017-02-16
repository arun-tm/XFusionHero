package com.teramatrix.xfusionhero.model;

/**
 * Created by arun.singh on 10/25/2016.
 */
public class AlertModel {

    public String day;
    public String time;
    public String message;
    public String sub_message;
    public AlertModel(String time,String day,String message,String sub_message)
    {
        this.time = time;
        this.day =day;
        this.message = message;
        this.sub_message= sub_message;
    }
}
