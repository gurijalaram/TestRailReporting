package com.integration.testsuites;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    ApiSanitySuite.class,
    CustomerSmokeEnvironmentSuite.class,
    CustomerTagSuite.class
})
public class CustomerGlobalSuite {
}
