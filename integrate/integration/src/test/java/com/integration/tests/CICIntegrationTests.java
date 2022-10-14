package com.integration.tests;

import com.apriori.enums.SortedOrderType;
import com.apriori.enums.WorkflowListColumns;
import com.apriori.nts.email.EmailData;
import com.apriori.nts.email.EmailService;
import com.apriori.nts.excel.ExcelService;
import com.apriori.nts.pdf.PDFDocument;
import com.apriori.nts.reports.componentsummary.MultipleComponentSummary;
import com.apriori.nts.reports.partscost.PartsCost;
import com.apriori.pagedata.WorkFlowData;
import com.apriori.pages.login.LoginPage;
import com.apriori.pages.workflows.WorkflowHome;
import com.apriori.pages.workflows.schedule.SchedulePage;
import com.apriori.pages.workflows.schedule.costinginputs.CostingInputsPart;
import com.apriori.pages.workflows.schedule.notifications.NotificationsPart;
import com.apriori.pages.workflows.schedule.publishresults.PublishResultsPart;
import com.apriori.pages.workflows.schedule.querydefinitions.QueryDefinitions;
import com.apriori.utils.StringUtils;
import com.apriori.utils.TestRail;
import com.apriori.utils.dataservice.TestDataService;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import entity.request.workflow.JobDefinition;
import entity.response.AgentWorkflow;
import entity.response.AgentWorkflowJobRun;
import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.CicApiTestUtil;

@Slf4j
public class CICIntegrationTests extends TestBase {

    private static String loginSession;
    UserCredentials currentUser = UserUtil.getUser();
    private static AgentWorkflow agentWorkflowResponse;
    private static JobDefinition jobDefinitionData;
    private static ResponseWrapper<AgentWorkflowJobRun> agentWorkflowJobRunResponse;
    private static String workflowName;
    private static String scenarioName;
    private static String workflowData;
    private static final String emailSubject = "aPriori CI Generate DFM Part Summary";

    public CICIntegrationTests() {
        super();
    }

    @Before
    public void setup() {
        jobDefinitionData = new TestDataService().getTestData("CicGuiDeleteJobDefData.json", JobDefinition.class);
        String randomNumber = RandomStringUtils.randomNumeric(6);
        workflowName = "CIC_REPORT" + randomNumber;
        scenarioName = PropertiesContext.get("customer") + randomNumber;
    }

    @Test
    @TestRail(testCaseId = {"12045"})
    @Description("Test creating, invoking, tracking and deletion of a workflow")
    public void testCreateAndDeleteWorkflow() {
        WorkFlowData workFlowData = new TestDataService().getTestData("WorkFlowData.json", WorkFlowData.class);
        workFlowData.setWorkflowName(StringUtils.saltString(workFlowData.getWorkflowName()));
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
        EmailService emailService = new EmailService();
        workflowData = String.format(CicApiTestUtil.getWorkflowData("WatchPointReportData.json"), CicApiTestUtil.getCustomerName(), CicApiTestUtil.getAgent(), workflowName, scenarioName);
        // Create WorkFlow
        PartsCost xlsWatchPointReportExpectedData = new TestDataService().getReportData("PartCostReport.json", PartsCost.class);
        loginSession = CicApiTestUtil.getLoginSession(currentUser, driver);
        ResponseWrapper<String> responseWrapper = CicApiTestUtil.CreateWorkflow(loginSession, workflowData);
        softAssertions.assertThat(responseWrapper.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(responseWrapper.getBody()).contains(">true<");
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowName);
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId());
        softAssertions.assertThat(agentWorkflowJobRunResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getResponseEntity().getJobId()).isNotNull();

        // Verify workflow job is finished.
        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getResponseEntity().getJobId())).isTrue();

        // Delete the workflow
        jobDefinitionData.setJobDefinition(CicApiTestUtil.getMatchedWorkflowId(workflowName).getId() + "_Job");
        ResponseWrapper<String> deleteWorkflowResponse = CicApiTestUtil.deleteWorkFlow(loginSession, jobDefinitionData);
        Assert.assertEquals("Verify Workflow is deleted", deleteWorkflowResponse.getStatusCode(), HttpStatus.SC_OK);

        //Verify Email Notification
        EmailData emailMessage = emailService.getEmailMessageData(emailSubject, scenarioName);
        softAssertions.assertThat(emailMessage).isNotNull();
        ExcelService excelReport = emailMessage.getAttachments();
        softAssertions.assertThat(excelReport).isNotNull();
        softAssertions.assertThat(excelReport.getSheetCount()).isEqualTo(7);
        softAssertions.assertThat(excelReport.getFirstCellRowNum("Part Number")).isGreaterThan(0);
        softAssertions.assertThat(excelReport.getSheetNames()).contains(xlsWatchPointReportExpectedData.getPartCostReport().getTitle());
        emailService.markForDelete(emailMessage.getMessage());

        softAssertions.assertAll();
    }

    @Test
    @TestRail(testCaseId = {"12046"})
    @Description("Create Workflow, Invoke workflow, verify CIR report from email and delete workflow")
    public void testVerifyCIRReport() {
        SoftAssertions softAssertions = new SoftAssertions();
        EmailService emailService = new EmailService();
        workflowData = String.format(CicApiTestUtil.getWorkflowData("CIRReportData.json"), CicApiTestUtil.getCustomerName(), CicApiTestUtil.getAgent(), workflowName, scenarioName);

        //Create a Workflow
        MultipleComponentSummary pdfExpectedReportData = new TestDataService().getReportData("MultipleComponentsSummary.json", MultipleComponentSummary.class);
        loginSession = CicApiTestUtil.getLoginSession(currentUser, driver);
        ResponseWrapper<String> responseWrapper = CicApiTestUtil.CreateWorkflow(loginSession, workflowData);
        softAssertions.assertThat(responseWrapper.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(responseWrapper.getBody()).contains(">true<");
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowName);
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId());
        softAssertions.assertThat(agentWorkflowJobRunResponse.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getResponseEntity().getJobId()).isNotNull();

        // verify workflow job is finished
        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getResponseEntity().getJobId())).isTrue();

        // Delete the workflow
        jobDefinitionData.setJobDefinition(CicApiTestUtil.getMatchedWorkflowId(workflowName).getId() + "_Job");
        ResponseWrapper<String> deleteWorkflowResponse = CicApiTestUtil.deleteWorkFlow(loginSession, jobDefinitionData);
        Assert.assertEquals("Verify Workflow is deleted", deleteWorkflowResponse.getStatusCode(), HttpStatus.SC_OK);

        // Read the email and verify content and attached watch point report
        EmailData emailMessage = emailService.getEmailMessageData(emailSubject, scenarioName);
        PDFDocument pdfDocument = emailMessage.getAttachments();

        softAssertions.assertThat(emailMessage.getContent()).contains("aPriori Cost Insight Generate Notification");
        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains("DFM Multiple Components Summary Report");
        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains(pdfExpectedReportData.getCostMetric());
        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains(scenarioName);
        emailService.markForDelete(emailMessage.getMessage());

        softAssertions.assertAll();
    }

    @After
    public void cleanup() {

    }
}
