package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.explore.SearchTests;
import com.explore.StartComparisonTests;
import com.login.LoginTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("867")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    LoginTests.class,
    StartComparisonTests.class,
    SearchTests.class,
})
public class RegressionTestSuite {
}