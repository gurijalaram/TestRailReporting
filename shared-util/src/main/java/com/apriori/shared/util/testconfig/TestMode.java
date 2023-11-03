package com.apriori.shared.util.testconfig;

public enum TestMode {
    LOCAL_GRID("LOCAL_GRID"), // test against env that QA uses like cid
    QA_LOCAL("QA_LOCAL"), // test locally, default
    EXPORT("EXPORT"), // run export tests, handy when running over RemoteWebDriver as download will happen on remote machine ;
    HOSTED_GRID("HOSTED_GRID"),
    GRID("GRID"),
    DOCKER_GRID("DOCKER_GRID");

    private final String value;

    TestMode(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
