package com.cic.tests;

import com.apriori.enums.ReportsEnum;
import com.apriori.pagedata.WorkFlowData;
import com.apriori.pages.login.LoginPage;
import com.apriori.pages.workflows.WorkflowHome;
import com.apriori.pages.workflows.schedule.costinginputs.CostingInputsPart;
import com.apriori.pages.workflows.schedule.details.DetailsPart;
import com.apriori.pages.workflows.schedule.notifications.AttachReportTab;
import com.apriori.pages.workflows.schedule.notifications.NotificationsPart;
import com.apriori.pages.workflows.schedule.querydefinitions.QueryDefinitions;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import common.testdata.TestDataService;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NotificationTests extends TestBase {

    private UserCredentials currentUser = UserUtil.getUser();
    private static WorkFlowData workFlowData;
    WorkflowHome workflowHome;

    public NotificationTests() {
        super();
    }

    @Before
    public void setUpAndLogin() {
        workFlowData = new TestDataService().getTestData();
    }

    @Test
    @TestRail(testCaseId = {"3951", "4875"})
    @Description("Test email Tab on the Add New Workflow Dialog")
    public void testNotificationsEmailTab() {
        DetailsPart detailsPart = new LoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton();

        QueryDefinitions queryDefinitions = (QueryDefinitions) detailsPart.enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .clickWFDetailsNextBtn();

        CostingInputsPart costingInputsPart = queryDefinitions.addRule(workFlowData, this.workFlowData.getQueryDefinitionsData().size())
            .clickWFQueryDefNextBtn();

        NotificationsPart notificationsPart = costingInputsPart.clickCINextBtn();

        Assert.assertTrue("Verify next button is enabled",notificationsPart.getNotificationNextButton().isEnabled());
        notificationsPart.selectEmailTab().selectEmailTemplate();
        Assert.assertFalse("Verify next button is disabled",notificationsPart.getNotificationNextButton().isEnabled());
        notificationsPart.selectEmailTab().selectRecipient();
        Assert.assertTrue("Verify next button is enabled Tab",notificationsPart.getNotificationNextButton().isEnabled());
        Assert.assertEquals("Verify Costing round drop down defaulted to Yes", "Yes", notificationsPart.selectEmailTab().getEmailConfigCostRoundingElement().getText());
        Assert.assertEquals("Verify aPriori Costing round drop down defaulted to Fully Burdened Cost", "Fully Burdened Cost", notificationsPart.selectEmailTab().getEmailConfigAprioriCostElement().getText());
    }

    @Test
    @TestRail(testCaseId = {"5691"})
    @Description("Test Reports tab in Notification Step during new workflow creation")
    public void testNotificationsReportsTab() {
        SoftAssertions softAssertions = new SoftAssertions();
        DetailsPart detailsPart = new LoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton();

        QueryDefinitions queryDefinitions = (QueryDefinitions) detailsPart.enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .clickWFDetailsNextBtn();

        CostingInputsPart costingInputsPart = queryDefinitions.addRule(workFlowData, this.workFlowData.getQueryDefinitionsData().size())
            .clickWFQueryDefNextBtn();

        NotificationsPart notificationsPart = costingInputsPart.clickCINextBtn();

        notificationsPart.selectEmailTab().selectEmailTemplate().selectRecipient();
        AttachReportTab attachReportTab = notificationsPart.selectAttachReport().selectReportName();
        softAssertions.assertThat(attachReportTab.getCurrencyCodeDdl().isDisplayed()).isEqualTo(true);

        workFlowData.getNotificationsData().setReportName(ReportsEnum.PART_COST.getReportName());
        attachReportTab = notificationsPart.selectAttachReport().selectReportName();
        softAssertions.assertThat(attachReportTab.getEmptyReportLbl().getText()).isEqualTo("No report configuration defined.");

        workFlowData.getNotificationsData().setReportName(ReportsEnum.DTC_MULTIPLE_COMPONENT_SUMMARY.getReportName());
        attachReportTab = notificationsPart.selectAttachReport().selectReportName();
        softAssertions.assertThat(attachReportTab.getCostMetricDdl().isDisplayed()).isTrue();
    }
}
