package testsuites;

import com.apriori.customer.systemconfiguration.SystemConfigurationGroupsTests;
import com.apriori.customer.systemconfiguration.SystemConfigurationPermissionsTests;
import com.apriori.customer.systemconfiguration.SystemConfigurationTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@Suite
@org.junit.runners.Suite.SuiteClasses({})
@RunWith(org.junit.runners.Suite.class)
@SelectClasses({
    SystemConfigurationGroupsTests.class,
    SystemConfigurationPermissionsTests.class,
    SystemConfigurationTests.class
})
public class SystemConfigurationTestSuite {
}
