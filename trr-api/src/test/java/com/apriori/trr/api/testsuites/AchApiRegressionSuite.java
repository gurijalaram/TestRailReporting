package com.apriori.trr.api.testsuites;

import com.apriori.trr.api.tests.AchCustomersTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@SelectClasses({
    AchCustomersTests.class,
})
public class AchApiRegressionSuite {
}