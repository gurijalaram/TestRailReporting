package com.apriori.cic.ui.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cic.api.enums.PlmPartDataType;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.ui.enums.RuleOperatorEnum;
import com.apriori.cic.ui.features.WorkFlowFeatures;
import com.apriori.cic.ui.pagedata.WorkFlowData;
import com.apriori.cic.ui.pageobjects.home.CIConnectHome;
import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;
import com.apriori.cic.ui.pageobjects.workflows.schedule.details.DetailsPart;
import com.apriori.cic.ui.pageobjects.workflows.schedule.details.WorkflowSchedule;
import com.apriori.cic.ui.pageobjects.workflows.schedule.querydefinitions.QueryDefinitions;
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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class WorkflowScheduleTests extends TestBaseUI {
    private static WorkFlowData workFlowData;
    private UserCredentials currentUser;
    private WorkflowSchedule workflowSchedule;
    private WorkFlowFeatures workFlowFeatures;
    private CIConnectHome ciConnectHome;
    private WorkflowHome workflowHome;
    private SoftAssertions softAssertions;

    public WorkflowScheduleTests() {
        super();
    }

    @BeforeEach
    public void setup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
        workFlowData = new TestDataService().getTestData("WorkFlowTestData.json", WorkFlowData.class);
        String scenarioName = "AUTO_SN" + new GenerateStringUtil().getRandomNumbers();
        workFlowData.getQueryDefinitionsData().get(0).setFieldName(PlmTypeAttributes.PLM_PART_NUMBER.getCicGuiField());
        workFlowData.getQueryDefinitionsData().get(0).setFieldOperator(RuleOperatorEnum.EQUAL.getRuleOperator());
        workFlowData.getQueryDefinitionsData().get(0).setFieldValue(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber());
        ciConnectHome = new CicLoginPage(driver).login(currentUser);
        workFlowFeatures = ciConnectHome.clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewWorkflowBtn();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {4083})
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
    @TestRail(id = {3600})
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

        workflowHome = queryDefinitions.addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber())
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
    @TestRail(id = {3601, 4324, 4325, 4331, 4869})
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
    @TestRail(id = {9113})
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
    @TestRail(id = {9114})
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
    @TestRail(id = {9115})
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
    @TestRail(id = {9116})
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
    @TestRail(id = {9117})
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

    @Test
    @TestRail(id = {4084})
    @Description("Test Workflow Schedule UI Options")
    public void testALLScheduleTypesWorkflow() {
        DetailsPart detailsPart = new DetailsPart(driver).enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .selectEnabledCheckbox("on");

        detailsPart = detailsPart.setSchedule(WorkflowSchedule.builder()
            .schedule(WorkflowSchedule.Schedule.MINUTES)
            .numberOfMinutes(1)
            .build());

        softAssertions.assertThat(detailsPart.getMinutesInput().isDisplayed()).isTrue();

        detailsPart = detailsPart.setSchedule(WorkflowSchedule.builder()
            .schedule(WorkflowSchedule.Schedule.HOUR)
            .selectEveryHour(false)
            .startHour("10")
            .startMinutes("10")
            .build());

        softAssertions.assertThat(detailsPart.getEveryHour().isDisplayed()).isTrue();

        detailsPart = detailsPart.setSchedule(WorkflowSchedule.builder()
            .schedule(WorkflowSchedule.Schedule.DAILY)
            .selectEveryDay(true)
            .numberOfDays(1)
            .startHour("11")
            .startMinutes("15")
            .build());

        softAssertions.assertThat(detailsPart.getDaysInput().isDisplayed()).isTrue();

        detailsPart = detailsPart.setSchedule(WorkflowSchedule.builder()
            .schedule(WorkflowSchedule.Schedule.WEEKLY)
            .weekDay(WorkflowSchedule.WeekDay.FRIDAY)
            .startHour("10")
            .startMinutes("30")
            .build());

        softAssertions.assertThat(detailsPart.getWeeklyMinutes().isDisplayed()).isTrue();

        detailsPart = detailsPart.setSchedule(WorkflowSchedule.builder()
            .schedule(WorkflowSchedule.Schedule.MONTHLY)
            .selectDayInEveryMonth(true)
            .dayOfMonth(9)
            .numberOfMonths(2)
            .startHour("12")
            .startMinutes("20")
            .build());

        softAssertions.assertThat(detailsPart.getEveryMonth().isDisplayed()).isTrue();

        detailsPart = detailsPart.setSchedule(WorkflowSchedule.builder()
            .schedule(WorkflowSchedule.Schedule.YEARLY)
            .selectEveryYear(true)
            .month(WorkflowSchedule.Month.AUGUST)
            .dayOfMonth(9)
            .startHour("12")
            .startMinutes("15")
            .build());

        softAssertions.assertThat(detailsPart.getYearInput().isDisplayed()).isTrue();
    }

    @AfterEach
    public void cleanup() {
        softAssertions.assertAll();
        CicApiTestUtil.deleteWorkFlow(ciConnectHome.getSession(), CicApiTestUtil.getMatchedWorkflowId(workFlowData.getWorkflowName()));
    }
}
