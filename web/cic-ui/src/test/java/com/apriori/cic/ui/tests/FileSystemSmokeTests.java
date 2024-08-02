package com.apriori.cic.ui.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cic.api.agent.FileSystemAgent;
import com.apriori.cic.api.enums.CICPartSelectionType;
import com.apriori.cic.api.enums.EmailRecipientType;
import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.enums.ReportsEnum;
import com.apriori.cic.api.models.request.JobDefinition;
import com.apriori.cic.api.models.response.AgentWorkflowJobResults;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.WorkflowDataUtil;
import com.apriori.cic.ui.enums.EmailTemplateEnum;
import com.apriori.cic.ui.enums.FieldState;
import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;
import com.apriori.cic.ui.utils.CicGuiTestUtil;
import com.apriori.shared.util.enums.RolesEnum;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class FileSystemSmokeTests extends CicGuiTestUtil {

    private static JobDefinition jobDefinitionData;
    private FileSystemAgent fileSystemAgent;
    private WorkflowHome workflowHome;
    private SoftAssertions softAssertions;
    private String scenarioName;

    @BeforeEach
    public void setup() {
        softAssertions = new SoftAssertions();
        jobDefinitionData = CicApiTestUtil.getJobDefinitionData();
        currentUser = UserUtil.getUser(RolesEnum.APRIORI_DEVELOPER);
        fileSystemAgent = new FileSystemAgent();
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY).build();
        ciConnectHome = new CicLoginPage(driver).login(currentUser);
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {30959, 30962})
    @Description("Create and Process File System work flow" +
        "DTC Component Summary [CIR] report written to File System")
    public void testVerifyDtcReportForFileSystemWorkflow() {
        String fileSystemInputFile = "fsa-cig-input1";
        String randomNumber = RandomStringUtils.randomNumeric(6);
        scenarioName = PropertiesContext.get("customer") + randomNumber;
        workflowHome = ciConnectHome.clickWorkflowMenu()
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workflowRequestDataBuilder.getName())
            .selectWorkflowConnector(fileSystemAgent.getAgentPort().getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnForFileSystem()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .addCostingInputRow(PlmTypeAttributes.PLM_PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(PlmTypeAttributes.PLM_DIGITAL_FACTORY, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(PlmTypeAttributes.PLM_MATERIAL_NAME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(PlmTypeAttributes.PLM_ANNUAL_VOLUME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(PlmTypeAttributes.PLM_BATCH_SIZE, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(PlmTypeAttributes.PLM_MACHINING_MODE, MappingRule.MAPPED_FROM_PLM, "")
            .clickCINextBtn()
            .selectEmailTab()
            .selectEmailTemplate(EmailTemplateEnum.DFM_PART_SUMMARY)
            .selectRecipient(EmailRecipientType.CONSTANT, PropertiesContext.get("global.report_email_address"))
            .selectAttachReport()
            .selectReportName(ReportsEnum.DTC_COMPONENT_SUMMARY)
            .selectCostRounding(FieldState.Yes)
            .clickCINotificationNextBtn()
            .selectAttachReportTab()
            .selectReportName(ReportsEnum.DTC_COMPONENT_SUMMARY)
            .selectCostRounding(FieldState.Yes)
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");

        if (softAssertions.wasSuccess()) {
            fileSystemAgent.uploadInputFileToRemoteWorkflowFolder(workflowRequestDataBuilder.getName(), fileSystemInputFile);
            AgentWorkflowJobResults agentWorkflowJobResults = this.getWorkflow()
                .invokeWorkflow()
                .trackWorkflow()
                .getJobResult();
            softAssertions.assertThat(agentWorkflowJobResults.size()).isGreaterThan(0);
            softAssertions.assertThat(fileSystemAgent.verifyOutputFile(workflowRequestDataBuilder.getName(), agentWorkflowJobRunResponse.getJobId())).isTrue();
            softAssertions.assertThat(fileSystemAgent.verifyReports(fileSystemInputFile, workflowRequestDataBuilder.getName(), agentWorkflowJobRunResponse.getJobId())).isTrue();
        }
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {30963})
    @Description("Part Cost[SSR] report written to File System")
    public void testVerifyPartCostReportForFileSystemWorkflow() {
        String fileSystemInputFile = "fsa-cig-input1";
        String randomNumber = RandomStringUtils.randomNumeric(6);
        scenarioName = PropertiesContext.get("customer") + randomNumber;
        workflowHome = ciConnectHome.clickWorkflowMenu()
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workflowRequestDataBuilder.getName())
            .selectWorkflowConnector(fileSystemAgent.getAgentPort().getConnector())
            .selectEnabledCheckbox("off")
            .clickNextBtnForFileSystem()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .addCostingInputRow(PlmTypeAttributes.PLM_PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(PlmTypeAttributes.PLM_DIGITAL_FACTORY, MappingRule.MAPPED_FROM_PLM, "")
            .clickCINextBtn()
            .selectEmailTab()
            .selectEmailTemplate(EmailTemplateEnum.DFM_PART_SUMMARY)
            .selectRecipient(EmailRecipientType.CONSTANT, PropertiesContext.get("global.report_email_address"))
            .selectAttachReport()
            .selectReportName(ReportsEnum.PART_COST)
            .clickCINotificationNextBtn()
            .selectAttachReportTab()
            .selectReportName(ReportsEnum.PART_COST)
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");

        if (softAssertions.wasSuccess()) {
            fileSystemAgent.uploadInputFileToRemoteWorkflowFolder(workflowRequestDataBuilder.getName(), fileSystemInputFile);
            AgentWorkflowJobResults agentWorkflowJobResults = this.getWorkflow()
                .invokeWorkflow()
                .trackWorkflow()
                .getJobResult();
            softAssertions.assertThat(agentWorkflowJobResults.size()).isGreaterThan(0);
            softAssertions.assertThat(fileSystemAgent.verifyOutputFile(workflowRequestDataBuilder.getName(), agentWorkflowJobRunResponse.getJobId())).isTrue();
            softAssertions.assertThat(fileSystemAgent.verifyReports(fileSystemInputFile, workflowRequestDataBuilder.getName(), agentWorkflowJobRunResponse.getJobId())).isTrue();
        }
    }

    @AfterEach
    public void cleanUp() {
        fileSystemAgent.deleteWorkflowFromAgent(workflowRequestDataBuilder.getName());
        CicApiTestUtil.deleteWorkFlow(ciConnectHome.getSession(), CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName()));
        fileSystemAgent.closeConnection();
        softAssertions.assertAll();
    }
}
