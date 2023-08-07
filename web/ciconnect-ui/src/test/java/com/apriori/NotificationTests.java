package com.apriori;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.cic.enums.ReportsEnum;
import com.apriori.cic.utils.WorkflowTestUtil;
import com.apriori.dataservice.TestDataService;
import com.apriori.pagedata.WorkFlowData;
import com.apriori.pageobjects.login.CicLoginPage;
import com.apriori.pageobjects.workflows.schedule.costinginputs.CostingInputsPart;
import com.apriori.pageobjects.workflows.schedule.details.DetailsPart;
import com.apriori.pageobjects.workflows.schedule.notifications.AttachReportTab;
import com.apriori.pageobjects.workflows.schedule.notifications.FilterTab;
import com.apriori.pageobjects.workflows.schedule.notifications.NotificationsPart;
import com.apriori.pageobjects.workflows.schedule.querydefinitions.QueryDefinitions;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class NotificationTests extends WorkflowTestUtil {

    private static WorkFlowData workFlowData;

    @BeforeEach
    public void setup() {
        currentUser = UserUtil.getUser();
        workFlowData = new TestDataService().getTestData("WorkFlowTestData.json",WorkFlowData.class);
    }

    @Test
    @TestRail(id = {3951, 4875})
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

        assertTrue(notificationsPart.getNotificationNextButton().isEnabled(),"Verify next button is enabled");
        notificationsPart.selectEmailTab().selectEmailTemplate();
        assertFalse(notificationsPart.getNotificationNextButton().isEnabled(),"Verify next button is disabled");
        notificationsPart.selectEmailTab().selectRecipient();
        assertTrue(notificationsPart.getNotificationNextButton().isEnabled(),"Verify next button is enabled Tab");
        assertEquals("Verify Costing round drop down defaulted to Yes", "Yes", notificationsPart.selectEmailTab().getEmailConfigCostRoundingElement().getText());
        assertEquals("Verify aPriori Costing round drop down defaulted to Fully Burdened Cost", "Fully Burdened Cost", notificationsPart.selectEmailTab().getEmailConfigAprioriCostElement().getText());
    }

    @Test
    @TestRail(id = {5691})
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
    @TestRail(id = {5693, 5694, 5710, 5711})
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
    @TestRail(id = {5714})
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

        assertTrue(filterTab.getFilterRuleList().containsAll(rulesExpectedList),"verify filter rule drop down values");
    }
}
