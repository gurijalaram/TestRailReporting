package com.apriori;

public enum TestSuiteType {
    SMOKE("SMOKE"),
    REGRESSION("REGRESSION"),
    EXTENDED_REGRESSION("EXTENDED_REGRESSION"),
    SANITY("SANITY"),
    CUSTOMER("CUSTOMER"),
    SYSTEM_CONFIGURATION("SYSTEM_CONFIGURATION"),
    USERS("USERS"),
    IGNORE("IGNORE");

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
        public static final String SANITY = "SANITY";
        public static final String CUSTOMER = "CUSTOMER";
        public static final String SYSTEM_CONFIGURATION = "SYSTEM_CONFIGURATION";
        public static final String USERS = "USERS";
        public static final String IGNORE = "IGNORE";
    }
}
