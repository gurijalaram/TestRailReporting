package com.apriori.cir.utils;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Properties {
    private ArrayList<Services> services;
    private String renderto;
    private int width;
    private int height;
    private String prop;
    private String val;
}
