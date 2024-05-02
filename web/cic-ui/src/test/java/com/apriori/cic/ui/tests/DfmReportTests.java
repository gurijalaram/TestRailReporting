package com.apriori.cic.ui.tests;

import com.apriori.cic.api.enums.CICPartSelectionType;
import com.apriori.cic.api.enums.EmailRecipientType;
import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.models.request.AgentPort;
import com.apriori.cic.api.models.response.AgentWorkflowJobResults;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.WorkflowDataUtil;
import com.apriori.cic.ui.enums.AprioriCostType;
import com.apriori.cic.ui.enums.CheckboxState;
import com.apriori.cic.ui.enums.EmailTemplateEnum;
import com.apriori.cic.ui.enums.FieldState;
import com.apriori.cic.ui.enums.RuleOperatorEnum;
import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;
import com.apriori.cic.ui.utils.CicGuiTestUtil;
import com.apriori.shared.util.email.GraphEmailService;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.models.response.EmailMessage;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class DfmReportTests extends CicGuiTestUtil {

    private static String scenarioName;
    private static SoftAssertions softAssertions;
    private AgentPort agentPort;
    private WorkflowHome workflowHome;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
        agentPort = CicApiTestUtil.getAgentPortData();
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY).build();
        ciConnectHome = new CicLoginPage(driver)
            .login(currentUser);
    }

    @Test
    @TestRail(id = {4040, 3969, 11995, 25799})
    @Description("Test Email Configuration - CostRounding set to Yes" +
        "Scenario name is included in Component / Scenarios column in DFM Part Summary Email Template" +
        "DFM Part Summary Notification email has correct text")
    public void testVerifyDFMEmailCostRoundingSetToYes() {
        String emailNotificationContent = "The following components have been analyzed for manufacturability and cost by aPriori. The DFM Risk rating indicates the severity of the manufacturability issues associated with each component. <br>Click the Part Number link in the table below to review the components manufacturing guidance in aP Design";
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
            .selectReturnLatestRevisionOnlyCheckbox(CheckboxState.on)
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, "0000003985")
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .clickCINextBtn()
            .selectEmailTab()
            .selectEmailTemplate(EmailTemplateEnum.DFM_PART_SUMMARY)
            .selectRecipient(EmailRecipientType.CONSTANT, PropertiesContext.get("global.report_email_address"))
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");

        AgentWorkflowJobResults agentWorkflowJobResults = this.getWorkflow()
            .invokeWorkflow()
            .trackWorkflow()
            .getJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getPartType().equals("PART")).isTrue();
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getFullyBurdenedCost().toString().contains("2.7")).isTrue();

        // Read the email and verify content and attached watch point report
        EmailMessage emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        softAssertions.assertThat(emailMessage.getBody().getContent().contains(scenarioName)).isTrue();
        softAssertions.assertThat(emailMessage.getSubject().contains("aP Generate DFM Part Summary")).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("aP Generate Analysis Summary")).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains(emailNotificationContent)).isTrue();

        softAssertions.assertThat(emailMessage.getBody().getContent().contains(String.format(">%s</a>", agentWorkflowJobResults.get(0).getCidPartNumber()))).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("Costing Result")).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("DFM Risk Rating")).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("Process Group")).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("aPriori Cost")).isTrue();

        softAssertions.assertThat(emailMessage.getBody().getContent().contains("$3")).isTrue();
        emailMessage.deleteEmailMessage();
    }

    @Test
    @TestRail(id = {29565})
    @Description("Test Email Configuration - CostRounding set to Yes and apriori cost set to Piece Part Cost")
    public void testVerifyDFMAprioriCostSetToPPC() {
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
            .selectReturnLatestRevisionOnlyCheckbox(CheckboxState.on)
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, "0000003987")
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .clickCINextBtn()
            .selectEmailTab()
            .selectEmailTemplate(EmailTemplateEnum.DFM_PART_SUMMARY)
            .selectAprioriCost(AprioriCostType.PIECE_PART_COST)
            .selectRecipient(EmailRecipientType.CONSTANT, PropertiesContext.get("global.report_email_address"))
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");

        AgentWorkflowJobResults agentWorkflowJobResults = this.getWorkflow()
            .invokeWorkflow()
            .trackWorkflow()
            .getJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getPartType().equals("PART")).isTrue();
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getTotalCost().toString().contains("21.6")).isTrue();

        // Read the email and verify content and attached watch point report
        EmailMessage emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        softAssertions.assertThat(emailMessage.getBody().getContent().contains(scenarioName)).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("aP Generate Analysis Summary")).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("$22")).isTrue();
        emailMessage.deleteEmailMessage();
    }

    @Test
    @TestRail(id = {29565})
    @Description("Test Email Configuration - CostRounding set to No and apriori cost set to Fully Burdened Cost")
    public void testVerifyDFMAprioriCostSetToFBC() {
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
            .selectReturnLatestRevisionOnlyCheckbox(CheckboxState.on)
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, "0000003985")
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .clickCINextBtn()
            .selectEmailTab()
            .selectEmailTemplate(EmailTemplateEnum.DFM_PART_SUMMARY)
            .selectCostRounding(FieldState.No)
            .selectAprioriCost(AprioriCostType.FULLY_BURDENED_COST)
            .selectRecipient(EmailRecipientType.CONSTANT, PropertiesContext.get("global.report_email_address"))
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");

        AgentWorkflowJobResults agentWorkflowJobResults = this.getWorkflow()
            .invokeWorkflow()
            .trackWorkflow()
            .getJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getPartType().equals("PART")).isTrue();
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getFullyBurdenedCost().toString().contains("2.7")).isTrue();

        // Read the email and verify content and attached watch point report
        EmailMessage emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        softAssertions.assertThat(emailMessage.getBody().getContent().contains(scenarioName)).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("aP Generate Analysis Summary")).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains(String.format(">%s</a>", agentWorkflowJobResults.get(0).getCidPartNumber()))).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("Costing Result")).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("DFM Risk Rating")).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("Process Group")).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("aPriori Cost")).isTrue();

        softAssertions.assertThat(emailMessage.getBody().getContent().contains("$2.7")).isTrue();
        emailMessage.deleteEmailMessage();
    }

    @Test
    @TestRail(id = {29565})
    @Description("Test Email Configuration - CostRounding set to Yes and apriori cost set to Material Cost")
    public void testVerifyDFMAprioriCostSetToMC() {
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
            .selectReturnLatestRevisionOnlyCheckbox(CheckboxState.on)
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, "0000003986")
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .clickCINextBtn()
            .selectEmailTab()
            .selectEmailTemplate(EmailTemplateEnum.DFM_PART_SUMMARY)
            .selectAprioriCost(AprioriCostType.MATERIAL_COST)
            .selectRecipient(EmailRecipientType.CONSTANT, PropertiesContext.get("global.report_email_address"))
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");

        AgentWorkflowJobResults agentWorkflowJobResults = this.getWorkflow()
            .invokeWorkflow()
            .trackWorkflow()
            .getJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getPartType().equals("PART")).isTrue();
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getMaterialCost().toString().contains("0.6")).isTrue();

        // Read the email and verify content and attached watch point report
        EmailMessage emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        softAssertions.assertThat(emailMessage.getBody().getContent().contains(scenarioName)).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("aP Generate Analysis Summary")).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("$1")).isTrue();
        emailMessage.deleteEmailMessage();
    }

    @AfterEach
    public void cleanup() {
        softAssertions.assertAll();
        CicApiTestUtil.deleteWorkFlow(workflowHome.getJsessionId(), CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName()));
    }
}