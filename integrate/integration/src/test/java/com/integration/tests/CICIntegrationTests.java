package com.integration.tests;

import com.apriori.enums.SortedOrderType;
import com.apriori.enums.WorkflowListColumns;
import com.apriori.nts.email.EmailData;
import com.apriori.nts.email.EmailService;
import com.apriori.nts.excel.ExcelService;
import com.apriori.nts.pdf.PDFDocument;
import com.apriori.nts.reports.componentsummary.MultipleComponentSummary;
import com.apriori.nts.reports.data.ReportData;
import com.apriori.nts.reports.partscost.PartsCost;
import com.apriori.pagedata.WorkFlowData;
import com.apriori.pages.login.LoginPage;
import com.apriori.pages.workflows.WorkflowHome;
import com.apriori.pages.workflows.schedule.SchedulePage;
import com.apriori.pages.workflows.schedule.costinginputs.CostingInputsPart;
import com.apriori.pages.workflows.schedule.notifications.NotificationsPart;
import com.apriori.pages.workflows.schedule.publishresults.PublishResultsPart;
import com.apriori.pages.workflows.schedule.querydefinitions.QueryDefinitions;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import common.testdata.TestDataService;
import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class CICIntegrationTests extends TestBase {

    private UserCredentials currentUser = UserUtil.getUser();
    private static final String emailSubject = "aPriori CI Generate DFM Part Summary";

    public CICIntegrationTests() {
        super();
    }

    @Before
    public void setup() {
    }

    @Test
    @TestRail(testCaseId = {"12045"})
    @Description("Test creating, invoking, tracking and deletion of a workflow")
    public void testCreateAndDeleteWorkflow() {
        WorkFlowData workFlowData = new TestDataService().getTestData("WorkFlowData.json");
        log.info(String.format("Start Creating Workflow >> %s <<", workFlowData.getWorkflowName()));
        SchedulePage schedulePage = new LoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab();

        QueryDefinitions queryDefinitions = (QueryDefinitions) schedulePage
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .clickWFDetailsNextBtn();

        CostingInputsPart costingInputsPart = queryDefinitions.addRule(workFlowData, workFlowData.getQueryDefinitionsData().size())
            .clickWFQueryDefNextBtn();

        NotificationsPart notificationsPart = costingInputsPart.addCostingInputFields(workFlowData.getCostingInputsData().size()).clickCINextBtn();
        PublishResultsPart publishResultsPart = notificationsPart.selectEmailTab().selectEmailTemplate().selectRecipient().clickCINotificationNextBtn();
        WorkflowHome workflowHome = publishResultsPart.selectAttachReportTab().selectReportName().selectCurrencyCode().selectCostRounding().clickSaveButton();
        log.info(String.format("created Workflow confirmation >> %s <<", workflowHome.getWorkFlowStatusMessage()));
        Assert.assertEquals("Job definition created", workflowHome.getWorkFlowStatusMessage());

        Assert.assertTrue("verify workflow is sorted by Name", schedulePage.isWorkflowListIsSorted(WorkflowListColumns.Name, SortedOrderType.ASCENDING, workFlowData.getWorkflowName()));

        log.info(String.format("Delete Workflow >> %s <<", workFlowData.getWorkflowName()));
        Assert.assertTrue("verify workflow is deleted", schedulePage.deleteWorkFlow(workFlowData.getWorkflowName()));
    }

    @Test
    @TestRail(testCaseId = {"12046"})
    @Description("Create Workflow, Invoke workflow, verify Parts Cost watchpoint report from email and delete workflow")
    public void testVerifyWatchPointReport() {
        SoftAssertions softAssertions = new SoftAssertions();
        WorkFlowData workFlowData = new TestDataService().getTestData("WatchPointReportData.json");
        PartsCost xlsWatchPointReportExpectedData = ReportData.getExpectedData("testdata/PartCostReport.json", PartsCost.class);
        EmailService emailService = new EmailService();
        WorkflowHome workflowHome = new LoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData);

        if (workflowHome.selectScheduleTab().isWorkflowExists("- - - 0 0 Auto_WPR_DoNotDelete")) {
            // SELECT AND INVOKE WORKFLOW
            workflowHome = workflowHome.selectScheduleTab().selectWorkflowAndInvoke("- - - 0 0 Auto_WPR_DoNotDelete");
            Assert.assertEquals("The job was successfully started!", workflowHome.getWorkFlowStatusMessage());
            Assert.assertTrue("verify Job status is finished", workflowHome.selectViewHistoryTab().searchAndTrackWorkFlowStatus("- - - 0 0 Auto_WPR_DoNotDelete"));
        } else {
            log.info(String.format("Start Creating Workflow for CIR report>> %s <<", workFlowData.getWorkflowName()));
            QueryDefinitions queryDefinitions = (QueryDefinitions) workflowHome.selectScheduleTab()
                .clickNewButton()
                .enterWorkflowNameField(workFlowData.getWorkflowName())
                .selectWorkflowConnector(workFlowData.getConnectorName())
                .clickWFDetailsNextBtn();

            workflowHome = queryDefinitions.addRule(workFlowData, workFlowData.getQueryDefinitionsData().size())
                .clickWFQueryDefNextBtn()
                .clickCINextBtn()
                .selectEmailTab()
                .selectEmailTemplate()
                .selectRecipient()
                .selectAttachReport()
                .selectReportName()
                .clickCINotificationNextBtn()
                .clickSaveButton();

            Assert.assertEquals("Job definition created", workflowHome.getWorkFlowStatusMessage());

            Assert.assertTrue("verify workflow is sorted by Name", workflowHome.selectScheduleTab().isWorkflowListIsSorted(WorkflowListColumns.Name, SortedOrderType.ASCENDING, workFlowData.getWorkflowName()));

            // SELECT AND INVOKE WORKFLOW
            workflowHome = workflowHome.selectScheduleTab().selectWorkflowAndInvoke(workFlowData.getWorkflowName());

            Assert.assertEquals("The job was successfully started!", workflowHome.getWorkFlowStatusMessage());
            Assert.assertTrue("verify Job status is finished", workflowHome.selectViewHistoryTab().searchAndTrackWorkFlowStatus(workFlowData.getWorkflowName()));

            //DELETE WORKFLOW
            workflowHome.selectScheduleTab().deleteWorkFlow(workFlowData.getWorkflowName());
        }

        // Read the email and verify content and attached watch point report
        Assert.assertTrue("Validate Report Email", emailService.validateEmail(emailSubject));

        EmailData emailMessage = emailService.getEmailMessageData(emailSubject);
        ExcelService excelReport = emailMessage.getAttachments();

        Assert.assertEquals(7, excelReport.getSheetCount());
        Assert.assertTrue("Verify the field name 'PART NUMBER' in the report", excelReport.getFirstCellRowNum("Part Number") > 0);
        Assert.assertTrue("Verify the title of the report", excelReport.getSheetNames().contains(xlsWatchPointReportExpectedData.getPartCostReport().getTitle()));

        emailService.markForDelete(emailMessage.getMessage());

    }

    @Test
    @TestRail(testCaseId = {"12046"})
    @Description("Create Workflow, Invoke workflow, verify CIR report from email and delete workflow")
    public void testVerifyCIRReport() {
        WorkFlowData workFlowData = new TestDataService().getTestData("CIRReportData.json");
        MultipleComponentSummary pdfExpectedReportData = ReportData.getExpectedData("testdata/MultipleComponentsSummary.json", MultipleComponentSummary.class);
        EmailService emailService = new EmailService();
        WorkflowHome workflowHome;

        workflowHome = new LoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData);

        if (workflowHome.selectScheduleTab().isWorkflowExists("- - - 0 0 Auto_CIR_DoNotDelete")) {
            workflowHome = workflowHome.selectScheduleTab().selectWorkflowAndInvoke("- - - 0 0 Auto_CIR_DoNotDelete");
            Assert.assertEquals("The job was successfully started!", workflowHome.getWorkFlowStatusMessage());
            Assert.assertTrue("verify Job status is finished", workflowHome.selectViewHistoryTab().searchAndTrackWorkFlowStatus("- - - 0 0 Auto_CIR_DoNotDelete"));
        } else {
            log.info(String.format("Start Creating Workflow for CIR report>> %s <<", workFlowData.getWorkflowName()));
            QueryDefinitions queryDefinitions = (QueryDefinitions) workflowHome.selectScheduleTab()
                .clickNewButton()
                .enterWorkflowNameField(workFlowData.getWorkflowName())
                .selectWorkflowConnector(workFlowData.getConnectorName())
                .clickWFDetailsNextBtn();

            workflowHome = queryDefinitions.addRule(workFlowData, workFlowData.getQueryDefinitionsData().size())
                .clickWFQueryDefNextBtn()
                .clickCINextBtn()
                .selectEmailTab()
                .selectEmailTemplate()
                .selectRecipient()
                .selectAttachReport()
                .selectReportName()
                .clickCINotificationNextBtn()
                .clickSaveButton();

            Assert.assertEquals("Job definition created", workflowHome.getWorkFlowStatusMessage());

            Assert.assertTrue("verify workflow is sorted by Name", workflowHome.selectScheduleTab().isWorkflowListIsSorted(WorkflowListColumns.Name, SortedOrderType.ASCENDING, workFlowData.getWorkflowName()));

            // SELECT AND INVOKE WORKFLOW
            workflowHome = workflowHome.selectScheduleTab().selectWorkflowAndInvoke(workFlowData.getWorkflowName());

            Assert.assertEquals("The job was successfully started!", workflowHome.getWorkFlowStatusMessage());

            Assert.assertTrue("verify Job status is finished", workflowHome.selectViewHistoryTab().searchAndTrackWorkFlowStatus(workFlowData.getWorkflowName()));

            //DELETE WORKFLOW
            workflowHome.selectScheduleTab().deleteWorkFlow(workFlowData.getWorkflowName());
        }
        // Read the email and verify content and attached watch point report
        EmailData emailMessage = emailService.getEmailMessageData(emailSubject);
        PDFDocument pdfDocument = emailMessage.getAttachments();

        Assert.assertTrue("Verify Email Content Title >>aPriori Cost Insight Generate Notification<<", emailMessage.getContent().contains("aPriori Cost Insight Generate Notification"));
        Assert.assertTrue("Verify PDF report title >>DFM Multiple Components Summary Report<<", pdfDocument.getDocumentContents().contains("DFM Multiple Components Summary Report"));
        Assert.assertTrue("Verify PDF report field Cost Metric >>Fully Burdened Cost<<", pdfDocument.getDocumentContents().contains(pdfExpectedReportData.getCostMetric()));
        Assert.assertTrue("Verify PDF report field Scenario Name >>CIG_<<", pdfDocument.getDocumentContents().contains(pdfExpectedReportData.getScenarioName()));
        emailService.markForDelete(emailMessage.getMessage());
    }
}
