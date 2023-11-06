package testsuites;

import com.apriori.bcs.api.tests.AdditionalMappingsTest;
import com.apriori.bcs.api.tests.BatchPartNegativeTest;
import com.apriori.bcs.api.tests.BatchPartTest;
import com.apriori.bcs.api.tests.BatchResourcesTest;
import com.apriori.bcs.api.tests.CostingScenarioTest;
import com.apriori.bcs.api.tests.CustomerNegativeTests;
import com.apriori.bcs.api.tests.CustomerResourcesTest;
import com.apriori.bcs.api.tests.ReportResourcesTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    BatchResourcesTest.class,
    BatchPartTest.class,
    CustomerResourcesTest.class,
    ReportResourcesTest.class,
    CostingScenarioTest.class,
    BatchPartNegativeTest.class,
    CustomerNegativeTests.class,
    AdditionalMappingsTest.class
})
public class APISuite {
}
