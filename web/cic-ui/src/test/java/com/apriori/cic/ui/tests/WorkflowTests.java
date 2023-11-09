package com.apriori.cic.ui.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmPartDataType;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.models.request.AgentPort;
import com.apriori.cic.api.models.request.JobDefinition;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.ui.enums.RuleOperatorEnum;
import com.apriori.cic.ui.enums.SortedOrderType;
import com.apriori.cic.ui.enums.WorkflowListColumns;
import com.apriori.cic.ui.pagedata.WorkFlowData;
import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;
import com.apriori.cic.ui.pageobjects.workflows.history.HistoryPage;
import com.apriori.cic.ui.pageobjects.workflows.schedule.SchedulePage;
import com.apriori.cic.ui.pageobjects.workflows.schedule.details.DetailsPart;
import com.apriori.shared.util.dataservice.TestDataService;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WorkflowTests extends TestBaseUI {
    private static WorkFlowData workFlowData;
    private static JobDefinition jobDefinitionData;
    private WorkflowHome workflowHome;
    private SoftAssertions softAssertions;
    private UserCredentials currentUser;
    private AgentPort agentPort;
    private String scenarioName;

    public WorkflowTests() {
        super();
    }

    @BeforeEach
    public void setup() {
        softAssertions = new SoftAssertions();
        jobDefinitionData = CicApiTestUtil.getJobDefinitionData();
        currentUser = UserUtil.getUser();
        agentPort = CicApiTestUtil.getAgentPortData();
        workFlowData = new TestDataService().getTestData("WorkFlowTestData.json", WorkFlowData.class);
        scenarioName = "AUTO_SN" + new GenerateStringUtil().getRandomNumbers();
        workFlowData.setWorkflowName(GenerateStringUtil.saltString(workFlowData.getWorkflowName()));
        workFlowData.getQueryDefinitionsData().get(0).setFieldName(PlmTypeAttributes.PLM_PART_NUMBER.getCicGuiField());
        workFlowData.getQueryDefinitionsData().get(0).setFieldOperator(RuleOperatorEnum.EQUAL.getRuleOperator());
        workFlowData.getQueryDefinitionsData().get(0).setFieldValue(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber());

    }

    @Test
    @TestRail(id = {4109, 3586, 3588, 3587, 3591, 3961})
    @Description("Test creating, editing and deletion of a workflow")
    public void testCreateEditAndDeleteWorkflow() {
        // CREATE WORK FLOW
        workflowHome = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber())
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .clickSaveButton();
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        workflowHome.closeMessageAlertBox();

        // EDIT WORK FLOW
        DetailsPart detailsPart = workflowHome.selectScheduleTab()
            .selectWorkflow(workFlowData.getWorkflowName())
            .clickEditWorkflowBtn();
        workFlowData.setWorkflowName(GenerateStringUtil.saltString("- - - 0 0 Auto_Upd"));
        workflowHome = detailsPart.enterWorkflowNameField(workFlowData.getWorkflowName())
            .clickNextBtnInDetailsTab()
            .clickWFQueryDefNextBtn()
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .clickSaveButton();
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition updated!");
        workflowHome.closeMessageAlertBox();
        workFlowData.setWorkflowName(workFlowData.getWorkflowName());
        softAssertions.assertThat(workflowHome.selectScheduleTab().getItemFromWorkflowList(workFlowData.getWorkflowName(), WorkflowListColumns.Last_Modified_By).getText()).isEqualTo(currentUser.getEmail());
        //DELETE WORKFLOW
        SchedulePage schedulePage = workflowHome.selectScheduleTab().selectWorkflow(workFlowData.getWorkflowName())
            .clickDeleteButton().clickConfirmAlertBoxDelete();
        softAssertions.assertThat(schedulePage.isWorkflowExists(workFlowData.getWorkflowName())).isEqualTo(false);
    }

    @Test
    @TestRail(id = {4273})
    @Description("Test the state of the edit, delete and invoke buttons on the WF schedule screen. With a WF selected" +
        "and with no WF selected")
    public void testButtonState() {
        // CREATE WORK FLOW
        SchedulePage schedulePage = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab();

        //Verify Schedule Page -> WorkFlow Edit and Delete button is in disabled mode
        softAssertions.assertThat(schedulePage.getEditWorkflowButton().isEnabled()).isFalse();
        softAssertions.assertThat(schedulePage.getDeleteWorkflowButton().isEnabled()).isFalse();

        schedulePage.selectWorkflowByRow(2);

        //Verify Schedule Page -> WorkFlow Edit and Delete button is in enabled mode
        softAssertions.assertThat(schedulePage.getEditWorkflowButton().isEnabled()).isTrue();
        softAssertions.assertThat(schedulePage.getDeleteWorkflowButton().isEnabled()).isTrue();
    }

    @Test
    @TestRail(id = {3809, 3944})
    @Description("Test default sorting, ascending and descending of workflows by name in the schedule table")
    public void testSortedByName() {
        workflowHome = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber())
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");

        SchedulePage schedulePage = workflowHome.selectScheduleTab();
        //Verify workflow name are sorted by ascending order
        softAssertions.assertThat(schedulePage.isWorkflowListIsSorted(WorkflowListColumns.Name, SortedOrderType.ASCENDING, workFlowData.getWorkflowName())).isEqualTo(true);
        //Verify workflow name are sorted by descending order
        softAssertions.assertThat(schedulePage.isWorkflowListIsSorted(WorkflowListColumns.Name, SortedOrderType.DESCENDING, workFlowData.getWorkflowName())).isEqualTo(false);
        //Verify workflow name are sorted by ascending order
        softAssertions.assertThat(schedulePage.isWorkflowListIsSorted(WorkflowListColumns.Name, SortedOrderType.ASCENDING, workFlowData.getWorkflowName())).isEqualTo(true);
        jobDefinitionData.setJobDefinition(CicApiTestUtil.getMatchedWorkflowId(workFlowData.getWorkflowName()).getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(workflowHome.getJsessionId(), jobDefinitionData);
    }

    @Test
    @TestRail(id = {4302})
    public void testValidateInputFields() {
        DetailsPart detailsPart = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton();
        softAssertions.assertThat(detailsPart.getMinutesTab().isDisplayed()).isEqualTo(true);
        softAssertions.assertThat(detailsPart.getHourlyTab().isDisplayed()).isEqualTo(true);
        softAssertions.assertThat(detailsPart.getDailyTab().isDisplayed()).isEqualTo(true);
        softAssertions.assertThat(detailsPart.getWeeklyTab().isDisplayed()).isEqualTo(true);
        softAssertions.assertThat(detailsPart.getMonthlyTab().isDisplayed()).isEqualTo(true);
        softAssertions.assertThat(detailsPart.getYearlyTab().isDisplayed()).isEqualTo(true);
    }

    @Test
    @TestRail(id = {3991})
    public void testValidateNameWithSpecial() {
        DetailsPart detailsPart = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton();
        detailsPart.enterWorkflowNameField("!@#$!@#$");
        assertEquals("verify Workflow name field with special characters", "Name should only contain spaces and the following characters: a-zA-Z0-9-_", detailsPart.getWorkflowNameErrorLbl().getText());

        detailsPart.enterWorkflowNameField("w12345678901234567890123456789012345678901234567890123456789012345678901234567890");
        assertEquals("verify Workflow name field with special characters", "Name must be less than or equal to 64 characters.", detailsPart.getWorkflowNameErrorLbl().getText());
    }

    @Test
    @TestRail(id = {5842, 5956, 6070})
    @Description("Cancel job using the cancel button in CIC App, " +
        "Job status and Status Details are as expected when cancelled from CIC App, " +
        "Cancel button is not enabled for a job in a terminal state")
    public void testCreateInvokeCancelWorkflow() {
        workflowHome = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber())
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        workflowHome.closeMessageAlertBox()
            .selectScheduleTab()
            .selectWorkflow(workFlowData.getWorkflowName())
            .clickInvokeButton();
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).contains("The job was successfully started");
        HistoryPage historyPage = workflowHome.closeMessageAlertBox()
            .selectViewHistoryTab()
            .searchWorkflow(workFlowData.getWorkflowName())
            .clickCancelButton();

        softAssertions.assertThat(historyPage.searchAndTrackWorkFlowStatus(workFlowData.getWorkflowName())).isTrue();
        historyPage.searchWorkflow(workFlowData.getWorkflowName());
        softAssertions.assertThat(historyPage.getWorkflowStatusDetails(workFlowData.getWorkflowName())).contains("ended with state 'CANCELLED'");
        softAssertions.assertThat(historyPage.isCancelButtonEnabled()).isFalse();
        jobDefinitionData.setJobDefinition(CicApiTestUtil.getMatchedWorkflowId(workFlowData.getWorkflowName()).getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(workflowHome.getJsessionId(), jobDefinitionData);
    }

    @AfterEach
    public void cleanup() {
        softAssertions.assertAll();
    }
}
