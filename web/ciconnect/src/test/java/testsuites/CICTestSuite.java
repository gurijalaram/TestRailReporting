package testsuites;

import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.cic.tests.UsersTests;
import com.cic.tests.WorkflowTests;
import com.login.LoginTests;
import com.navbar.NavBarTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        UsersTests.class,
        WorkflowTests.class
})

public class CICTestSuite {
}
