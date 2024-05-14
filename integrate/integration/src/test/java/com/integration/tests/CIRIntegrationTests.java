package com.integration.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmPartDataType;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.enums.ReportsEnum;
import com.apriori.cic.api.models.request.AgentPort;
import com.apriori.cic.api.models.request.JobDefinition;
import com.apriori.cic.api.models.response.AgentWorkflowJobRun;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.ui.enums.FieldState;
import com.apriori.cic.ui.enums.RuleOperatorEnum;
import com.apriori.cic.ui.pagedata.WorkFlowData;
import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;
import com.apriori.cic.ui.utils.CicGuiTestUtil;
import com.apriori.cir.ui.pageobjects.header.ReportsHeader;
import com.apriori.cir.ui.pageobjects.login.ReportsLoginPage;
import com.apriori.shared.util.dataservice.TestDataService;
import com.apriori.shared.util.email.GraphEmailService;
import com.apriori.shared.util.file.PDFDocument;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.models.response.EmailMessage;
import com.apriori.shared.util.properties.PropertiesContext;
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
public class CIRIntegrationTests extends CicGuiTestUtil {

    private JobDefinition jobDefinitionData;
    private String workflowName;
    private String scenarioName;
    private SoftAssertions softAssertions;
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
    @TestRail(id = {12046, 3967, 4044})
    @Description("Create Workflow, Invoke workflow, verify CIR report from email and delete workflow" +
        "Test DTC Component Summary Report email attachment" +
        "Check the contents of the DTC Part Summary report tally with the costing results expected")
    public void testVerifyCIRReport() {
        WorkFlowData workFlowData = new TestDataService().getTestData("WorkFlowData.json", WorkFlowData.class);
        String randomNumber = RandomStringUtils.randomNumeric(6);
        workflowName = "CIC_REPORT" + randomNumber;
        scenarioName = PropertiesContext.get("customer") + randomNumber;
        workFlowData.setWorkflowName(workflowName);
        workFlowData.getNotificationsData().setReportName(ReportsEnum.DTC_COMPONENT_SUMMARY.getReportName());
        workFlowData.getPublishResultsData().setReportName(ReportsEnum.DTC_COMPONENT_SUMMARY.getReportName());
        log.info(String.format("Start Creating Workflow >> %s <<", workFlowData.getWorkflowName()));
        this.ciConnectHome = new CicLoginPage(driver)
            .login(currentUser);

        WorkflowHome workflowHome = this.ciConnectHome.clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, plmPartNumber)
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .clickCINextBtn()
            .selectEmailTab()
            .selectEmailTemplate()
            .selectRecipient()
            .selectAttachReport()
            .selectReportName()
            .selectCostRounding(FieldState.Yes)
            .clickCINotificationNextBtn()
            .selectAttachReportTab()
            .selectReportName()
            .selectCostRounding(FieldState.Yes)
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        this.agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowName);
        softAssertions.assertThat(this.agentWorkflowResponse.getId()).isNotNull();

        this.agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(this.agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        this.trackWorkflow();

        EmailMessage emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        softAssertions.assertThat(emailMessage.getBody().getContent()).contains(scenarioName);
        softAssertions.assertThat(emailMessage.getBody().getContent()).contains("aP Generate Analysis Summary");
        PDFDocument pdfDocument = (PDFDocument) emailMessage.emailMessageAttachment().getFileAttachment();
        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains("GENERAL COST INFORMATION");
        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains("PRODUCTION INFORMATION");
        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains("COST INFORMATION");
        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains("MATERIAL INFORMATION");
        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains(scenarioName);
        emailMessage.deleteEmailMessage();
        CicApiTestUtil.deleteWorkFlow(this.ciConnectHome.getSession(), CicApiTestUtil.getMatchedWorkflowId(workflowName));
    }

    @AfterEach
    public void cleanup() {
        softAssertions.assertAll();
    }
}
