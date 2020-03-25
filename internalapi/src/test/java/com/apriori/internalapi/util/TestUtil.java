package com.apriori.internalapi.util;

import static org.junit.Assert.assertEquals;

public class TestUtil {

    protected void validateResponseCodeByExpectingAndRealCode(int expectedStatus, int realStatusCode) {
        assertEquals("The response code should be as expected ",  expectedStatus, realStatusCode);
    }
}
