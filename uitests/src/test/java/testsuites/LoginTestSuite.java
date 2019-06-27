package test.java.testsuites;

import main.java.LoginTests;
import main.java.api.APILoginTest;
import main.java.runner.ConcurrentSuiteRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
//        LoginTests.class,
        APILoginTest.class
})
public class LoginTestSuite {

}
