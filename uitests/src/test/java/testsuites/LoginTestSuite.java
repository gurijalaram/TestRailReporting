package test.java.testsuites;

import main.java.LoginTests;
import main.java.runner.ConcurrentSuiteRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        LoginTests.class,
})
public class LoginTestSuite {

}
