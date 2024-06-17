package com.apriori.cic.ui.tests;

import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmPartDataType;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.models.request.AgentPort;
import com.apriori.cic.api.models.request.JobDefinition;
import com.apriori.cic.api.models.response.AgentWorkflowJobRun;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.ui.enums.RuleOperatorEnum;
import com.apriori.cic.ui.pagedata.WorkFlowData;
import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;
import com.apriori.cic.ui.utils.CicGuiTestUtil;
import com.apriori.shared.util.email.GraphEmailService;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.EmailMessage;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JobMonitoringEmailTests extends CicGuiTestUtil {

    private JobDefinition jobDefinitionData;
    private WorkFlowData workFlowData;
    private String scenarioName;
    private String workflowName;
    private WorkflowHome workflowHome;
    private SoftAssertions softAssertions;
    private AgentPort agentPort;
    private EmailMessage emailMessage;

    public JobMonitoringEmailTests() {
        super();
    }

    @BeforeEach
    public void setup() {
        softAssertions = new SoftAssertions();
        jobDefinitionData = CicApiTestUtil.getJobDefinitionData();
        currentUser = UserUtil.getUser();
        agentPort = CicApiTestUtil.getAgentPortData();
        scenarioName = PropertiesContext.get("customer") + RandomStringUtils.randomNumeric(6);
        workflowName = GenerateStringUtil.saltString("- - - 0 JME");
        this.cicGuiLogin();
    }

    @Test
    @TestRail(id = {29285})
    @Description("Job Monitoring email generated on condition Failed Job")
    public void verifyMonitorWithFailedJob() {
        // CREATE WORK FLOW
        workflowHome = this.ciConnectHome.clickWorkflowMenu()
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workflowName)
            .selectWorkflowConnector("AUTO-WC-NC")
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber())
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .clickCINextBtn()
            .selectMonitoringTab()
            .selectMultiSelectField("Failed Job")
            .enterRecipient(PropertiesContext.get("global.report_email_address"))
            .clickCINotificationNextBtn()
            .clickSaveButton();
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        workflowHome.closeMessageAlertBox();

        workflowHome.selectScheduleTab().selectWorkflow(workflowName).clickInvokeButton();

        softAssertions.assertThat(workflowHome.selectViewHistoryTab().searchAndTrackWorkFlowStatus(workflowName)).isTrue();
        // Read the email and verify content and attached watch point report
        emailMessage = GraphEmailService.searchEmailMessageWithAttachments(workflowName);
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("aP Generate Job Monitoring Details")).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("Errored")).isTrue();
    }

    @Test
    @TestRail(id = {29286})
    @Description("Job Monitoring email generated on condition Errored Component")
    public void verifyMonitorWithErroredComponent() {
        // CREATE WORK FLOW
        workflowHome = this.ciConnectHome.clickWorkflowMenu()
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workflowName)
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, "0000003974")
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .clickCINextBtn()
            .selectMonitoringTab()
            .selectMultiSelectField("Errored Component")
            .enterRecipient(PropertiesContext.get("global.report_email_address"))
            .clickCINotificationNextBtn()
            .clickSaveButton();
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        workflowHome.closeMessageAlertBox();

        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowName);
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();
        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();
        this.trackWorkflow();

        // Read the email and verify content and attached watch point report
        emailMessage = GraphEmailService.searchEmailMessageWithAttachments(workflowName);
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("aP Generate Job Monitoring Details")).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("Finished")).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains(">1<")).isTrue();
    }

    @Test
    @TestRail(id = {29287})
    @Description("Job Monitoring email generated on condition Errored Component")
    public void verifyMonitorWithProcessedComponent() {
        // CREATE WORK FLOW
        workflowHome = this.ciConnectHome.clickWorkflowMenu()
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workflowName)
            .selectWorkflowConnector(agentPort.getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PARTIAL).getPlmPartNumber())
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .clickCINextBtn()
            .selectMonitoringTab()
            .selectMultiSelectField("No Errored Components")
            .enterRecipient(PropertiesContext.get("global.report_email_address"))
            .clickCINotificationNextBtn()
            .clickSaveButton();
        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        workflowHome.closeMessageAlertBox();

        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowName);
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();
        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        this.trackWorkflow();

        // Read the email and verify content and attached watch point report
        emailMessage = GraphEmailService.searchEmailMessageWithAttachments(workflowName);
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("aP Generate Job Monitoring Details")).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("Finished")).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains(">1<")).isTrue();
    }

    @AfterEach
    public void cleanup() {
        workflowHome.selectScheduleTab().selectWorkflow(workflowName).clickDeleteBtn().clickConfirmDeleteBtn();
        softAssertions.assertAll();
    }
}
