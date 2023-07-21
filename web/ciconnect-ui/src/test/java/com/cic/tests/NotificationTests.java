package com.cic.tests;

import com.apriori.pagedata.WorkFlowData;
import com.apriori.pages.login.CicLoginPage;
import com.apriori.pages.workflows.WorkflowHome;
import com.apriori.pages.workflows.schedule.costinginputs.CostingInputsPart;
import com.apriori.pages.workflows.schedule.details.DetailsPart;
import com.apriori.pages.workflows.schedule.notifications.AttachReportTab;
import com.apriori.pages.workflows.schedule.notifications.FilterTab;
import com.apriori.pages.workflows.schedule.notifications.NotificationsPart;
import com.apriori.pages.workflows.schedule.querydefinitions.QueryDefinitions;
import com.apriori.utils.TestRail;
import com.apriori.utils.dataservice.TestDataService;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import enums.ReportsEnum;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.WorkflowTestUtil;

import java.util.Arrays;
import java.util.List;

public class NotificationTests extends WorkflowTestUtil {

    private static WorkFlowData workFlowData;

    @Before
    public void setup() {
        currentUser = UserUtil.getUser();
        workFlowData = new TestDataService().getTestData("WorkFlowTestData.json",WorkFlowData.class);
    }

    @Test
    @TestRail(testCaseId = {"3951", "4875"})
    @Description("Test email Tab on the Add New Workflow Dialog")
    public void testNotificationsEmailTab() {
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

    @Test
    @TestRail(testCaseId = {"5693", "5694", "5710", "5711"})
    @Description("Verify Filter Application checkboxes are disabled until appropriate template(s) selected")
    public void testNotificationsFilterTab() {
        SoftAssertions softAssertions = new SoftAssertions();
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

        softAssertions.assertThat(notificationsPart.getAttachReportTab().getAttribute("class").contains("disabled")).isEqualTo(true);
        softAssertions.assertThat(notificationsPart.getFilterTab().getAttribute("class").contains("disabled")).isEqualTo(true);

        notificationsPart.selectEmailTab().selectEmailTemplate();

        softAssertions.assertThat(notificationsPart.getAttachReportTab().getAttribute("class").contains("disabled")).isEqualTo(false);
        softAssertions.assertThat(notificationsPart.getFilterTab().getAttribute("class").contains("disabled")).isEqualTo(false);

        FilterTab filterTab = notificationsPart.selectFilterTab();
        softAssertions.assertThat(filterTab.getEmailCheckbox().isEnabled()).isEqualTo(true);
        softAssertions.assertThat(filterTab.getAttachReportCheckbox().isEnabled()).isEqualTo(false);

        notificationsPart.selectAttachReport().selectReportName();
        filterTab = notificationsPart.selectFilterTab();
        softAssertions.assertThat(filterTab.getAttachReportCheckbox().isEnabled()).isEqualTo(true);

        filterTab.getEmailCheckbox().click();
        softAssertions.assertThat(notificationsPart.getNotificationNextButton().isEnabled()).isEqualTo(false);

        workFlowData.getNotificationsData().setEmailTemplate("None");
        notificationsPart.selectEmailTab().selectEmailTemplate();

        softAssertions.assertThat(notificationsPart.getAttachReportTab().getAttribute("class").contains("disabled")).isEqualTo(true);
        softAssertions.assertThat(notificationsPart.getFilterTab().getAttribute("class").contains("disabled")).isEqualTo(true);

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"5714"})
    @Description("Verify Filter tab rule drop down list")
    public void testFilterTabVerifyRuleDdl() {
        List<String> rulesExpectedList = Arrays.asList(new String[] {"Capital Investment",
            "Piece Part Cost",
            "Fully Burdened Cost",
            "Material Cost",
            "Labor Time",
            "Finish Mass",
            "Rough Mass",
            "Utilization",
            "DFM Risk Rating",
            "Currency Code",
            "Costing Result",
            "Cycle Time",
            "aPriori Part Number",
            "DFM Risk Score"});
        SoftAssertions softAssertions = new SoftAssertions();
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
        FilterTab filterTab = notificationsPart.selectEmailTab().selectEmailTemplate().selectFilterTab();

        Assert.assertTrue("verify filter rule drop down values",filterTab.getFilterRuleList().containsAll(rulesExpectedList));
    }
}
