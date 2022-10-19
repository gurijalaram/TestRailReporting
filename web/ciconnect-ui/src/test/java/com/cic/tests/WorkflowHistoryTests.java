package com.cic.tests;

import com.apriori.pagedata.WorkFlowData;
import com.apriori.pages.login.CicLoginPage;
import com.apriori.pages.workflows.WorkflowHome;
import com.apriori.utils.TestRail;
import com.apriori.utils.dataservice.TestDataService;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

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
        workFlowData = new TestDataService().getTestData("WorkFlowTestData.json",WorkFlowData.class);
        workFlowData.setWorkflowName("- - - 0 0 Auto_WF_DoNotDelete");
        workflowHome = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .selectWorkflowAndInvoke(workFlowData.getWorkflowName());

        Assert.assertEquals("The job was successfully started!", workflowHome.getWorkFlowStatusMessage());

        Assert.assertTrue("Verify Workflow job is finished",workflowHome.selectViewHistoryTab().searchAndTrackWorkFlowStatus(workFlowData.getWorkflowName()));
    }
}
