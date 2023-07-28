package testsuites;

import com.apriori.BatchPartNegativeTest;
import com.apriori.BatchPartTest;
import com.apriori.BatchResourcesTest;
import com.apriori.CostingScenarioTest;
import com.apriori.CustomerNegativeTests;
import com.apriori.CustomerResourcesTest;
import com.apriori.ReportResourcesTest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
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
