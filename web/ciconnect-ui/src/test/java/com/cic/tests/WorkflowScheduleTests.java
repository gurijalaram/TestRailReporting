package com.cic.tests;

import com.apriori.features.WorkFlowFeatures;
import com.apriori.pagedata.WorkFlowData;
import com.apriori.pages.home.CIConnectHome;
import com.apriori.pages.login.CicLoginPage;
import com.apriori.pages.workflows.WorkflowHome;
import com.apriori.pages.workflows.schedule.details.DetailsPart;
import com.apriori.pages.workflows.schedule.details.WorkflowSchedule;
import com.apriori.pages.workflows.schedule.querydefinitions.QueryDefinitions;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.dataservice.TestDataService;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.CicApiTestUtil;
import utils.WorkflowTestUtil;

public class WorkflowScheduleTests extends WorkflowTestUtil {
    private static final UserCredentials currentUser = UserUtil.getUser();
    private static WorkFlowData workFlowData;
    private WorkflowSchedule workflowSchedule;
    private WorkFlowFeatures workFlowFeatures;
    private CIConnectHome ciConnectHome;
    WorkflowHome workflowHome;
    SoftAssertions softAssertions;

    public WorkflowScheduleTests() {
        super();
    }

    @Before
    public void setup() {
        softAssertions = new SoftAssertions();
        workFlowData = new TestDataService().getTestData("WorkFlowTestData.json", WorkFlowData.class);
        ciConnectHome = new CicLoginPage(driver).login(currentUser);
        workFlowFeatures = ciConnectHome.clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewWorkflowBtn();
    }

