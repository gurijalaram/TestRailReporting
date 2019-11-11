package testsuites;

import com.apriori.utils.runner.ConcurrentSuiteRunner;
import login.ReportLoginTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//TODO: add project ID for TestRail
//@ProjectRunID("999")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses ({
    ReportLoginTests.class
})
public class ReportingSuite {
}