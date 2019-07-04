package test.java.testsuites;


import main.java.api.AccountsTest;
import main.java.runner.ConcurrentSuiteRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        AccountsTest.class
})
public class APITestSuite {
}
