package com.apriori.testrail;

public enum TestRailStatus {
    PASSED(1),
    BLOCKED(2),
    UNTESTED(3),
    RETEST(4),
    FAILED(5),
    NOT_APPLICABLE(6),
    DISABLED(7);

    private int id;

    TestRailStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
