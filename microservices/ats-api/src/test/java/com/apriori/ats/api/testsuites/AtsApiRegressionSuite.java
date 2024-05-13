package com.apriori.ats.api.testsuites;

import com.apriori.ats.api.tests.AtsAuthenticationTests;
import com.apriori.ats.api.tests.AtsAuthorizationTests;
import com.apriori.ats.api.tests.AtsUsersTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    AtsAuthorizationTests.class,
    AtsAuthenticationTests.class,
    AtsUsersTests.class
})
public class AtsApiRegressionSuite {
}
