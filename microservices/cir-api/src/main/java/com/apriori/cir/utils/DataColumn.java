package com.apriori.cir.utils;

import lombok.Data;

import java.util.ArrayList;

@Data
public class DataColumn {
    private int sortMeasureIndex;
    private Object order;
    private ArrayList<ColumnValue> columnValues;
}
