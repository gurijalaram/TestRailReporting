package com.apriori.cir.utils;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ReportConfigItem {
    private String id;
    private String type;
    private ArrayList<Object> reportConfig;
}
