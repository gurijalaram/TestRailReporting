package com.apriori.cir.utils;

import java.util.ArrayList;

import lombok.Data;

@Data
public class ChartInfoItem {
    private String id;
    private String chartUuid;
    private String type;
    private String module;
    private String uimodule;
    private GlobalOptionsItem globalOptions;
    private HcInstanceDataItem hcinstancedata;
    private boolean interactive;
    private String charttype;
    private boolean datetimeSupported;
    private boolean treemapSupported;
    private boolean gaugesSupported;
    private boolean detailChartEnabled;
    private String fragmentId;
    private String crosstabId;
    private int startColumnIndex;
    private boolean hasFloatingHeaders;
    private ArrayList<RowGroup> rowGroups;
    private ArrayList<DataColumn> dataColumns;
}
