package com.apriori.cis.ui.utils;

public enum CisSortOrderEnum {

    DESCENDING("sort-down"),
    ASCENDING("sort-up");

    private final String order;

    CisSortOrderEnum(String order) {
        this.order = order;
    }

    public String getOrder() {
        return this.order;
    }
}
