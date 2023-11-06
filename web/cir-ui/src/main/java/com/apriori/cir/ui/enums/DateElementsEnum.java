package com.apriori.cir.ui.enums;

public enum DateElementsEnum {
    HOUR("HH"),
    MINUTE("mm"),
    YEAR("yyyy"),
    MONTH("MM"),
    DAY("dd");

    private final String dateElement;

    DateElementsEnum(String dateElement) {
        this.dateElement = dateElement;
    }

    /**
     * Gets date element as string
     * @return String
     */
    public String getDateElement() {
        return this.dateElement;
    }
}