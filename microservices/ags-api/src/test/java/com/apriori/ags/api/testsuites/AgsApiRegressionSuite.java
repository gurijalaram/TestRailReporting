package com.apriori.ags.api.testsuites;

import com.apriori.ags.api.tests.AgsBcmTests;
import com.apriori.ags.api.tests.AgsCssTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    AgsBcmTests.class,
    AgsCssTests.class
})
public class AgsApiRegressionSuite {
}