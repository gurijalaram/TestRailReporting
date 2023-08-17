package com.apriori.enums;

public enum TimeEnum {

    MILLISECOND("ms"),
    SECOND("s"),
    MINUTE("min"),
    HOUR("hr");

    private final String time;

    TimeEnum(String time) {
        this.time = time;
    }

    public String getTime() {
        return this.time;
    }
}
