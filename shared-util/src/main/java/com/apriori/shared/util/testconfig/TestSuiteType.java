package com.apriori.shared.util.testconfig;

public enum TestSuiteType {
    SMOKE("SMOKE"),
    REGRESSION("REGRESSION"),
    EXTENDED_REGRESSION("EXTENDED_REGRESSION"),
    SANITY("SANITY"),
    API_SANITY("API_SANITY"),
    CUSTOMER("CUSTOMER"),
    SYSTEM_CONFIGURATION("SYSTEM_CONFIGURATION"),
    USERS("USERS"),
    IGNORE("IGNORE"),
    REPORTS("REPORTS"),
    JASPER_API("JASPER"),
    ADMIN("ADMIN"),
    FULL_ON_PREM("FULL_ON_PREM"),
    DELETE("DELETE");

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
        public static final String API_SANITY = "API_SANITY";
        public static final String CUSTOMER = "CUSTOMER";
        public static final String NON_CUSTOMER = "NON_CUSTOMER";
        public static final String SYSTEM_CONFIGURATION = "SYSTEM_CONFIGURATION";
        public static final String USERS = "USERS";
        public static final String IGNORE = "IGNORE";
        public static final String REPORTS = "REPORTS";
        public static final String JASPER_API = "JASPER_API";
        public static final String ADMIN = "ADMIN";
        public static final String FULL_ON_PREM = "FULL_ON_PREM";
        public static final String DELETE = "DELETE";
    }
}
