package com.apriori.cic.ui.tests;

import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmPartDataType;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.models.request.AgentPort;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.ui.enums.JobDetailsListHeaders;
import com.apriori.cic.ui.enums.RuleOperatorEnum;
import com.apriori.cic.ui.enums.WorkflowListColumns;
import com.apriori.cic.ui.pagedata.WorkFlowData;
import com.apriori.cic.ui.pageobjects.home.CIConnectHome;
import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;
import com.apriori.cic.ui.pageobjects.workflows.history.HistoryPage;
import com.apriori.cic.ui.pageobjects.workflows.history.JobDetails;
import com.apriori.cic.ui.pageobjects.workflows.schedule.SchedulePage;
import com.apriori.serialization.util.DateFormattingUtils;
import com.apriori.shared.util.dataservice.TestDataService;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.DateUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WorkflowHistoryTests extends TestBaseUI {

    private static WorkFlowData workFlowData;
    private UserCredentials currentUser;
    private WorkflowHome workflowHome;
    private SoftAssertions softAssertions;
    private CIConnectHome ciConnectHome;
    private AgentPort agentPort;
    private String plmPartNumber;

    public WorkflowHistoryTests() {
        super();
    }

    @BeforeEach
    public void setup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
        ciConnectHome = new CicLoginPage(driver).login(currentUser);
        agentPort = CicApiTestUtil.getAgentPortData();
        workFlowData = new TestDataService().getTestData("WorkFlowTestData.json", WorkFlowData.class);
        plmPartNumber = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber();
    }

    @Test
    @TestRail(id = {3589})
    @Description("Test creating, editing and deletion of a worflow")
    public void testStartAndTrackJob() {
        workFlowData.setWorkflowName(GenerateStringUtil.saltString("----0WFC"));
        workFlowData.getQueryDefinitionsData().get(0).setFieldValue(plmPartNumber);
        String scenarioName = "AUTO_SN" + new GenerateStringUtil().getRandomNumbers();
        String plmPartNumber = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber();

        // CREATE WORK FLOW
        workflowHome = ciConnectHome
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(agentPort.getConnector())
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, plmPartNumber)
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
        softAssertions.assertThat(workflowHome.selectViewHistoryTab().searchAndTrackWorkFlowStatus(workFlowData.getWorkflowName())).isTrue();
    }

    @Test
    @TestRail(id = {4317, 4397, 3950, 3981})
    @Description("Verify start time displayed is based on the browser timezone, " +
        "Locked workflow may not be edited" +
        "Test Job Details list control panel " +
        "Test the Job Details Dialog")
    public void testVerifyJobDetailsOfWorkflow() {
        workFlowData.setWorkflowName(GenerateStringUtil.saltString("----0WFC"));
        String scenarioName = "AUTO_SN" + new GenerateStringUtil().getRandomNumbers();
        workFlowData.setWorkflowName(GenerateStringUtil.saltString(workFlowData.getWorkflowName()));
        String date = DateUtil.getCurrentDate(DateFormattingUtils.dtf_MMddyyyyHHmmss_hyphen);
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
            .selectWorkflowAndInvoke(workFlowData.getWorkflowName());
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).contains("The job was successfully started");

        SchedulePage schedulePage = workflowHome.selectScheduleTab();

        softAssertions.assertThat(schedulePage.getWorkflowLockedStatus(workFlowData.getWorkflowName(), WorkflowListColumns.Locked).isSelected()).isTrue();
        softAssertions.assertThat(schedulePage.getEditWorkflowButton().isEnabled()).isFalse();

        HistoryPage historyPage = workflowHome.selectViewHistoryTab()
            .searchWorkflow(workFlowData.getWorkflowName());

        softAssertions.assertThat(historyPage.getWorkflowStartedAt(workFlowData.getWorkflowName())).contains(StringUtils.split(date, ":")[0]);
        softAssertions.assertThat(historyPage.searchAndTrackWorkFlowStatus(workFlowData.getWorkflowName())).isTrue();

        JobDetails jobDetails = historyPage.clickViewDetailsButton();
        softAssertions.assertThat(jobDetails.getWorkflowNameElement().getText()).isEqualTo(this.workFlowData.getWorkflowName());
        softAssertions.assertThat(jobDetails.getStartedAtElement().getText()).contains(StringUtils.split(date, ":")[0]);
        softAssertions.assertThat(jobDetails.getIdElement().getText()).isNotEmpty();
        softAssertions.assertThat(jobDetails.getJobStatusElement().getText()).isNotEmpty();
        softAssertions.assertThat(jobDetails.getJobStatusElement().getText()).isNotEmpty();

        jobDetails = jobDetails.selectPartRow("Casting - Die", JobDetailsListHeaders.PROCESS_GROUP);
        softAssertions.assertThat(jobDetails.getStartedAtElement().getText()).contains(StringUtils.split(date, ":")[0]);
    }

    @Test
    @TestRail(id = {3987, 4288, 3988})
    @Description("Test column selection dialog" +
        "Test persistence of column selection")
    public void testVerifyJobDetailsHeaders() {
        JobDetails jobDetails = ciConnectHome.clickWorkflowMenu().selectViewHistoryTab().clickViewDetailsButton();
        softAssertions.assertThat(jobDetails.getExportBtn().isEnabled()).isTrue();
        softAssertions.assertThat(jobDetails.getIdElement().getText()).isNotEmpty();
        softAssertions.assertThat(jobDetails.getJobStatusElement().getText()).isNotEmpty();
        softAssertions.assertThat(jobDetails.getJobStatusElement().getText()).isNotEmpty();
        softAssertions.assertThat(jobDetails.isHeaderExists(JobDetailsListHeaders.APRIORI_PART_NUMBER)).isTrue();
        softAssertions.assertThat(jobDetails.isHeaderExists(JobDetailsListHeaders.PART_STATUS)).isTrue();
        softAssertions.assertThat(jobDetails.isHeaderExists(JobDetailsListHeaders.STATUS_DETAILS)).isTrue();
        softAssertions.assertThat(jobDetails.isHeaderExists(JobDetailsListHeaders.COSTING_RESULT)).isTrue();
        softAssertions.assertThat(jobDetails.isHeaderExists(JobDetailsListHeaders.DFM_RISK_RATING)).isTrue();
        softAssertions.assertThat(jobDetails.isHeaderExists(JobDetailsListHeaders.PROCESS_GROUP)).isTrue();
        softAssertions.assertThat(jobDetails.isHeaderExists(JobDetailsListHeaders.DIGITAL_FACTORY)).isTrue();

        jobDetails.rightClickOnHeader().clickHeaderCheckboxFromPopList(JobDetailsListHeaders.PLM_PART_ID);
        softAssertions.assertThat(jobDetails.isHeaderExists(JobDetailsListHeaders.PLM_PART_ID)).isTrue();

        jobDetails = jobDetails.clickCloseButton().clickViewDetailsButton();
        softAssertions.assertThat(jobDetails.isHeaderExists(JobDetailsListHeaders.PLM_PART_ID)).isTrue();

        jobDetails.rightClickOnHeader().clickHeaderCheckboxFromPopList(JobDetailsListHeaders.PLM_PART_ID);
        softAssertions.assertThat(jobDetails.isHeaderExists(JobDetailsListHeaders.PLM_PART_ID)).isFalse();
    }

    @AfterEach
    public void cleanup() {
        softAssertions.assertAll();
        CicApiTestUtil.deleteWorkFlow(this.ciConnectHome.getSession(), CicApiTestUtil.getMatchedWorkflowId(this.workFlowData.getWorkflowName()));
    }
}