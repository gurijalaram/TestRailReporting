package testsuites;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cic.ui.tests.AssemblyReportTests;
import com.apriori.cic.ui.tests.ConnectorTabTests;
import com.apriori.cic.ui.tests.CostingInputTabTests;
import com.apriori.cic.ui.tests.CostingPreferencesTests;
import com.apriori.cic.ui.tests.NavBarTests;
import com.apriori.cic.ui.tests.NotificationTests;
import com.apriori.cic.ui.tests.PlmIntegrationTests;
import com.apriori.cic.ui.tests.PlmQueryDefDateRuleTests;
import com.apriori.cic.ui.tests.PlmQueryDefIntRuleTests;
import com.apriori.cic.ui.tests.PlmQueryDefRealRuleTests;
import com.apriori.cic.ui.tests.PlmQueryDefStrEmailRuleTests;
import com.apriori.cic.ui.tests.PlmUdaTests;
import com.apriori.cic.ui.tests.PlmWorkflowRevisionTests;
import com.apriori.cic.ui.tests.PublishResultsTests;
import com.apriori.cic.ui.tests.QueryDefinitionTests;
import com.apriori.cic.ui.tests.UsersTests;
import com.apriori.cic.ui.tests.WorkflowHistoryTests;
import com.apriori.cic.ui.tests.WorkflowScheduleTests;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    ConnectorTabTests.class,
    CostingInputTabTests.class,
    CostingPreferencesTests.class,
    NavBarTests.class,
    NotificationTests.class,
    PlmIntegrationTests.class,
    PlmQueryDefIntRuleTests.class,
    PlmQueryDefDateRuleTests.class,
    PlmQueryDefRealRuleTests.class,
    PlmQueryDefStrEmailRuleTests.class,
    PlmUdaTests.class,
    PlmWorkflowRevisionTests.class,
    PublishResultsTests.class,
    QueryDefinitionTests.class,
    UsersTests.class,
    WorkflowScheduleTests.class,
    WorkflowHistoryTests.class,
    AssemblyReportTests.class
})

@IncludeTags(SMOKE)
public class CICSmokeTestSuite {
}
