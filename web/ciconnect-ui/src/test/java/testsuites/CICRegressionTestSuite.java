package testsuites;

import com.apriori.ConnectorTabTests;
import com.apriori.CostingInputTabTests;
import com.apriori.CostingPreferencesTests;
import com.apriori.NavBarTests;
import com.apriori.NotificationTests;
import com.apriori.PlmIntegrationTests;
import com.apriori.PlmQueryDefDateRuleTests;
import com.apriori.PlmQueryDefIntRuleTests;
import com.apriori.PlmQueryDefRealRuleTests;
import com.apriori.PlmQueryDefStrEmailRuleTests;
import com.apriori.PlmUdaTests;
import com.apriori.PlmWorkflowRevisionTests;
import com.apriori.PublishResultsTests;
import com.apriori.QueryDefinitionTests;
import com.apriori.UsersTests;
import com.apriori.WorkflowHistoryTests;
import com.apriori.WorkflowScheduleTests;
import com.apriori.WorkflowTests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.apriori")
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
    PlmQueryDefStrEmailRuleTests.class
})
public class CICRegressionTestSuite {
}
