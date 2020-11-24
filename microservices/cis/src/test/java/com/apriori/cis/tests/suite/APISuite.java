package com.apriori.cis.tests.suite;

import com.apriori.cis.tests.BatchPartResourcesTest;
import com.apriori.cis.tests.BatchResourcesTest;
import com.apriori.cis.tests.CostingScenarioTest;
import com.apriori.cis.tests.CustomerResourcesTest;
import com.apriori.cis.tests.MultiPartCostingScenarioTest;
import com.apriori.cis.tests.PartResourcesTest;
import com.apriori.cis.tests.ReportResourcesTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("407")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        CostingScenarioTest.class,
        CustomerResourcesTest.class,
        PartResourcesTest.class,
        ReportResourcesTest.class,
        BatchResourcesTest.class,
        BatchPartResourcesTest.class
})
public class APISuite {
}
