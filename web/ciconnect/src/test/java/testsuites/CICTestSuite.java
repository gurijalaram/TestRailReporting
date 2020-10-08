package testsuites;

import com.apriori.utils.runner.ConcurrentSuiteRunner;

import login.LoginTests;
import navbar.NavBarTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import workflow.WorkflowTests;

@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    LoginTests.class,
    NavBarTests.class,
    WorkflowTests.class
})

public class CICTestSuite {
}
