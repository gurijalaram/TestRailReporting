package com.apriori.cir.utils;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Item2 {
    private String name;
    private String _jrid;
    private ArrayList<Item> properties;
    private ArrayList<Item4> data;
}
