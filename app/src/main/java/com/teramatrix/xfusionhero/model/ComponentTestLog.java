package com.teramatrix.xfusionhero.model;

import java.io.Serializable;

/**
 * Created by arun.singh on 11/2/2016.
 */
public class ComponentTestLog implements Serializable {

    public String id;
    public String alias;
    public String component_name;
    public String slot;
    public String value;

    public ComponentTestLog(String id,String alias,String component_name,String slot,String value)
    {
        this.id = id;
        this.alias = alias;
        this.component_name = component_name;
        this.slot = slot;
        this.value = value;
    }
}
