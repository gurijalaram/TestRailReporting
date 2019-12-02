package testsuites;

import cireporttests.navigation.NavigationTests;
import cireporttests.login.LoginTests;
import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("261")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses ({
    LoginTests.class,
    NavigationTests.class
})
public class ReportingSuite {
}