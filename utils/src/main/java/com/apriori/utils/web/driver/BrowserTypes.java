package com.apriori.utils.web.driver;

public enum  BrowserTypes {

    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge"),
    IEXPLORER("iexplorer");

    private final String value;

    BrowserTypes(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
