package testsuites;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        CustomerApplicationsTestSuite.class,
        CustomerEnvironmentTestSuite.class,
        CustomerTagTestSuite.class
})
public class CustomerGlobalTestSuite {
}
