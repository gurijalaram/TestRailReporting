package com.apriori.cir.utils;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ItemTypeTwo {
    private String name;
    private String _jrid;
    private ArrayList<ItemTypeOne> properties;
    private ArrayList<ItemTypeThree> data;
}
