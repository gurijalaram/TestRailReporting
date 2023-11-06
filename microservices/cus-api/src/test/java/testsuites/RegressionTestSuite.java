package testsuites;

import com.apriori.cus.api.tests.UserPreferencesTests;
import com.apriori.cus.api.tests.UserTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    UserTests.class,
    UserPreferencesTests.class
})
public class RegressionTestSuite {
}