    @Test
    @TestRail(testCaseId = {"4083"})
    @Description("Create minutes schedule workflow")
    public void testCreateMinutesScheduledWorkflow() {
        workflowSchedule = WorkflowSchedule.builder()
            .schedule(WorkflowSchedule.Schedule.MINUTES)
            .numberOfMinutes(1)
            .build();

        workflowHome = workFlowFeatures.createScheduledWorkflow(workflowSchedule);
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        workflowHome.closeMessageAlertBox();

        softAssertions.assertThat(workflowHome.selectScheduleTab().isWorkflowExists(workFlowData.getWorkflowName())).isTrue();
        softAssertions.assertThat(workflowHome.selectViewHistoryTab().isScheduledWorkflowInvoked(workFlowData.getWorkflowName())).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"3600"})
    @Description("Create Disabled schedule workflow and workflow does not run")
    public void testCreateDisabledScheduledWorkflow() {
        workflowSchedule = WorkflowSchedule.builder()
            .schedule(WorkflowSchedule.Schedule.MINUTES)
            .numberOfMinutes(1)
            .build();

        workFlowData.setWorkflowName(GenerateStringUtil.saltString(workFlowData.getWorkflowName()));
        QueryDefinitions queryDefinitions = (QueryDefinitions) new DetailsPart(driver).enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .selectEnabledCheckbox("off")
            .setSchedule(workflowSchedule)
            .clickWFDetailsNextBtn();

        workflowHome = queryDefinitions.addRule(workFlowData, workFlowData.getQueryDefinitionsData().size())
            .clickWFQueryDefNextBtn()
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        workflowHome.closeMessageAlertBox();

        softAssertions.assertThat(workflowHome.selectScheduleTab().isWorkflowExists(workFlowData.getWorkflowName())).isTrue();
        softAssertions.assertThat(workflowHome.selectViewHistoryTab().isScheduledWorkflowInvoked(workFlowData.getWorkflowName())).isFalse();
    }

    @Test
    @TestRail(testCaseId = {"3601", "4324", "4325", "4331"})
    @Description("1. Create minutes schedule workflow to invoke in 1 minute " +
        "2. Edit workflow to disable the schedule and verify job is not invoked" +
        "3. Edit workflow to enable the schedule and verify job is invoked")
    public void testVerifyScheduledWorkflowIsInvoked() {
        workflowSchedule = WorkflowSchedule.builder()
            .schedule(WorkflowSchedule.Schedule.MINUTES)
            .numberOfMinutes(2)
            .build();

        workflowHome = workFlowFeatures.createScheduledWorkflow(workflowSchedule);
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        workflowHome.closeMessageAlertBox();

        softAssertions.assertThat(workflowHome.selectScheduleTab().isWorkflowExists(workFlowData.getWorkflowName())).isTrue();

        workflowHome.selectScheduleTab().selectWorkflow(workFlowData.getWorkflowName()).clickEditButton();
        QueryDefinitions queryDefinitions = (QueryDefinitions) new DetailsPart(driver)
            .selectEnabledCheckbox("off")
            .clickWFDetailsNextBtn();

        workflowHome = queryDefinitions.clickWFQueryDefNextBtn()
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition updated!");
        workflowHome.closeMessageAlertBox();
        softAssertions.assertThat(workflowHome.selectViewHistoryTab().isScheduledWorkflowInvoked(workFlowData.getWorkflowName())).isFalse();

        workflowHome.selectScheduleTab().selectWorkflow(workFlowData.getWorkflowName()).clickEditButton();
        queryDefinitions = (QueryDefinitions) new DetailsPart(driver)
            .selectEnabledCheckbox("on")
            .clickWFDetailsNextBtn();

        workflowHome = queryDefinitions.clickWFQueryDefNextBtn()
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition updated!");
        workflowHome.closeMessageAlertBox();

        softAssertions.assertThat(workflowHome.selectViewHistoryTab().isScheduledWorkflowInvoked(workFlowData.getWorkflowName())).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"9113"})
    @Description("Create Hourly schedule workflow")
    public void testCreateHourlyScheduledWorkflow() {
        workflowSchedule = WorkflowSchedule.builder()
            .schedule(WorkflowSchedule.Schedule.HOUR)
            .selectEveryHour(true)
            .numberOfHours(2)
            .build();

        workflowHome = workFlowFeatures.createScheduledWorkflow(workflowSchedule);
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        workflowHome.closeMessageAlertBox();

        softAssertions.assertThat(workflowHome.selectScheduleTab().isWorkflowExists(workFlowData.getWorkflowName())).isEqualTo(true);

        workflowHome.selectScheduleTab().selectWorkflow(workFlowData.getWorkflowName())
            .clickDeleteButton().clickConfirmAlertBoxDelete();

        workflowHome.selectScheduleTab().clickNewWorkflowBtn();

        workflowSchedule = WorkflowSchedule.builder()
            .schedule(WorkflowSchedule.Schedule.HOUR)
            .selectEveryHour(false)
            .startHour("10")
            .startMinutes("10")
            .build();

        workflowHome = workFlowFeatures.createScheduledWorkflow(workflowSchedule);
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        workflowHome.closeMessageAlertBox();

        softAssertions.assertThat(workflowHome.selectScheduleTab().isWorkflowExists(workFlowData.getWorkflowName())).isEqualTo(true);
    }

    @Test
    @TestRail(testCaseId = {"9114"})
    @Description("Create Daily schedule workflow")
    public void testCreateDailyScheduledWorkflow() {
        workflowSchedule = WorkflowSchedule.builder()
            .schedule(WorkflowSchedule.Schedule.DAILY)
            .selectEveryDay(true)
            .numberOfDays(1)
            .startHour("11")
            .startMinutes("15")
            .build();

        workflowHome = workFlowFeatures.createScheduledWorkflow(workflowSchedule);
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        workflowHome.closeMessageAlertBox();

        softAssertions.assertThat(workflowHome.selectScheduleTab().isWorkflowExists(workFlowData.getWorkflowName())).isEqualTo(true);

        workflowHome.selectScheduleTab().selectWorkflow(workFlowData.getWorkflowName())
            .clickDeleteButton().clickConfirmAlertBoxDelete();

        workFlowFeatures = workflowHome.selectScheduleTab().clickNewWorkflowBtn();

        workflowSchedule = WorkflowSchedule.builder()
            .schedule(WorkflowSchedule.Schedule.HOUR)
            .selectEveryDay(false)
            .startHour("10")
            .startMinutes("10")
            .build();

        workflowHome = workFlowFeatures.createScheduledWorkflow(workflowSchedule);
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        workflowHome.closeMessageAlertBox();

        softAssertions.assertThat(workflowHome.selectScheduleTab().isWorkflowExists(workFlowData.getWorkflowName())).isEqualTo(true);
    }

    @Test
    @TestRail(testCaseId = {"9115"})
    @Description("Create Weekly schedule workflow")
    public void testCreateWeeklyScheduledWorkflow() {
        workflowSchedule = WorkflowSchedule.builder()
            .schedule(WorkflowSchedule.Schedule.WEEKLY)
            .weekDay(WorkflowSchedule.WeekDay.FRIDAY)
            .startHour("10")
            .startMinutes("30")
            .build();

        workflowHome = workFlowFeatures.createScheduledWorkflow(workflowSchedule);
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        workflowHome.closeMessageAlertBox();

        softAssertions.assertThat(workflowHome.selectScheduleTab().isWorkflowExists(workFlowData.getWorkflowName())).isEqualTo(true);
    }

    @Test
    @TestRail(testCaseId = {"9116"})
    @Description("Create Monthly schedule workflow")
    public void testCreateMonthlyScheduledWorkflow() {
        workflowSchedule = WorkflowSchedule.builder()
            .schedule(WorkflowSchedule.Schedule.MONTHLY)
            .selectDayInEveryMonth(true)
            .dayOfMonth(9)
            .numberOfMonths(2)
            .startHour("12")
            .startMinutes("20")
            .build();

        workflowHome = workFlowFeatures.createScheduledWorkflow(workflowSchedule);
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        workflowHome.closeMessageAlertBox();

        softAssertions.assertThat(workflowHome.selectScheduleTab().isWorkflowExists(workFlowData.getWorkflowName())).isEqualTo(true);

        workflowHome.selectScheduleTab().selectWorkflow(workFlowData.getWorkflowName())
            .clickDeleteButton().clickConfirmAlertBoxDelete();

        workflowHome.selectScheduleTab().clickNewWorkflowBtn();

        workflowSchedule = WorkflowSchedule.builder()
            .schedule(WorkflowSchedule.Schedule.MONTHLY)
            .monthlyOccurance(WorkflowSchedule.MonthlyOccurance.FIRST)
            .weekDay(WorkflowSchedule.WeekDay.THURSDAY)
            .numberOfMonths(2)
            .startHour("10")
            .startMinutes("10")
            .build();

        workflowHome = workFlowFeatures.createScheduledWorkflow(workflowSchedule);
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        workflowHome.closeMessageAlertBox();

        softAssertions.assertThat(workflowHome.selectScheduleTab().isWorkflowExists(workFlowData.getWorkflowName())).isEqualTo(true);
    }

    @Test
    @TestRail(testCaseId = {"9117"})
    @Description("Create Yearly schedule workflow")
    public void testCreateYearlyScheduledWorkflow() {
        workflowSchedule = WorkflowSchedule.builder()
            .schedule(WorkflowSchedule.Schedule.YEARLY)
            .selectEveryYear(true)
            .month(WorkflowSchedule.Month.AUGUST)
            .dayOfMonth(9)
            .startHour("12")
            .startMinutes("15")
            .build();

        workflowHome = workFlowFeatures.createScheduledWorkflow(workflowSchedule);
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        workflowHome.closeMessageAlertBox();

        softAssertions.assertThat(workflowHome.selectScheduleTab().isWorkflowExists(workFlowData.getWorkflowName())).isEqualTo(true);

        workflowHome.selectScheduleTab().selectWorkflow(workFlowData.getWorkflowName())
            .clickDeleteButton().clickConfirmAlertBoxDelete();

        workflowHome.selectScheduleTab().clickNewWorkflowBtn();

        workflowSchedule = WorkflowSchedule.builder()
            .selectEveryYear(false)
            .monthlyOccurance(WorkflowSchedule.MonthlyOccurance.FIRST)
            .weekDay(WorkflowSchedule.WeekDay.FRIDAY)
            .month(WorkflowSchedule.Month.MARCH)
            .startHour("10")
            .startMinutes("15")
            .build();

        workflowHome = workFlowFeatures.createScheduledWorkflow(workflowSchedule);
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        workflowHome.closeMessageAlertBox();

        softAssertions.assertThat(workflowHome.selectScheduleTab().isWorkflowExists(workFlowData.getWorkflowName())).isEqualTo(true);
    }

    @After
    public void cleanup() {
        CicApiTestUtil.deleteWorkFlow(ciConnectHome.getSession(), CicApiTestUtil.getMatchedWorkflowId(workFlowData.getWorkflowName()));
        softAssertions.assertAll();
    }
}
