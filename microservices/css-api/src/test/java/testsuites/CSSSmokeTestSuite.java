package testsuites;

import com.apriori.tests.newendpoint.ScenarioIterationPostTests;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    ScenarioIterationPostTests.class
})
public class CSSSmokeTestSuite {
}
