package testsuites;

import com.apriori.cic.ui.tests.CostingInputTabTests;
import com.apriori.cic.ui.tests.NavBarTests;
import com.apriori.cic.ui.tests.NotificationTests;
import com.apriori.cic.ui.tests.PublishResultsTests;
import com.apriori.cic.ui.tests.QueryDefinitionTests;
import com.apriori.cic.ui.tests.UsersTests;
import com.apriori.cic.ui.tests.WorkflowHistoryTests;
import com.apriori.cic.ui.tests.WorkflowTests;

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
