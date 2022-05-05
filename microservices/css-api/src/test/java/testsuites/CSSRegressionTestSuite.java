package testsuites;

import com.apriori.tests.newendpoint.VerifyIfFails;
import com.apriori.tests.newendpoint.VerifyIfNotFailTests;
import com.apriori.tests.oldendpoint.ScenarioIterationGetTests;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    VerifyIfFails.class,
    VerifyIfNotFailTests.class,
    ScenarioIterationGetTests.class
})
public class CSSRegressionTestSuite {
}
