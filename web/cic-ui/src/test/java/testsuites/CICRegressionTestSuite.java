package testsuites;

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
    CostingInputTabTests.class,
    ConnectorTabTests.class,
    WorkflowScheduleTests.class,
    PlmIntegrationTests.class,
    CostingPreferencesTests.class,
    PlmUdaTests.class,
    PlmWorkflowRevisionTests.class,
    PlmQueryDefDateRuleTests.class,
    PlmQueryDefIntRuleTests.class,
    PlmQueryDefRealRuleTests.class,
    PlmQueryDefStrEmailRuleTests.class,
    AssemblyReportTests.class
})
public class CICRegressionTestSuite {
}
