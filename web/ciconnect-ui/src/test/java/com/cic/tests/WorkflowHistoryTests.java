package com.cic.tests;

import com.apriori.pagedata.WorkFlowData;
import com.apriori.pages.login.LoginPage;
import com.apriori.pages.workflows.WorkflowHome;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import common.testdata.TestDataService;
import io.qameta.allure.Description;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WorkflowHistoryTests extends TestBase {


    private UserCredentials currentUser = UserUtil.getUser();
    private static WorkFlowData workFlowData;
    WorkflowHome workflowHome;

    public WorkflowHistoryTests() {
        super();
    }

    @Before
    public void setup() {
    }

    @Test
    @TestRail(testCaseId = {"3589"})
    @Description("Test creating, editing and deletion of a worflow")
    public void testStartAndTrackJob() {
        workFlowData = new TestDataService().getTestData();
        workFlowData.setWorkflowName("0 0 Auto Workflow01");
        workflowHome = new LoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .selectWorkflowAndInvoke(workFlowData.getWorkflowName());

        Assert.assertEquals("The job was successfully started!", workflowHome.getWorkFlowStatusMessage());

        workflowHome.selectViewHistoryTab().searchAndTrackWorkFlowStatus(workFlowData.getWorkflowName());


    }
}
