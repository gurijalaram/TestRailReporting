package com.integration.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmPartDataType;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.enums.ReportsEnum;
import com.apriori.cic.api.models.request.AgentPort;
import com.apriori.cic.api.models.request.JobDefinition;
import com.apriori.cic.api.models.response.AgentWorkflow;
import com.apriori.cic.api.models.response.AgentWorkflowJobRun;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.ui.enums.RuleOperatorEnum;
import com.apriori.cic.ui.pagedata.WorkFlowData;
import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;
import com.apriori.cir.ui.pageobjects.header.ReportsHeader;
import com.apriori.cir.ui.pageobjects.login.ReportsLoginPage;
import com.apriori.nts.api.reports.componentsummary.MultipleComponentSummary;
import com.apriori.shared.util.PDFDocument;
import com.apriori.shared.util.dataservice.TestDataService;
import com.apriori.shared.util.email.GraphEmailService;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.models.response.EmailMessage;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class CIRIntegrationTests extends TestBaseUI {

    private AgentWorkflow agentWorkflowResponse;
    private JobDefinition jobDefinitionData;
    private AgentWorkflowJobRun agentWorkflowJobRunResponse;
    private String workflowName;
    private String scenarioName;
    private SoftAssertions softAssertions;
    private UserCredentials currentUser;
    private AgentPort agentPort;
    private String plmPartNumber;

    @BeforeEach
    public void setup() {
        softAssertions = new SoftAssertions();
        jobDefinitionData = new TestDataService().getTestData("CicGuiDeleteJobDefData.json", JobDefinition.class);
        currentUser = UserUtil.getUser();
        agentPort = CicApiTestUtil.getAgentPortData();
        plmPartNumber = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber();
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
    @TestRail(id = {12046})
    @Description("Create Workflow, Invoke workflow, verify CIR report from email and delete workflow")
    public void testVerifyCIRReport() {
        WorkFlowData workFlowData = new TestDataService().getTestData("WorkFlowData.json", WorkFlowData.class);
        MultipleComponentSummary pdfExpectedReportData = new TestDataService().getReportData("MultipleComponentsSummary.json", MultipleComponentSummary.class);
        String randomNumber = RandomStringUtils.randomNumeric(6);
        workflowName = "CIC_REPORT" + randomNumber;
        scenarioName = PropertiesContext.get("customer") + randomNumber;
        workFlowData.setWorkflowName(workflowName);
        workFlowData.getNotificationsData().setReportName(ReportsEnum.DTC_COMPONENT_SUMMARY.getReportName());
        workFlowData.getPublishResultsData().setReportName(ReportsEnum.DTC_COMPONENT_SUMMARY.getReportName());
        log.info(String.format("Start Creating Workflow >> %s <<", workFlowData.getWorkflowName()));
        WorkflowHome workflowHome = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, plmPartNumber)
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .clickCINextBtn()
            .selectEmailTab()
            .selectEmailTemplate()
            .selectRecipient()
            .selectAttachReport()
            .selectReportName()
            .selectCostRounding()
            .clickCINotificationNextBtn()
            .selectAttachReportTab()
            .selectReportName()
            .selectCostRounding()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowName);
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();
        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        // Verify workflow job is finished.
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
        CicApiTestUtil.deleteWorkFlow(workflowHome.getJsessionId(), CicApiTestUtil.getMatchedWorkflowId(workflowName));
    }

    @AfterEach
    public void cleanup() {
        softAssertions.assertAll();
    }
}
