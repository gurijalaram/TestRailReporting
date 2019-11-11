package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;
import login.ReportLoginTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("261")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses ({
    ReportLoginTests.class
})
public class ReportingSuite {
}