package com.apriori;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apriori.cic.utils.CicApiTestUtil;
import com.apriori.dataservice.TestDataService;
import com.apriori.enums.WorkflowListColumns;
import com.apriori.http.utils.DateUtil;
import com.apriori.http.utils.GenerateStringUtil;
import com.apriori.pagedata.WorkFlowData;
import com.apriori.pageobjects.home.CIConnectHome;
import com.apriori.pageobjects.login.CicLoginPage;
import com.apriori.pageobjects.workflows.WorkflowHome;
import com.apriori.pageobjects.workflows.history.HistoryPage;
import com.apriori.pageobjects.workflows.history.JobDetails;
import com.apriori.pageobjects.workflows.schedule.SchedulePage;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WorkflowHistoryTests extends TestBaseUI {

    private static WorkFlowData workFlowData;
    private UserCredentials currentUser = UserUtil.getUser();
    private WorkflowHome workflowHome;
    private SoftAssertions softAssertions;
    private CIConnectHome ciConnectHome;

    public WorkflowHistoryTests() {
        super();
    }

    @BeforeEach
    public void setup() {
        softAssertions = new SoftAssertions();
        ciConnectHome = new CicLoginPage(driver).login(currentUser);
    }

    @Test
    @TestRail(id = {3589})
    @Description("Test creating, editing and deletion of a worflow")
    public void testStartAndTrackJob() {
        workFlowData = new TestDataService().getTestData("WorkFlowTestData.json", WorkFlowData.class);
        workFlowData.setWorkflowName(GenerateStringUtil.saltString("----0WFC"));
        // CREATE WORK FLOW
        workflowHome = ciConnectHome
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .clickNextBtnInDetailsTab()
            .addRule(workFlowData, this.workFlowData.getQueryDefinitionsData().size())
            .clickWFQueryDefNextBtn()
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
    @TestRail(id = {4317, 4397})
    @Description("Verify start time displayed is based on the browser timezone, " +
        "Locked workflow may not be edited")
    public void testVerifyStartTimeOfWorkflow() {
        workFlowData = new TestDataService().getTestData("WorkFlowTestData.json", WorkFlowData.class);
        workFlowData.setWorkflowName(GenerateStringUtil.saltString("----0WFC"));
        String date = DateUtil.getCurrentDate(DateFormattingUtils.dtf_MMddyyyyHHmmss_hyphen);
        workflowHome = ciConnectHome.clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(workFlowData, this.workFlowData.getQueryDefinitionsData().size())
            .clickWFQueryDefNextBtn()
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

        assertEquals("The job was successfully started!", workflowHome.getWorkFlowStatusMessage());
        softAssertions.assertThat(historyPage.getWorkflowStartedAt(workFlowData.getWorkflowName())).contains(StringUtils.split(date, ":")[0]);

        assertTrue(workflowHome.selectViewHistoryTab().searchAndTrackWorkFlowStatus(workFlowData.getWorkflowName()), "Verify Workflow job is finished");
        JobDetails jobDetails = historyPage.clickViewDetailsButton();
        softAssertions.assertThat(jobDetails.getStartedAtElement().getText()).contains(StringUtils.split(date, ":")[0]);
    }

    @AfterEach
    public void cleanup() {
        softAssertions.assertAll();
        CicApiTestUtil.deleteWorkFlow(this.ciConnectHome.getSession(), CicApiTestUtil.getMatchedWorkflowId(this.workFlowData.getWorkflowName()));
    }
}
