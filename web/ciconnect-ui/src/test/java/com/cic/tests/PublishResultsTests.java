package com.cic.tests;

import com.apriori.pagedata.WorkFlowData;
import com.apriori.pages.login.CicLoginPage;
import com.apriori.pages.workflows.WorkflowHome;
import com.apriori.pages.workflows.schedule.costinginputs.CostingInputsPart;
import com.apriori.pages.workflows.schedule.details.DetailsPart;
import com.apriori.pages.workflows.schedule.notifications.AttachReportTab;
import com.apriori.pages.workflows.schedule.notifications.EmailTab;
import com.apriori.pages.workflows.schedule.notifications.NotificationsPart;
import com.apriori.pages.workflows.schedule.publishresults.PRAttachReportTab;
import com.apriori.pages.workflows.schedule.publishresults.PublishResultsPart;
import com.apriori.pages.workflows.schedule.querydefinitions.QueryDefinitions;
import com.apriori.utils.TestRail;
import com.apriori.utils.dataservice.TestDataService;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import enums.ReportsEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

public class PublishResultsTests extends TestBase {

    private UserCredentials currentUser = UserUtil.getUser();
    private static WorkFlowData workFlowData;
    WorkflowHome workflowHome;
    SoftAssertions softAssertions;

    public PublishResultsTests() {
        super();
    }

    @Before
    public void setUpAndLogin() {
        workFlowData = new TestDataService().getTestData("WorkFlowTestData.json",WorkFlowData.class);
    }

    @Test
    @TestRail(testCaseId = {"4042"})
    @Description("Test Reports Tab on the publish results tab during workflow creation")
    public void testPublishResultsAttachReportTab() {
        softAssertions = new SoftAssertions();
        DetailsPart detailsPart = new CicLoginPage(driver)
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
        EmailTab emailTab = notificationsPart.selectEmailTab().selectEmailTemplate().selectRecipient();
        AttachReportTab attachReportTab = notificationsPart.selectAttachReport().selectReportName();
        softAssertions.assertThat(attachReportTab.getCurrencyCodeDdl().isDisplayed()).isEqualTo(true);

        workFlowData.getNotificationsData().setReportName(ReportsEnum.PART_COST.getReportName());
        attachReportTab = notificationsPart.selectAttachReport().selectReportName();
        softAssertions.assertThat(attachReportTab.getEmptyReportLbl().getText()).isEqualTo("No report configuration defined.");

        workFlowData.getNotificationsData().setReportName(ReportsEnum.DTC_MULTIPLE_COMPONENT_SUMMARY.getReportName());
        attachReportTab = notificationsPart.selectAttachReport().selectReportName();
        softAssertions.assertThat(notificationsPart.selectAttachReport().getCostMetricDdl().isDisplayed()).isEqualTo(true);
        PublishResultsPart publishResultsPart = notificationsPart.clickCINotificationNextBtn();

        workFlowData.getPublishResultsData().setReportName(ReportsEnum.DTC_COMPONENT_SUMMARY.getReportName());
        PRAttachReportTab prAttachReportTab = publishResultsPart.selectAttachReportTab().selectReportName();
        softAssertions.assertThat(prAttachReportTab.getCurrencyCodeDdl().isDisplayed()).isEqualTo(true);
        softAssertions.assertThat(prAttachReportTab.getCostRoundingDdl().isDisplayed()).isEqualTo(true);

        workFlowData.getPublishResultsData().setReportName(ReportsEnum.PART_COST.getReportName());
        prAttachReportTab = publishResultsPart.selectAttachReportTab().selectReportName();
        softAssertions.assertThat(prAttachReportTab.getEmptyPRReportLbl().getText()).isEqualTo("No report configuration defined.");
        softAssertions.assertAll();
    }
}
