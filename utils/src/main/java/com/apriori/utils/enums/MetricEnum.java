package com.apriori.utils.enums;

public enum MetricEnum {

    ENGLISH("English"),
    METRIC("Metric");

    private final String unit;

    MetricEnum(String unit) {
        this.unit = unit;
    }

    public String getMetricUnit() {
        return this.unit;
    }
}
