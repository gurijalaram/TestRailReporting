package com.apriori.cid.ui.utils;

public enum TimeEnum {

    MILLISECOND("Millisecond"),
    SECOND("Second"),
    MINUTE("Minute"),
    HOUR("Hour");

    private final String time;

    TimeEnum(String time) {
        this.time = time;
    }

    public String getTime() {
        return this.time;
    }
}
