package com.apriori.bcs.tests.suite;

import com.apriori.bcs.tests.BatchPartResourcesTest;
import com.apriori.bcs.tests.BatchResourcesTest;
import com.apriori.bcs.tests.CostingScenarioTest;
import com.apriori.bcs.tests.CustomerResourcesTest;
import com.apriori.bcs.tests.MultiPartCostingScenarioTest;
import com.apriori.bcs.tests.ReportResourcesTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("407")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        CustomerResourcesTest.class,
        ReportResourcesTest.class,
        BatchResourcesTest.class,
        BatchPartResourcesTest.class,
        CostingScenarioTest.class,
        MultiPartCostingScenarioTest.class
})
public class APISuite {
}
