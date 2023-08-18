package testsuites;

import com.integration.tests.customer.environment.CustomerCloudHomeTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    CustomerCloudHomeTest.class
})
public class CustomerEnvironmentTestSuite {
}
