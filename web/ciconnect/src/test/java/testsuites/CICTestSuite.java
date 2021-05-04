package testsuites;

import com.apriori.utils.ProjectRunID;
import com.apriori.utils.runner.ConcurrentSuiteRunner;

import com.cic.tests.NavBarTests;
import com.cic.tests.NewWorkflowDetailsTests;
import com.cic.tests.NotificationTests;
import com.cic.tests.QueryDefinitionTests;
import com.cic.tests.UsersTests;
import com.cic.tests.WorkflowTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@ProjectRunID("561")
@RunWith(ConcurrentSuiteRunner.class)
@Suite.SuiteClasses({
        UsersTests.class,
        WorkflowTests.class,
        NewWorkflowDetailsTests.class,
        QueryDefinitionTests.class,
        NotificationTests.class,
        NavBarTests.class
})

public class CICTestSuite {
}
