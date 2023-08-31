package com.apriori.cir.utils;

import lombok.Data;

import java.util.ArrayList;

@Data
public class HcInstanceDataItem {
    private ArrayList<Properties> properties;
    private ArrayList<Services> services;
    private String renderto;
    private int width;
    private int height;
}
