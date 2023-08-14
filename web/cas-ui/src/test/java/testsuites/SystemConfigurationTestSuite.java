package testsuites;

import com.apriori.customer.systemconfiguration.SystemConfigurationGroupsTests;
import com.apriori.customer.systemconfiguration.SystemConfigurationPermissionsTests;
import com.apriori.customer.systemconfiguration.SystemConfigurationTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    SystemConfigurationGroupsTests.class,
    SystemConfigurationPermissionsTests.class,
    SystemConfigurationTests.class
})
public class SystemConfigurationTestSuite {
}
