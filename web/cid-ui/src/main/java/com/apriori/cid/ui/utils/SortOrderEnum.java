package com.apriori.cid.ui.utils;

public enum SortOrderEnum {

    DESCENDING("sort-down"),
    ASCENDING("sort-up");

    private final String order;

    SortOrderEnum(String order) {
        this.order = order;
    }

    public String getOrder() {
        return this.order;
    }
}
