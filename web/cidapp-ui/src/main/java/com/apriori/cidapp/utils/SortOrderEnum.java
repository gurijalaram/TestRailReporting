package com.apriori.cidapp.utils;

public enum SortOrderEnum {

    ASCENDING("sort-down"),
    DESCENDING("sort-up");

    private final String order;

    SortOrderEnum(String order) {
        this.order = order;
    }

    public String getOrder() {
        return this.order;
    }
}
