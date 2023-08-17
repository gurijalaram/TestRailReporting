package com.apriori.testrail;

public enum TestRailStatus {
    PASSED(1),
    BLOCKED(2),
    DISABLED(3),
    RETEST(4),
    FAILED(5);

    private int id;

    TestRailStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
