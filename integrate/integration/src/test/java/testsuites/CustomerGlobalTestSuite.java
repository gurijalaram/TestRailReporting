package testsuites;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        CustomerTagTestSuite.class,
        CustomerApplicationsTestSuite.class,
        CustomerEnvironmentTestSuite.class

})
public class CustomerGlobalTestSuite {
}
