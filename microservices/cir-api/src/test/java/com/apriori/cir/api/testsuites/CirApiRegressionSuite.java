package com.apriori.cir.api.testsuites;

import com.apriori.cir.api.tests.JasperReportTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    JasperReportTest.class
})
public class CirApiRegressionSuite {
}