package com.integration.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.PDFDocument;
import com.apriori.TestBaseUI;
import com.apriori.authorization.response.EmailMessage;
import com.apriori.cic.enums.CICReportType;
import com.apriori.cic.enums.ReportsEnum;
import com.apriori.cic.models.request.JobDefinition;
import com.apriori.cic.models.response.AgentWorkflow;
import com.apriori.cic.models.response.AgentWorkflowJobRun;
import com.apriori.cic.models.response.AgentWorkflowReportTemplates;
import com.apriori.cic.models.response.ReportTemplatesRow;
import com.apriori.cic.utils.CicApiTestUtil;
import com.apriori.cic.utils.CicLoginUtil;
import com.apriori.dataservice.TestDataService;
import com.apriori.email.GraphEmailService;
import com.apriori.nts.reports.componentsummary.MultipleComponentSummary;
import com.apriori.pageobjects.header.ReportsHeader;
import com.apriori.pageobjects.login.ReportsLoginPage;
import com.apriori.properties.PropertiesContext;
import com.apriori.reader.file.user.UserCredentials;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CIRIntegrationTests extends TestBaseUI {

    private static String loginSession;
    private static AgentWorkflow agentWorkflowResponse;
    private static JobDefinition jobDefinitionData;
    private static AgentWorkflowJobRun agentWorkflowJobRunResponse;
    private static String workflowName;
    private static String scenarioName;
    private static String workflowData;
    private static SoftAssertions softAssertions;
    private static AgentWorkflowReportTemplates reportTemplateNames;
    private static ReportTemplatesRow reportTemplateName;
    UserCredentials currentUser = UserUtil.getUser();

    public CIRIntegrationTests() {
        super();
    }

    @BeforeEach
    public void setup() {
        softAssertions = new SoftAssertions();
        jobDefinitionData = new TestDataService().getTestData("CicGuiDeleteJobDefData.json", JobDefinition.class);
        String randomNumber = RandomStringUtils.randomNumeric(6);
        workflowName = "CIC_REPORT" + randomNumber;
        scenarioName = PropertiesContext.get("customer") + randomNumber;
    }

    @Test
    @TestRail(id = {12046})
    @Description("Verify user can login CIR")
    public void testUserLoginCIR() {
        ReportsHeader reportsHeader = new ReportsLoginPage(driver)
            .login()
            .navigateToHomePage();

        assertThat(reportsHeader.getHomeTitleText(), CoreMatchers.is(containsString("Home")));
    }

    @Test
    @Issue("DEVOPS-3166")
    @TestRail(id = {12046})
    @Description("Create Workflow, Invoke workflow, verify CIR report from email and delete workflow")
    public void testVerifyCIRReport() {
        loginSession = new CicLoginUtil(driver).login(currentUser).navigateToUserMenu().getWebSession();
        reportTemplateNames = CicApiTestUtil.getAgentReportTemplates(CICReportType.EMAIL, loginSession);
        workflowData = String.format(CicApiTestUtil.getWorkflowData("CIRReportData.json"), CicApiTestUtil.getCustomerName(), CicApiTestUtil.getAgent(loginSession),
            workflowName, scenarioName,
            CicApiTestUtil.getAgentReportTemplate(reportTemplateNames, ReportsEnum.DTC_MULTIPLE_COMPONENT_SUMMARY).getValue(),
            CicApiTestUtil.getAgentReportTemplate(reportTemplateNames, ReportsEnum.DTC_COMPONENT_SUMMARY).getValue());

        //Create a Workflow
        MultipleComponentSummary pdfExpectedReportData = new TestDataService().getReportData("MultipleComponentsSummary.json", MultipleComponentSummary.class);
        CicApiTestUtil.createWorkflow(loginSession, workflowData);
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowName);
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        // verify workflow job is finished
        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        // Read the email and verify content and attached watch point report
        EmailMessage emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        softAssertions.assertThat(emailMessage.getBody().getContent()).contains(scenarioName);
        softAssertions.assertThat(emailMessage.getBody().getContent()).contains("aPriori Cost Insight Generate Notification");
        PDFDocument pdfDocument = (PDFDocument) emailMessage.emailMessageAttachment().getFileAttachment();
        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains("DFM Multiple Components Summary Report");
        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains(pdfExpectedReportData.getCostMetric());
        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains(scenarioName);
        emailMessage.deleteEmailMessage();

    }

    @AfterEach
    public void cleanup() {
        CicApiTestUtil.deleteWorkFlow(loginSession, CicApiTestUtil.getMatchedWorkflowId(workflowName));
        softAssertions.assertAll();
    }
}
