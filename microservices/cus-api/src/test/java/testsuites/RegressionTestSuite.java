package testsuites;

import com.apriori.tests.UserPreferencesTests;
import com.apriori.tests.UserTests;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.CategorySuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("768")
@RunWith(CategorySuiteRunner.class)
@Suite.SuiteClasses({
    UserTests.class,
    UserPreferencesTests.class
})
public class RegressionTestSuite {
}
