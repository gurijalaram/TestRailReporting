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

@Suite
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
