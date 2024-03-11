package com.apriori.cic.ui.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmPartDataType;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.enums.ReportsEnum;
import com.apriori.cic.api.models.response.AgentWorkflowJobResults;
import com.apriori.cic.api.models.response.AgentWorkflowJobRun;
import com.apriori.cic.api.models.response.ConnectorJobPart;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.ui.enums.CheckboxState;
import com.apriori.cic.ui.enums.RuleOperatorEnum;
import com.apriori.cic.ui.pagedata.WorkFlowData;
import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;
import com.apriori.cic.ui.utils.CicGuiTestUtil;
import com.apriori.shared.util.dataservice.TestDataService;
import com.apriori.shared.util.email.GraphEmailService;
import com.apriori.shared.util.file.part.PartData;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.models.response.EmailMessage;
import com.apriori.shared.util.models.response.EmailMessageAttachments;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class AssemblyReportTests extends CicGuiTestUtil {

    private static String scenarioName;
    private static SoftAssertions softAssertions;
    private static List<PartData> plmPartData;
    private AgentWorkflowJobResults agentWorkflowJobResults;
    private String workflowName;
    private WorkflowHome workflowHome;
    private WorkFlowData workFlowData;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {11103, 11096, 11109, 11968, 11077, 11072, 11071, 11982})
    @Description("Assemblies are returned from Windchill with default agent configuration" +
        "DTC Component Summary Reports are generated for assemblies" +
        "Assemblies are included in component counts" +
        "All assemblies costed with Process Group 'Assembly' when different process group read from PLM" +
        "Material set in Workflow with mapping rule Mapped from PLM (not applicable to assemblies) is ignored for any assemblies costed in resulting jobs" +
        "Return Latest Revision option can be applied to assemblies")
    public void testVerifyDTCReportForAssemblies() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_ASSEMBLY, 6);
        WorkFlowData workFlowData = new TestDataService().getTestData("WorkFlowTestData.json", WorkFlowData.class);
        String randomNumber = RandomStringUtils.randomNumeric(6);
        workflowName = "CICASM" + randomNumber;
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
            .selectReturnLatestRevisionOnlyCheckbox(CheckboxState.on)
            .addRule(PlmTypeAttributes.PLM_STRING1, RuleOperatorEnum.EQUAL, "Auto Asm Test 1")
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .addCostingInputRow(PlmTypeAttributes.PLM_MATERIAL_NAME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(PlmTypeAttributes.PLM_PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
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

        AgentWorkflowJobResults agentWorkflowJobResults = this.getJobResult();
        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(6);
        softAssertions.assertThat(agentWorkflowJobResults.stream().filter(result -> result.getPartType().equals("ASSEMBLY")).count()).isEqualTo(3);
        softAssertions.assertThat(agentWorkflowJobResults.stream().filter(result -> result.getPartType().equals("PART")).count()).isEqualTo(3);
        softAssertions.assertThat(agentWorkflowJobResults.stream().filter(result -> result.getPartNumber().equals("0000003938"))
            .anyMatch(agentWorkflowJobPartsResult -> agentWorkflowJobPartsResult.getRevisionNumber().equals("B"))).isTrue();
        softAssertions.assertThat(this.verifyPartNumberInJobResult(agentWorkflowJobResults, plmPartData)).isTrue();
        softAssertions.assertThat(this.verifyPartNameInJobResult(agentWorkflowJobResults, plmPartData)).isTrue();


        // Read the email and verify content and attached watch point report
        EmailMessage emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        softAssertions.assertThat(emailMessage.getBody().getContent().contains(scenarioName)).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("aP Generate Analysis Summary")).isTrue();
        EmailMessageAttachments emailMessageAttachments = emailMessage.emailMessageAttachments();
        softAssertions.assertThat(emailMessageAttachments.value.size()).isEqualTo(6);

        softAssertions.assertThat(this.verifyEmailAttachedReportName(emailMessageAttachments, plmPartData)).isTrue();
        softAssertions.assertThat(this.verifyPdfDocumentContent(emailMessageAttachments, plmPartData)).isTrue();

        emailMessage.deleteEmailMessage();
    }


    @Test
    @TestRail(id = {11102, 11067})
    @Description("Assemblies are costed bottom up - sub assemblies costed first " +
        "Parts costed before assemblies for simple single level assemblies")
    public void testVerifySubAssemblies() {
        List<String> parts = Arrays.asList("0000003957", "0000003958", "0000003959", "0000003960",
            "0000003961", "0000003965", "0000003966", "0000003967", "0000003968",
            "0000003969", "0000003970", "0000003971", "0000003972", "0000003973");
        List<String> subAssemblies = Arrays.asList("0000003962", "0000003963");
        List<String> topLevelAssembly = Arrays.asList("0000003964");
        String randomNumber = RandomStringUtils.randomNumeric(6);
        workflowName = "CICASM" + randomNumber;
        scenarioName = PropertiesContext.get("customer") + randomNumber;
        workFlowData = new TestDataService().getTestData("WorkFlowTestData.json", WorkFlowData.class);
        workFlowData.setWorkflowName(workflowName);
        workFlowData.getNotificationsData().setReportName(ReportsEnum.DTC_COMPONENT_SUMMARY.getReportName());
        workFlowData.getPublishResultsData().setReportName(ReportsEnum.DTC_COMPONENT_SUMMARY.getReportName());
        log.info(String.format("Start Creating Workflow >> %s <<", workFlowData.getWorkflowName()));
        workflowHome = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab()
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .selectEnabledCheckbox("off")
            .clickNextBtnInDetailsTab()
            .selectReturnLatestRevisionOnlyCheckbox(CheckboxState.on)
            .addRule(PlmTypeAttributes.PLM_STRING1, RuleOperatorEnum.EQUAL, "Auto Asm Test 2")
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .addCostingInputRow(PlmTypeAttributes.PLM_PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");

        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workFlowData.getWorkflowName());
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        // Verify workflow job is finished.
        ConnectorJobPart expectedJobParts = ConnectorJobPart.builder().status("Completed").partType("Part").build();
        softAssertions.assertThat(this.isWorkflowJobStarted(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();
        List<ConnectorJobPart> connectorJobParts = this.getWorkflowJobCostedResult(agentWorkflowJobRunResponse.getJobId(),
            parts.size(), expectedJobParts, workflowHome.getJsessionId());
        softAssertions.assertThat(this.verifyParts(connectorJobParts, parts)).isTrue();

        ConnectorJobPart expectedJobSubAsmbly = ConnectorJobPart.builder().status("Completed").partType("Assembly").build();
        List<ConnectorJobPart> subAssembly = this.getWorkflowJobCostedResult(agentWorkflowJobRunResponse.getJobId(),
            subAssemblies.size(), expectedJobSubAsmbly, workflowHome.getJsessionId());
        softAssertions.assertThat(this.verifyParts(subAssembly, subAssemblies)).isTrue();

        ConnectorJobPart expectedJobTopAsmbly = ConnectorJobPart.builder().status("Completed").partType("Assembly").build();
        List<ConnectorJobPart> topAssembly = this.getWorkflowJobCostedResult(agentWorkflowJobRunResponse.getJobId(),
            3, expectedJobTopAsmbly, workflowHome.getJsessionId());
        softAssertions.assertThat(topAssembly.stream().filter(part -> part.getPartNumber().equals(topLevelAssembly.get(0))).findFirst().isPresent()).isTrue();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();
        agentWorkflowJobResults = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class, HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobResults.stream().filter(result -> result.getPartType().equals("ASSEMBLY")).count()).isEqualTo(3);
        softAssertions.assertThat(agentWorkflowJobResults.stream().filter(result -> result.getPartType().equals("PART")).count()).isEqualTo(parts.size());
    }

    @AfterEach
    public void cleanup() {
        softAssertions.assertAll();
        CicApiTestUtil.deleteWorkFlow(workflowHome.getJsessionId(), CicApiTestUtil.getMatchedWorkflowId(workFlowData.getWorkflowName()));
    }
}