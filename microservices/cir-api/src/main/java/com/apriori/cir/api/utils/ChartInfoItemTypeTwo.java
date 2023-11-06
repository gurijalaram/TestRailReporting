package com.apriori.cir.api.utils;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ChartInfoItemTypeTwo {
    private String type;
    private String module;
    private String uimodule;
    private String id;
    private String fragmentId;
    private String crosstabId;
    private int startColumnIndex;
    private boolean hasFloatingHeaders;
    private ArrayList<RowGroup> rowGroups;
    private ArrayList<DataColumn> dataColumns;
}
