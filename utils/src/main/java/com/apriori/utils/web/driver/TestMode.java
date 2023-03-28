package com.apriori.utils.web.driver;

public enum TestMode {
    LOCAL_DOCKER("QA"), // test against env that QA uses like cid-te
    LOCAL("LOCAL"), // test locally, default
    EXPORT("EXPORT"), // run export tests, handy when running over RemoteWebDriver as download will happen on remote machine ;
    SELENIUM_GRID("GRID"),
    HOSTED_DOCKER("DOCKER");

    private final String value;

    TestMode(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
