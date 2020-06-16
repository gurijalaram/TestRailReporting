package com.apriori.cis.tests.suite;

import com.apriori.cis.tests.CisBatchPartResources;
import com.apriori.cis.tests.CisBatchResources;
import com.apriori.cis.tests.CisCustomerResources;
import com.apriori.cis.tests.CisPartResources;
import com.apriori.cis.tests.CisReportResources;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("407")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        CisCustomerResources.class,
        CisPartResources.class,
        CisReportResources.class,
        CisBatchResources.class,
        CisBatchPartResources.class,
//        CisCostingScenario.class
})
public class CisAPISuite {
}
