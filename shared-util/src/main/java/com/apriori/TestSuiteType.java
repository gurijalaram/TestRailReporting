package com.apriori;

public enum TestSuiteType {
    SMOKE("SMOKE"),
    REGRESSION("REGRESSION"),
    EXTENDED_REGRESSION("EXTENDED_REGRESSION");

    private final String value;

    TestSuiteType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static class TestSuite {
        public static final String SMOKE = "SMOKE";
        public static final String REGRESSION = "REGRESSION";
        public static final String EXTENDED_REGRESSION = "EXTENDED_REGRESSION";
    }
}
