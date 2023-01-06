package com.apriori.cis.testsuite;

import com.apriori.cis.tests.UserPreferencesTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("867")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    UserPreferencesTest.class
})
public class RegressionSuite {
}