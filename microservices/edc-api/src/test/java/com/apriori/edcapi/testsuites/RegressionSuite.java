package com.apriori.edcapi.testsuites;

import com.apriori.edcapi.tests.BillOfMaterialsTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("262")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    BillOfMaterialsTest.class
})
public class RegressionSuite {
}
