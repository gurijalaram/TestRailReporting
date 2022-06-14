package com.apriori.bcs.tests.suite;

import com.apriori.bcs.tests.BatchPartNegativeTest;
import com.apriori.bcs.tests.BatchPartTest;
import com.apriori.bcs.tests.BatchResourcesTest;
import com.apriori.bcs.tests.CostingScenarioTest;
import com.apriori.bcs.tests.CustomerNegativeTests;
import com.apriori.bcs.tests.CustomerResourcesTest;
import com.apriori.bcs.tests.ReportResourcesTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("407")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    BatchResourcesTest.class,
    BatchPartTest.class,
    CustomerResourcesTest.class,
    ReportResourcesTest.class,
    CostingScenarioTest.class,
    BatchPartNegativeTest.class,
    CustomerNegativeTests.class
})
public class APISuite {
}
