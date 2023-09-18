package testsuites;

import com.apriori.BatchPartNegativeTest;
import com.apriori.BatchPartTest;
import com.apriori.BatchResourcesTest;
import com.apriori.CostingScenarioTest;
import com.apriori.CustomerNegativeTests;
import com.apriori.CustomerResourcesTest;
import com.apriori.ReportResourcesTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@Suite
@org.junit.runners.Suite.SuiteClasses({})
@RunWith(org.junit.runners.Suite.class)
@SelectClasses({
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
