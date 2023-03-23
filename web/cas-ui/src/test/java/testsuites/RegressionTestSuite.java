package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import com.infrastructure.AccessControlsApplicationTests;
import com.navigation.NavigationTests;
import com.security.MfaEnabledTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("874")
@RunWith(CategorySuiteRunner.class)
@Suite.SuiteClasses({
    CustomerTestSuite.class,
    NavigationTests.class,
    UsersTestSuite.class,
    SystemConfigurationTestSuite.class,
    MfaEnabledTests.class,
    AccessControlsApplicationTests.class
})
public final class RegressionTestSuite {
}
