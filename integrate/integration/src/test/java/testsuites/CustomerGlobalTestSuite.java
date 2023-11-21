package testsuites;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    CustomerApplicationsTestSuite.class,
    CustomerSmokeEnvironmentTestSuite.class,
    CustomerTagTestSuite.class
})
public class CustomerGlobalTestSuite {
}
