package test.java.testsuites;

import main.java.runner.ConcurrentSuiteRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.java.evaluate.designguidance.tolerance.ToleranceTests;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    ToleranceTests.class
})
public class SmokeTestSuite {
}
