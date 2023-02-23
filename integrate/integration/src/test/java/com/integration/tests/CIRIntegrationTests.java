package com.integration.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import com.apriori.enums.ReportsEnum;
import com.apriori.nts.reports.componentsummary.MultipleComponentSummary;
import com.apriori.pageobjects.header.ReportsHeader;
import com.apriori.pageobjects.pages.login.ReportsLoginPage;
import com.apriori.utils.TestRail;
import com.apriori.utils.dataservice.TestDataService;
import com.apriori.utils.email.GraphEmailService;
import com.apriori.utils.email.response.EmailMessage;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.pdf.PDFDocument;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import entity.request.JobDefinition;
import entity.response.AgentWorkflow;
import entity.response.AgentWorkflowJobRun;
import entity.response.ReportTemplatesRow;
import enums.CICReportType;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.CicApiTestUtil;

public class CIRIntegrationTests extends TestBase {

    private static String loginSession;
    UserCredentials currentUser = UserUtil.getUser();
    private static AgentWorkflow agentWorkflowResponse;
    private static JobDefinition jobDefinitionData;
    private static AgentWorkflowJobRun agentWorkflowJobRunResponse;
    private static String workflowName;
    private static String scenarioName;
    private static String workflowData;
    private static SoftAssertions softAssertions;
    private static ReportTemplatesRow reportTemplateNames;

    public CIRIntegrationTests() {
        super();
    }

    @Before
    public void setup() {
        softAssertions = new SoftAssertions();
        jobDefinitionData = new TestDataService().getTestData("CicGuiDeleteJobDefData.json", JobDefinition.class);
        String randomNumber = RandomStringUtils.randomNumeric(6);
        workflowName = "CIC_REPORT" + randomNumber;
        scenarioName = PropertiesContext.get("customer") + randomNumber;
    }

    @Test
    @TestRail(testCaseId = {"12046"})
    @Description("Verify user can login CIR")
    public void testUserLoginCIR() {
        ReportsHeader reportsHeader = new ReportsLoginPage(driver)
            .login()
            .navigateToHomePage();

        assertThat(reportsHeader.getHomeTitleText(), CoreMatchers.is(containsString("Home")));
    }

    @Test
    @TestRail(testCaseId = {"12046"})
    @Description("Create Workflow, Invoke workflow, verify CIR report from email and delete workflow")
    public void testVerifyCIRReport() {
        loginSession = CicApiTestUtil.getLoginSession(currentUser, driver);
        reportTemplateNames = CicApiTestUtil.getAgentReportTemplate(ReportsEnum.DTC_MULTIPLE_COMPONENT_SUMMARY, CICReportType.EMAIL, loginSession);
        workflowData = String.format(CicApiTestUtil.getWorkflowData("CIRReportData.json"), CicApiTestUtil.getCustomerName(), CicApiTestUtil.getAgent(),
            workflowName, scenarioName, reportTemplateNames.getValue());

        //Create a Workflow
        MultipleComponentSummary pdfExpectedReportData = new TestDataService().getReportData("MultipleComponentsSummary.json", MultipleComponentSummary.class);
        ResponseWrapper<String> responseWrapper = CicApiTestUtil.CreateWorkflow(loginSession, workflowData);
        softAssertions.assertThat(responseWrapper.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(responseWrapper.getBody()).contains(">true<");
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowName);
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        // verify workflow job is finished
        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        // Read the email and verify content and attached watch point report
        EmailMessage emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        softAssertions.assertThat(emailMessage).isNotNull();
        PDFDocument pdfDocument = emailMessage.emailMessageAttachment().getFileAttachment();

        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains("aPriori Cost Insight Generate Notification");
        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains("DFM Multiple Components Summary Report");
        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains(pdfExpectedReportData.getCostMetric());
        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains(scenarioName);
        emailMessage.deleteEmailMessage();

        // Delete the workflow
        jobDefinitionData.setJobDefinition(CicApiTestUtil.getMatchedWorkflowId(workflowName).getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(loginSession, jobDefinitionData);
    }

    @After
    public void cleanup() {
        softAssertions.assertAll();
    }
}
