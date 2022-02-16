package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.customer.systemconfiguration.SystemConfigurationGroupsTests;
import com.customer.systemconfiguration.SystemConfigurationPermissionsTests;
import com.customer.systemconfiguration.SystemConfigurationTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("874")
@RunWith(CategorySuiteRunner.class)
@Suite.SuiteClasses({
    SystemConfigurationGroupsTests.class,
    SystemConfigurationPermissionsTests.class,
    SystemConfigurationTests.class
})
public class SystemConfigurationTestSuite {
}
