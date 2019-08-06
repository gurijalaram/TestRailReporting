package test.java.testsuites;

import main.java.runner.ConcurrentSuiteRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.java.login.LoginTests;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        LoginTests.class
})
public class LoginTestSuite {

}
