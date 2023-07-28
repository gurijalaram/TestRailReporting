package testsuites;

import com.apriori.UserPreferencesTests;
import com.apriori.UserTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
@SelectClasses({
    UserTests.class,
    UserPreferencesTests.class
})
public class RegressionTestSuite {
}
