package com.apriori.cir.api.utils;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ItemTypeTwo {
    private String name;
    @SuppressWarnings("checkstyle:MemberName")
    private String _jrid;
    private ArrayList<ItemTypeOne> properties;
    private ArrayList<ItemTypeThree> data;
}
