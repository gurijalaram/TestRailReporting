package com.apriori.cic.ui.tests;

import com.apriori.cic.api.enums.CICPartSelectionType;
import com.apriori.cic.api.enums.EmailRecipientType;
import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.models.request.AgentPort;
import com.apriori.cic.api.models.response.AgentWorkflowJobResults;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.WorkflowDataUtil;
import com.apriori.cic.ui.enums.CheckboxState;
import com.apriori.cic.ui.enums.EmailTemplateEnum;
import com.apriori.cic.ui.enums.FieldState;
import com.apriori.cic.ui.enums.RuleOperatorEnum;
import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;
import com.apriori.cic.ui.utils.CicGuiTestUtil;
import com.apriori.shared.util.email.GraphEmailService;
import com.apriori.shared.util.enums.PropertyEnum;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class DfmReportTests extends CicGuiTestUtil {

    private static String scenarioName;
    private static SoftAssertions softAssertions;
    private AgentPort agentPort;
    private WorkflowHome workflowHome;
    private EmailMessage emailMessage;
    private List<String> expectedEmailContent;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
        agentPort = CicApiTestUtil.getAgentPortData();
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY).build();
        expectedEmailContent = new ArrayList<>();
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

        emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        softAssertions.assertThat(emailMessage.getSubject().contains("aP Generate DFM Part Summary")).isTrue();
        expectedEmailContent = Arrays.asList(scenarioName, String.format(">%s</a>", agentWorkflowJobResults.get(0).getCidPartNumber()),
            emailNotificationContent,
            "aP Generate Analysis Summary",
            "Costing Result",
            "DFM Risk Rating",
            "Process Group",
            "aPriori Cost",
            "$3");
        softAssertions.assertThat(this.verifyEmailBody(emailMessage, expectedEmailContent)).isTrue();
    }

    @Test
    @TestRail(id = {30989})
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
            .selectAprioriCost(PropertyEnum.PIECE_PART_COST)
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

        emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        expectedEmailContent = Arrays.asList(scenarioName,
            "aP Generate Analysis Summary",
            "$22");
        softAssertions.assertThat(this.verifyEmailBody(emailMessage, expectedEmailContent)).isTrue();
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
            .selectRecipient(EmailRecipientType.CONSTANT, PropertiesContext.get("global.report_email_address"))
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
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getFullyBurdenedCost().toString().contains("2.7")).isTrue();

        emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        expectedEmailContent = Arrays.asList(scenarioName,
            "aP Generate Analysis Summary",
            "$2.7");
        softAssertions.assertThat(this.verifyEmailBody(emailMessage, expectedEmailContent)).isTrue();
    }

    @Test
    @TestRail(id = {30990})
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
            .selectRecipient(EmailRecipientType.CONSTANT, PropertiesContext.get("global.report_email_address"))
            .selectAprioriCost(PropertyEnum.MATERIAL_COST)
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

        emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        expectedEmailContent = Arrays.asList(scenarioName,
            "aP Generate Analysis Summary",
            "$1");
        softAssertions.assertThat(this.verifyEmailBody(emailMessage, expectedEmailContent)).isTrue();
    }

    @AfterEach
    public void cleanup() {
        softAssertions.assertAll();
        emailMessage.deleteEmailMessage();
        CicApiTestUtil.deleteWorkFlow(workflowHome.getJsessionId(), CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName()));
    }
}