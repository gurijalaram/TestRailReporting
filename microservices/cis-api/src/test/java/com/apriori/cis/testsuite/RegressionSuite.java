package com.apriori.cis.testsuite;

import com.apriori.cis.tests.CisBidPackageItemTest;
import com.apriori.cis.tests.CisBidPackageProjectsTest;
import com.apriori.cis.tests.CisBidPackageTest;
import com.apriori.cis.tests.UserPreferencesTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("867")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    UserPreferencesTest.class,
    CisBidPackageTest.class,
    CisBidPackageItemTest.class,
    CisBidPackageProjectsTest.class
})
public class RegressionSuite {
}