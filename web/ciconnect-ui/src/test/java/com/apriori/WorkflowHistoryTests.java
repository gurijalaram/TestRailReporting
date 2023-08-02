package com.apriori;

import com.apriori.dataservice.TestDataService;
import com.apriori.pagedata.WorkFlowData;
import com.apriori.pageobjects.login.CicLoginPage;
import com.apriori.pageobjects.workflows.WorkflowHome;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WorkflowHistoryTests extends TestBaseUI {

    private UserCredentials currentUser = UserUtil.getUser();
    private static WorkFlowData workFlowData;
    WorkflowHome workflowHome;

    public WorkflowHistoryTests() {
        super();
    }

    @BeforeEach
    public void setup() {
    }

    @Test
    @TestRail(id = {3589})
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
