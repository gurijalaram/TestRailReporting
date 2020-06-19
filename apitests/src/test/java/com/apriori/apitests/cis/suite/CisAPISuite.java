package com.apriori.apitests.cis.suite;

import com.apriori.apitests.cis.CisBatchPartResources;
import com.apriori.apitests.cis.CisBatchResources;
import com.apriori.apitests.cis.CisCostingScenario;
import com.apriori.apitests.cis.CisCustomerResources;
import com.apriori.apitests.cis.CisPartResources;
import com.apriori.apitests.cis.CisReportResources;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("407")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        CisCostingScenario.class,
        CisCustomerResources.class,
        CisPartResources.class,
        CisReportResources.class,
        CisBatchResources.class,
        CisBatchPartResources.class
})
public class CisAPISuite {
}
