package testsuites;

import com.integration.tests.customer.environment.CustomerCloudHomeAPITest;
import com.integration.tests.customer.environment.CustomerCloudHomeUITest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    CustomerCloudHomeAPITest.class,
    CustomerCloudHomeUITest.class
})
public class CustomerEnvironmentTestSuite {
}
