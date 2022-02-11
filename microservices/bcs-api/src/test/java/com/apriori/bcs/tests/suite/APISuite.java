package com.apriori.bcs.tests.suite;

import com.apriori.bcs.tests.BatchPartTest;
import com.apriori.bcs.tests.BatchResourcesTest;
import com.apriori.bcs.tests.CostingScenarioTest;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("407")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    // CustomerResourcesTest.class,
    // ReportResourcesTest.class,
    // BatchResourcesTest.class,
    // BatchPartResourcesTest.class,
    // CostingScenarioTest.class
    BatchResourcesTest.class,
    BatchPartTest.class,
    CostingScenarioTest.class
})
public class APISuite {
}
