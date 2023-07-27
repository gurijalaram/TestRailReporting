package testsuites;

import com.navigation.AdminNavigationTests;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@SelectClasses(AdminNavigationTests.class)
public class RegressionSuite {
}