package testsuites;

import com.apriori.CostingInputTabTests;
import com.apriori.NavBarTests;
import com.apriori.NotificationTests;
import com.apriori.PublishResultsTests;
import com.apriori.QueryDefinitionTests;
import com.apriori.UsersTests;
import com.apriori.WorkflowHistoryTests;
import com.apriori.WorkflowTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

@Suite
@org.junit.runners.Suite.SuiteClasses({})
@RunWith(org.junit.runners.Suite.class)
@SelectClasses({
    NavBarTests.class,
    NotificationTests.class,
    QueryDefinitionTests.class,
    UsersTests.class,
    WorkflowTests.class,
    WorkflowHistoryTests.class,
    PublishResultsTests.class,
    CostingInputTabTests.class
})
public class CICTestSuite {
}
