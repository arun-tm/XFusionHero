package com.teramatrix.xfusionhero.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by arun.singh on 11/2/2016.
 */
public class ComponentTestCycle implements Serializable{

    public int max_test_cycle_value;
    public int count_total_component_in_max_cycle;
    public int Count_total_component_in_not_max_cycle;
    public ArrayList<ComponentTestLog> componentTestLogArrayList = new ArrayList<>();
}
