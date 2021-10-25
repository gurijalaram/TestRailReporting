package com.apriori.cis.testsuites;

import com.apriori.cis.tests.LoginTests;
import com.apriori.utils.ProjectRunID;

import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("867")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    LoginTests.class
})
public class RegressionTestSuite {
}