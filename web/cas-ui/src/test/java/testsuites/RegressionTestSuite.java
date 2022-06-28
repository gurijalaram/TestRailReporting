package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.customer.NewCustomerTests;
import com.navigation.NavigationTests;
import com.security.MfaEnabledTests;
import com.sitesAndLicenses.LicenseTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("874")
@RunWith(CategorySuiteRunner.class)
@Suite.SuiteClasses({
    CustomerTestSuite.class,
    NavigationTests.class,
    UsersTestSuite.class,
    SystemConfigurationTestSuite.class,
    LicenseTests.class,
    MfaEnabledTests.class
})
public final class RegressionTestSuite {
}
