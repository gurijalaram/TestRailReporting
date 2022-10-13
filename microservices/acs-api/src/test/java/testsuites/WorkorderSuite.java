package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.workorders.WorkorderAPITests;

@ProjectRunID("1348")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
    WorkorderAPITests.class
})

public class WorkorderSuite {
}
