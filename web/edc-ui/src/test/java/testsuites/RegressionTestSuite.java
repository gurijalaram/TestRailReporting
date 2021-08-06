package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.login.LoginTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("262")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    LoginTests.class
})
public class RegressionTestSuite {
}
