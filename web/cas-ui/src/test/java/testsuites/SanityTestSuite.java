package testsuites;

import com.apriori.testsuites.categories.SanityTest;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.customer.NewCustomerTests;
import com.customer.systemconfiguration.SystemConfigurationGroupsTests;
import com.customer.systemconfiguration.SystemConfigurationPermissionsTests;
import com.login.LoginTests;
import com.navigation.NavigationTests;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("874")
@RunWith(CategorySuiteRunner.class)
@Categories.IncludeCategory({SanityTest.class})
@Suite.SuiteClasses({
    NewCustomerTests.class,
    LoginTests.class,
    NavigationTests.class,
    SystemConfigurationGroupsTests.class,
    SystemConfigurationPermissionsTests.class
})
public class SanityTestSuite {
}
