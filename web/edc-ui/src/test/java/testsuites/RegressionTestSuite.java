package testsuites;

import com.apriori.utils.ProjectRunID;

import com.login.LoginTests;
import org.junit.runners.Suite;

@ProjectRunID("262")
@Suite.SuiteClasses({
    LoginTests.class
})
public class RegressionTestSuite {
}
