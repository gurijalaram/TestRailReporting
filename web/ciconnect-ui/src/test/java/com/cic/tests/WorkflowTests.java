package com.cic.tests;

import com.apriori.enums.SortedOrderType;
import com.apriori.enums.WorkflowListColumns;
import com.apriori.features.WorkFlowFeatures;
import com.apriori.pagedata.WorkFlowData;
import com.apriori.pages.login.CicLoginPage;
import com.apriori.pages.workflows.WorkflowHome;
import com.apriori.pages.workflows.schedule.SchedulePage;
import com.apriori.pages.workflows.schedule.details.DetailsPart;
import com.apriori.pages.workflows.schedule.querydefinitions.QueryDefinitions;
import com.apriori.utils.StringUtils;
import com.apriori.utils.TestRail;
import com.apriori.utils.dataservice.TestDataService;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WorkflowTests extends TestBase {
    private UserCredentials currentUser = UserUtil.getUser();
    private static WorkFlowData workFlowData;
    WorkflowHome workflowHome;
    SoftAssertions softAssertions;

    public WorkflowTests() {
        super();
    }

    @Before
    public void setup() {
    }

    @Test
    @TestRail(testCaseId = {"4109", "3586", "3588", "3587", "3591","3961"})
    @Description("Test creating, editing and deletion of a workflow")
    public void testCreateEditAndDeleteWorkflow() {
        softAssertions = new SoftAssertions();
        workFlowData = new TestDataService().getTestData("WorkFlowTestData.json",WorkFlowData.class);
        // CREATE WORK FLOW
        WorkFlowFeatures workFlowFeatures = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewWorkflowBtn();
        workflowHome = workFlowFeatures.createWorkflow();
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        workflowHome.closeMessageAlertBox();

        // EDIT WORK FLOW
        workflowHome.selectScheduleTab()
            .selectWorkflow(workFlowData.getWorkflowName())
            .clickEditButton();
        workFlowData.setWorkflowName(StringUtils.saltString("- - - 0 0 Auto_Upd"));
        workflowHome = workFlowFeatures.editWorkflow();
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition updated!");
        workflowHome.closeMessageAlertBox();
        workFlowData.setWorkflowName(workFlowData.getWorkflowName());
        softAssertions.assertThat(workflowHome.selectScheduleTab().getItemFromWorkflowList(workFlowData.getWorkflowName(), WorkflowListColumns.Last_Modified_By).getText()).isEqualTo(currentUser.getEmail());
        //DELETE WORKFLOW
        SchedulePage schedulePage = workflowHome.selectScheduleTab().selectWorkflow(workFlowData.getWorkflowName())
            .clickDeleteButton().clickConfirmAlertBoxDelete();
        softAssertions.assertThat(schedulePage.isWorkflowExists(workFlowData.getWorkflowName())).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"4273"})
    @Description("Test the state of the edit, delete and invoke buttons on the WF schedule screen. With a WF selected" +
        "and with no WF selected")
    public void testButtonState() {
        softAssertions = new SoftAssertions();
        workFlowData = new TestDataService().getTestData("WorkFlowTestData.json",WorkFlowData.class);
        // CREATE WORK FLOW
        QueryDefinitions queryDefinitions = (QueryDefinitions) new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .clickWFDetailsNextBtn();

        workflowHome = queryDefinitions.addRule(workFlowData, this.workFlowData.getQueryDefinitionsData().size())
            .clickWFQueryDefNextBtn()
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");

        SchedulePage schedulePage = workflowHome.selectScheduleTab();

        //Verify Schedule Page -> WorkFlow Edit and Delete button is in disabled mode
        softAssertions.assertThat(schedulePage.getEditWorkflowButton().isEnabled()).isEqualTo(false);
        softAssertions.assertThat(schedulePage.getDeleteWorkflowButton().isEnabled()).isEqualTo(false);

        schedulePage.selectWorkflow(workFlowData.getWorkflowName());

        //Verify Schedule Page -> WorkFlow Edit and Delete button is in enabled mode
        softAssertions.assertThat(schedulePage.getEditWorkflowButton().isEnabled()).isEqualTo(true);
        softAssertions.assertThat(schedulePage.getDeleteWorkflowButton().isEnabled()).isEqualTo(true);

        //DELETE WORKFLOW
        schedulePage = workflowHome.selectScheduleTab().selectWorkflow(workFlowData.getWorkflowName())
            .clickDeleteButton().clickConfirmAlertBoxDelete();
        softAssertions.assertThat(schedulePage.isWorkflowExists(workFlowData.getWorkflowName())).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"3809", "3944"})
    @Description("Test default sorting, ascending and descending of workflows by name in the schedule table")
    public void testSortedByName() {
        softAssertions = new SoftAssertions();
        workFlowData = new TestDataService().getTestData("WorkFlowTestData.json",WorkFlowData.class);
        // CREATE WORK FLOW
        QueryDefinitions queryDefinitions = (QueryDefinitions) new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .clickWFDetailsNextBtn();

        workflowHome = queryDefinitions.addRule(workFlowData, this.workFlowData.getQueryDefinitionsData().size())
            .clickWFQueryDefNextBtn()
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

        //DELETE WORKFLOW
        schedulePage = workflowHome.selectScheduleTab().selectWorkflow(workFlowData.getWorkflowName())
            .clickDeleteButton().clickConfirmAlertBoxDelete();
        softAssertions.assertThat(schedulePage.isWorkflowExists(workFlowData.getWorkflowName())).isEqualTo(false);
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"4302"})
    public void testValidateInputFields() {
        softAssertions = new SoftAssertions();
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
        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"3991"})
    public void testValidateNameWithSpecial() {
        DetailsPart detailsPart = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton();
        detailsPart.enterWorkflowNameField("!@#$!@#$");
        Assert.assertEquals("verify Workflow name field with special characters", "Name should only contain spaces and the following characters: a-zA-Z0-9-_", detailsPart.getWorkflowNameErrorLbl().getText());

        detailsPart.enterWorkflowNameField("w12345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Assert.assertEquals("verify Workflow name field with special characters", "Name must be less than or equal to 64 characters.", detailsPart.getWorkflowNameErrorLbl().getText());
    }

    @After
    public void cleanup() {
    }
}
