package com.apriori.cic.ui.tests;

import com.apriori.cic.api.enums.CICPartSelectionType;
import com.apriori.cic.api.enums.EmailRecipientType;
import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.enums.ReportsEnum;
import com.apriori.cic.api.models.request.AgentPort;
import com.apriori.cic.api.models.response.AgentWorkflowJob;
import com.apriori.cic.api.models.response.AgentWorkflowJobResults;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.WorkflowDataUtil;
import com.apriori.cic.ui.enums.EmailConfigSummaryEnum;
import com.apriori.cic.ui.enums.EmailTemplateEnum;
import com.apriori.cic.ui.enums.FieldState;
import com.apriori.cic.ui.enums.RuleOperatorEnum;
import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;
import com.apriori.cic.ui.utils.CicGuiTestUtil;
import com.apriori.shared.util.email.GraphEmailService;
import com.apriori.shared.util.file.ExcelService;
import com.apriori.shared.util.file.PDFDocument;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.models.response.EmailMessage;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class EmailConfigTests extends CicGuiTestUtil {

    private static String scenarioName;
    private static SoftAssertions softAssertions;
    private AgentPort agentPort;
    private WorkflowHome workflowHome;
    private EmailMessage emailMessage;
    private List<String> expectedEmailContent;
    private String sessionID;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
        agentPort = CicApiTestUtil.getAgentPortData();
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY).build();
        expectedEmailContent = new ArrayList<>();
        ciConnectHome = new CicLoginPage(driver)
            .login(currentUser);
        sessionID = ciConnectHome.getSession();
    }

    @Test
    @TestRail(id = {29345, 29748, 29775, 29780})
    @Description("Part Number in Scenario Field is hyperlinked" +
        "Correct display of Configurable Email with only Scenario an one other field selected" +
        "Configurable Notification email with no report attachment")
    public void testVerifySelectedFieldHyperlink() {
        String emailNotificationContent = "The following components have been analyzed by aPriori. Click the Scenario link in the table below to review the components' manufacturability, cost, and sustainability guidance in the aPriori cloud.";
        String randomNumber = RandomStringUtils.randomNumeric(6);

        scenarioName = PropertiesContext.get("customer") + randomNumber;
        log.info(String.format("Start Creating Workflow >> %s <<", workflowRequestDataBuilder.getName()));
        workflowHome = ciConnectHome.clickWorkflowMenu()
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workflowRequestDataBuilder.getName())
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, "0000003991")
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .clickCINextBtn()
            .selectEmailTab()
            .selectEmailTemplate(EmailTemplateEnum.CONFIGURABLE_SUMMARY)
            .selectRecipient(EmailRecipientType.CONSTANT, PropertiesContext.get("global.report_email_address"))
            .selectScenarioLink(PlmTypeAttributes.PLM_APD_SCENARIO_LINK)
            .selectField(EmailConfigSummaryEnum.FIELD_1, PlmTypeAttributes.PLM_PROCESS_GROUP)
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");

        AgentWorkflowJobResults agentWorkflowJobResults = this.getWorkflow()
            .invokeWorkflow()
            .trackWorkflow()
            .getJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getPartType().equals("PART")).isTrue();

        emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        softAssertions.assertThat(emailMessage.emailMessageAttachments().value.size()).isEqualTo(0);

        softAssertions.assertThat(emailMessage.getSubject().contains("aP Generate Configurable Summary")).isTrue();
        expectedEmailContent = Arrays.asList(scenarioName, String.format(">%s </a>", agentWorkflowJobResults.get(0).getCidPartNumber()),
            emailNotificationContent,
            workflowRequestDataBuilder.getName(),
            PlmTypeAttributes.PLM_PROCESS_GROUP.getCicGuiField(),
            "aP Generate Notification",
            agentWorkflowJobResults.get(0).getCidPartNumber());
        softAssertions.assertThat(this.verifyEmailBody(emailMessage, expectedEmailContent)).isTrue();
        softAssertions.assertThat(workflowHome.verifyPageUrl(StringUtils.substringBetween(emailMessage.getBody().getContent(), "a href=\"", "\" style="),
            StringUtils.substringBetween(agentWorkflowJobResults.get(0).getCidPartLink(), "https://", "/components"))).isTrue();
    }

    @Test
    @TestRail(id = {29840, 29749, 29842})
    @Description("Correct display of Configurable Email with columns with no field selection" +
        "Correct value for all standard fields with Cost Rounding ENABLED in Configurable Notification" +
        "Correct value for all Costing Input fields when set from all sources")
    public void testVerifySelectedFieldsInEmail() {
        String randomNumber = RandomStringUtils.randomNumeric(6);
        scenarioName = PropertiesContext.get("customer") + randomNumber;
        log.info(String.format("Start Creating Workflow >> %s <<", workflowRequestDataBuilder.getName()));
        workflowHome = ciConnectHome.clickWorkflowMenu()
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workflowRequestDataBuilder.getName())
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, "0000003993")
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .addCostingInputRow(PlmTypeAttributes.PLM_PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
            .clickCINextBtn()
            .selectEmailTab()
            .selectEmailTemplate(EmailTemplateEnum.CONFIGURABLE_SUMMARY)
            .selectRecipient(EmailRecipientType.CONSTANT, PropertiesContext.get("global.report_email_address"))
            .selectScenarioLink(PlmTypeAttributes.PLM_APD_SCENARIO_LINK)
            .selectField(EmailConfigSummaryEnum.FIELD_1, PlmTypeAttributes.PLM_MATERIAL_NAME)
            .selectField(EmailConfigSummaryEnum.FIELD_2, PlmTypeAttributes.PLM_REVISION)
            .selectField(EmailConfigSummaryEnum.FIELD_3, PlmTypeAttributes.PLM_DIGITAL_FACTORY)
            .selectField(EmailConfigSummaryEnum.FIELD_4, PlmTypeAttributes.PLM_FINISH_MASS)
            .selectField(EmailConfigSummaryEnum.FIELD_5, PlmTypeAttributes.PLM_PROCESS_GROUP)
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");

        AgentWorkflowJobResults agentWorkflowJobResults = this.getWorkflow()
            .invokeWorkflow()
            .trackWorkflow()
            .getJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getPartType().equals("PART")).isTrue();

        emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        softAssertions.assertThat(emailMessage.getSubject().contains("aP Generate Configurable Summary")).isTrue();
        expectedEmailContent = Arrays.asList(scenarioName, String.format(">%s </a>", agentWorkflowJobResults.get(0).getCidPartNumber()),
            workflowRequestDataBuilder.getName(),
            agentWorkflowJobResults.get(0).getInput().getProcessGroupName(),
            PlmTypeAttributes.PLM_PROCESS_GROUP.getCicGuiField(),
            PlmTypeAttributes.PLM_MATERIAL_NAME.getCicGuiField(),
            PlmTypeAttributes.PLM_REVISION.getCicGuiField(),
            PlmTypeAttributes.PLM_DIGITAL_FACTORY.getCicGuiField(),
            PlmTypeAttributes.PLM_FINISH_MASS.getCicGuiField(),
            "aP Generate Notification",
            agentWorkflowJobResults.get(0).getCidPartNumber());
        softAssertions.assertThat(this.verifyEmailBody(emailMessage, expectedEmailContent)).isTrue();
    }

    @Test
    @TestRail(id = {29751, 29769, 29776})
    @Description("Correct value for UDAs of all data types with Cost Rounding DISABLED Configurable Notification" +
        "Correct value for multiselect UDAs in Configurable Notification" +
        "Configurable Notification email with DTC Component Summary report attachment")
    public void testVerifySelectedUdaFieldsAndDtcInEmail() {
        String randomNumber = RandomStringUtils.randomNumeric(6);
        String customString = RandomStringUtils.randomAlphabetic(6);
        scenarioName = PropertiesContext.get("customer") + randomNumber;
        log.info(String.format("Start Creating Workflow >> %s <<", workflowRequestDataBuilder.getName()));
        workflowHome = ciConnectHome.clickWorkflowMenu()
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workflowRequestDataBuilder.getName())
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, "0000003995")
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .addCostingInputRow(PlmTypeAttributes.PLM_PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(PlmTypeAttributes.PLM_CUSTOM_NUMBER, MappingRule.CONSTANT, randomNumber)
            .addCostingInputRow(PlmTypeAttributes.PLM_CUSTOM_STRING, MappingRule.CONSTANT, customString)
            .addCustomMultiString(MappingRule.CONSTANT, "Value 1")
            .clickCINextBtn()
            .selectEmailTab()
            .selectEmailTemplate(EmailTemplateEnum.CONFIGURABLE_SUMMARY)
            .selectRecipient(EmailRecipientType.CONSTANT, PropertiesContext.get("global.report_email_address"))
            .selectCostRounding(FieldState.No)
            .selectScenarioLink(PlmTypeAttributes.PLM_APD_SCENARIO_LINK)
            .selectField(EmailConfigSummaryEnum.FIELD_1, PlmTypeAttributes.PLM_CUSTOM_MULTI)
            .selectField(EmailConfigSummaryEnum.FIELD_2, PlmTypeAttributes.PLM_CUSTOM_STRING)
            .selectField(EmailConfigSummaryEnum.FIELD_3, PlmTypeAttributes.PLM_CUSTOM_NUMBER)
            .selectAttachReport()
            .selectReportName(ReportsEnum.DTC_COMPONENT_SUMMARY)
            .selectCostRounding(FieldState.No)
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");

        AgentWorkflowJobResults agentWorkflowJobResults = this.getWorkflow()
            .invokeWorkflow()
            .trackWorkflow()
            .getJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getPartType().equals("PART")).isTrue();

        emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        expectedEmailContent = Arrays.asList(scenarioName, workflowRequestDataBuilder.getName(),
            randomNumber, customString,
            PlmTypeAttributes.PLM_CUSTOM_NUMBER.getCicGuiField(),
            PlmTypeAttributes.PLM_CUSTOM_MULTI.getCicGuiField(),
            PlmTypeAttributes.PLM_CUSTOM_STRING.getCicGuiField(),
            agentWorkflowJobResults.get(0).getCidPartNumber());
        softAssertions.assertThat(this.verifyEmailBody(emailMessage, expectedEmailContent)).isTrue();
        PDFDocument pdfDocument = (PDFDocument) emailMessage.emailMessageAttachment().getFileAttachment();
        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains("GENERAL COST INFORMATION");
        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains("PRODUCTION INFORMATION");
        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains("COST INFORMATION");
        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains("MATERIAL INFORMATION");
        softAssertions.assertThat(pdfDocument.getDocumentContents()).contains(scenarioName);
    }

    @Test
    @TestRail(id = {29774})
    @Description("Configurable Notification email is not sent when all component fail processing")
    public void testVerifyNoEmailForInvalidPart() {
        String randomNumber = RandomStringUtils.randomNumeric(6);
        scenarioName = PropertiesContext.get("customer") + randomNumber;
        String emailErrorMessage = "Reports generated by aPA will not be attached to email. To view the reports, export the data to aPA and view the reports there.";
        log.info(String.format("Start Creating Workflow >> %s <<", workflowRequestDataBuilder.getName()));
        workflowHome = ciConnectHome.clickWorkflowMenu()
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workflowRequestDataBuilder.getName())
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, "0000003994")
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .clickCINextBtn()
            .selectEmailTab()
            .selectEmailTemplate(EmailTemplateEnum.CONFIGURABLE_SUMMARY)
            .selectRecipient(EmailRecipientType.CONSTANT, PropertiesContext.get("global.report_email_address"))
            .selectScenarioLink(PlmTypeAttributes.PLM_APD_SCENARIO_LINK)
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");

        AgentWorkflowJobResults agentWorkflowJobResults = this.getWorkflow()
            .invokeWorkflow()
            .trackWorkflow()
            .getJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getCicStatus()).isEqualTo("ERRORED");

        AgentWorkflowJob agentWorkflowJob = CicApiTestUtil.getCicAgentWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId());
        softAssertions.assertThat(agentWorkflowJob.getErrorMessage()).isEqualTo(emailErrorMessage);
    }

    @Test
    @TestRail(id = {29751, 29769, 29776})
    @Description("Correct value for UDAs of all data types with Cost Rounding DISABLED Configurable Notification" +
        "Correct value for multiselect UDAs in Configurable Notification" +
        "Configurable Notification email with DTC Component Summary report attachment")
    public void testVerifySelectedFieldsWatchPointInEmail() {
        String randomNumber = RandomStringUtils.randomNumeric(6);
        String customString = RandomStringUtils.randomAlphabetic(6);
        scenarioName = PropertiesContext.get("customer") + randomNumber;
        log.info(String.format("Start Creating Workflow >> %s <<", workflowRequestDataBuilder.getName()));
        workflowHome = ciConnectHome.clickWorkflowMenu()
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workflowRequestDataBuilder.getName())
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, "0000003997")
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .addCostingInputRow(PlmTypeAttributes.PLM_PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(PlmTypeAttributes.PLM_CUSTOM_NUMBER, MappingRule.CONSTANT, randomNumber)
            .addCostingInputRow(PlmTypeAttributes.PLM_CUSTOM_STRING, MappingRule.CONSTANT, customString)
            .clickCINextBtn()
            .selectEmailTab()
            .selectEmailTemplate(EmailTemplateEnum.CONFIGURABLE_SUMMARY)
            .selectRecipient(EmailRecipientType.CONSTANT, PropertiesContext.get("global.report_email_address"))
            .selectCostRounding(FieldState.No)
            .selectScenarioLink(PlmTypeAttributes.PLM_APD_SCENARIO_LINK)
            .selectField(EmailConfigSummaryEnum.FIELD_1, PlmTypeAttributes.PLM_CUSTOM_STRING)
            .selectField(EmailConfigSummaryEnum.FIELD_2, PlmTypeAttributes.PLM_CUSTOM_NUMBER)
            .selectAttachReport()
            .selectReportName(ReportsEnum.PART_COST)
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");

        AgentWorkflowJobResults agentWorkflowJobResults = this.getWorkflow()
            .invokeWorkflow()
            .trackWorkflow()
            .getJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getPartType().equals("PART")).isTrue();

        emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        expectedEmailContent = Arrays.asList(scenarioName, agentWorkflowJobResults.get(0).getCidPartNumber(),
            workflowRequestDataBuilder.getName(),
            randomNumber, customString,
            PlmTypeAttributes.PLM_CUSTOM_NUMBER.getCicGuiField(),
            PlmTypeAttributes.PLM_CUSTOM_STRING.getCicGuiField());
        softAssertions.assertThat(this.verifyEmailBody(emailMessage, expectedEmailContent)).isTrue();
        ExcelService excelReport = (ExcelService) emailMessage.emailMessageAttachment().getFileAttachment();
        softAssertions.assertThat(excelReport).isNotNull();
        softAssertions.assertThat(excelReport.getFirstCellRowNum("Part Number")).isGreaterThan(0);
        softAssertions.assertThat(excelReport.getSheetNames()).contains("Part Cost Report");
        softAssertions.assertThat(excelReport.getSheetNames()).contains("ReportSummarySheet");
    }

    @AfterEach
    public void cleanup() {
        softAssertions.assertAll();
        emailMessage.deleteEmailMessage();
        CicApiTestUtil.deleteWorkFlow(sessionID, CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName()));
    }
}