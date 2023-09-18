package testsuites;

import com.apriori.UserPreferencesTests;
import com.apriori.UserTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@Suite
@org.junit.runners.Suite.SuiteClasses({})
@RunWith(org.junit.runners.Suite.class)
@SelectClasses({
    UserTests.class,
    UserPreferencesTests.class
})
public class RegressionTestSuite {
}
