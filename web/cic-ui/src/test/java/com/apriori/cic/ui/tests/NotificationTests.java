package com.apriori.cic.ui.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.cic.api.enums.PlmPartDataType;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.enums.ReportsEnum;
import com.apriori.cic.api.models.request.AgentPort;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.api.utils.WorkflowTestUtil;
import com.apriori.cic.ui.enums.RuleOperatorEnum;
import com.apriori.cic.ui.pagedata.WorkFlowData;
import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.cic.ui.pageobjects.workflows.schedule.costinginputs.CostingInputsPart;
import com.apriori.cic.ui.pageobjects.workflows.schedule.details.DetailsPart;
import com.apriori.cic.ui.pageobjects.workflows.schedule.notifications.AttachReportTab;
import com.apriori.cic.ui.pageobjects.workflows.schedule.notifications.FilterTab;
import com.apriori.cic.ui.pageobjects.workflows.schedule.notifications.NotificationsPart;
import com.apriori.cic.ui.pageobjects.workflows.schedule.querydefinitions.QueryDefinitions;
import com.apriori.shared.util.dataservice.TestDataService;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class NotificationTests extends WorkflowTestUtil {

    private static WorkFlowData workFlowData;
    private AgentPort agentPort;
    private String plmPartNumber;

    @BeforeEach
    public void setup() {
        currentUser = UserUtil.getUser();
        workFlowData = new TestDataService().getTestData("WorkFlowTestData.json",WorkFlowData.class);
        plmPartNumber = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber();
        agentPort = CicApiTestUtil.getAgentPortData();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {3951, 4875})
    @Description("Test email Tab on the Add New Workflow Dialog")
    public void testNotificationsEmailTab() {
        workFlowData.setWorkflowName(GenerateStringUtil.saltString("----0WFC"));
        String plmPartNumber = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber();
        DetailsPart detailsPart = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton();

        QueryDefinitions queryDefinitions = (QueryDefinitions) detailsPart.enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .clickWFDetailsNextBtn();

        CostingInputsPart costingInputsPart = queryDefinitions.addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, plmPartNumber)
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

        CostingInputsPart costingInputsPart = queryDefinitions.addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, plmPartNumber)
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

        CostingInputsPart costingInputsPart = queryDefinitions.addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, plmPartNumber)
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

        CostingInputsPart costingInputsPart = queryDefinitions.addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, plmPartNumber)
            .clickWFQueryDefNextBtn();
        NotificationsPart notificationsPart = costingInputsPart.clickCINextBtn();
        FilterTab filterTab = notificationsPart.selectEmailTab().selectEmailTemplate().selectFilterTab();

        assertTrue(filterTab.getFilterRuleList().containsAll(rulesExpectedList),"verify filter rule drop down values");
    }
}
