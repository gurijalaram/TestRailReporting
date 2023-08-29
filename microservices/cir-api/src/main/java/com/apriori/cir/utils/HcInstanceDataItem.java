package com.apriori.cir.utils;

import java.util.ArrayList;

import lombok.Data;

@Data
public class HcInstanceDataItem {
    private ArrayList<Properties> properties;
    private ArrayList<Services> services;
    private String renderto;
    private int width;
    private int height;
}
