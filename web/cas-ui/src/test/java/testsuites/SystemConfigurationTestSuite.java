package testsuites;

import com.apriori.cas.ui.tests.customer.systemconfiguration.SystemConfigurationGroupsTests;
import com.apriori.cas.ui.tests.customer.systemconfiguration.SystemConfigurationPermissionsTests;
import com.apriori.cas.ui.tests.customer.systemconfiguration.SystemConfigurationTests;

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
