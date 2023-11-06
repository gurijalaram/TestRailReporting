package com.apriori.shared.util.testconfig;

/**
 * @author cfrith
 */

public enum Browser {

    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge"),
    IEXPLORER("iexplorer");

    private final String value;

    Browser(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
