package com.apriori.apibase.utils;

import static org.junit.Assert.assertEquals;

import com.apriori.utils.TestHelper;

public class TestUtil extends TestHelper {

    protected static void validateResponseCodeByExpectingAndRealCode(int expectedStatus, int realStatusCode) {
        assertEquals("The response code should be as expected ",  expectedStatus, realStatusCode);
    }
}
