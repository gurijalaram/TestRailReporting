package com.apriori.cirapi.entity.enums;

import java.util.HashMap;
import java.util.Map;

public enum ReportChartType {
    BUBBLE_SCATTER("Scatter"),
    STACKED_BAR("StackedBar");

    private static final Map<String, ReportChartType> lookup = new HashMap<>();
    private final String chartType;

    static {
        for (ReportChartType d : ReportChartType.values()) {
            lookup.put(d.getChartType(), d);
        }
    }

    ReportChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getChartType() {
        return chartType;
    }

    public static ReportChartType get(String abbreviation) {
        return lookup.get(abbreviation);
    }
}
