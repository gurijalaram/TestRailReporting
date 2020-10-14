package com.apriori.utils.enums.reports;

public enum DateElementsEnum {
    HOUR("HH"),
    MINUTE("mm"),
    YEAR("yyyy"),
    MONTH("MM"),
    DAY("dd");

    private String dateElement;

    DateElementsEnum(String dateElement) {
        this.dateElement = dateElement;
    }

    public String getDateElement() {
        return this.dateElement;
    }
}
