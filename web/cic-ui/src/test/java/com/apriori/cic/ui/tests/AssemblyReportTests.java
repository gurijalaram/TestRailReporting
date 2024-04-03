package com.apriori.cic.ui.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cic.api.enums.CICPartSelectionType;
import com.apriori.cic.api.enums.EmailRecipientType;
import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmPartDataType;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.enums.ReportsEnum;
import com.apriori.cic.api.models.request.AgentPort;
import com.apriori.cic.api.models.response.AgentWorkflowJobPartsResult;
import com.apriori.cic.api.models.response.AgentWorkflowJobResults;
import com.apriori.cic.api.models.response.AgentWorkflowJobRun;
import com.apriori.cic.api.models.response.ConnectorJobPart;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.api.utils.WorkflowDataUtil;
import com.apriori.cic.ui.enums.CheckboxState;
import com.apriori.cic.ui.enums.EmailTemplateEnum;
import com.apriori.cic.ui.enums.FieldState;
import com.apriori.cic.ui.enums.RuleOperatorEnum;
import com.apriori.cic.ui.pagedata.WorkFlowData;
import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;
import com.apriori.cic.ui.utils.CicGuiTestUtil;
import com.apriori.cid.api.utils.ComponentsUtil;
import com.apriori.cid.api.utils.IterationsUtil;
import com.apriori.shared.util.builder.ComponentInfoBuilder;
import com.apriori.shared.util.email.GraphEmailService;
import com.apriori.shared.util.file.part.PartData;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.EmailMessage;
import com.apriori.shared.util.models.response.EmailMessageAttachments;
import com.apriori.shared.util.models.response.component.componentiteration.ComponentIteration;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AssemblyReportTests extends CicGuiTestUtil {

    private final IterationsUtil iterationsUtil = new IterationsUtil();
    private static String scenarioName;
    private static SoftAssertions softAssertions;
    private static List<PartData> plmPartData;
    private AgentWorkflowJobResults agentWorkflowJobResults;

    private AgentPort agentPort;
    private String workflowName;
    private WorkflowHome workflowHome;
    private WorkFlowData workFlowData;

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
            .addRule(PlmTypeAttributes.PLM_STRING1, RuleOperatorEnum.EQUAL, "Auto Asm Test 1")
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .addCostingInputRow(PlmTypeAttributes.PLM_MATERIAL_NAME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(PlmTypeAttributes.PLM_PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
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

        AgentWorkflowJobResults agentWorkflowJobResults = this.getWorkflow()
            .invokeWorkflow()
            .trackWorkflow()
            .getJobResult();

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
    @TestRail(id = {11102, 11067, 11098, 11973})
    @Description("Assemblies are costed bottom up - sub assemblies costed first " +
        "Parts costed before assemblies for simple single level assemblies" +
        "Assemblies are included in DFM Multiple Component Summary Report " +
        "DFM Multiple Component Summary Report displays aggregate cost for assemblies")
    public void testVerifySubAssembliesAndDFMReport() {
        List<String> parts = Arrays.asList("0000003957", "0000003958", "0000003959", "0000003960",
            "0000003961", "0000003965", "0000003966", "0000003967", "0000003968",
            "0000003969", "0000003970", "0000003971", "0000003972", "0000003973");
        List<String> subAssemblies = Arrays.asList("0000003962", "0000003963");
        List<String> topLevelAssembly = Arrays.asList("0000003964");
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
            .addRule(PlmTypeAttributes.PLM_STRING1, RuleOperatorEnum.EQUAL, "Auto Asm Test 2")
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .addCostingInputRow(PlmTypeAttributes.PLM_PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");

        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

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

        agentWorkflowJobResults = this.trackWorkflow().getJobResult();
        softAssertions.assertThat(agentWorkflowJobResults.stream().filter(result -> result.getPartType().equals("ASSEMBLY")).count()).isEqualTo(3);
        softAssertions.assertThat(agentWorkflowJobResults.stream().filter(result -> result.getPartType().equals("PART")).count()).isEqualTo(parts.size());

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
    @TestRail(id = {11985})
    @Description("UDA fields are populated for assemblies")
    public void testVerifyUDAForAssemblies() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_ASSEMBLY, 6);
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
            .addRule(PlmTypeAttributes.PLM_STRING1, RuleOperatorEnum.EQUAL, "Auto Asm Test 1")
            .clickWFQueryDefNextBtn()
            .addCostingInputRow(PlmTypeAttributes.PLM_SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .addCostingInputRow(PlmTypeAttributes.PLM_MATERIAL_NAME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(PlmTypeAttributes.PLM_PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(PlmTypeAttributes.PLM_CUSTOM_STRING, MappingRule.CONSTANT, "CS1234")
            .addCostingInputRow(PlmTypeAttributes.PLM_CUSTOM_NUMBER, MappingRule.CONSTANT, "98765")
            .addCustomMultiString(MappingRule.CONSTANT, "Value 2")
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");

        AgentWorkflowJobResults agentWorkflowJobResults = this.getWorkflow()
            .invokeWorkflow()
            .trackWorkflow()
            .getJobResult();

        List<AgentWorkflowJobPartsResult> jobPartsResults = agentWorkflowJobResults.stream()
            .filter(result -> result.getPartType().equals("ASSEMBLY") && result.getCicStatus().equals("FINISHED")).collect(Collectors.toList());

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(
            ComponentInfoBuilder.builder()
                .componentIdentity(StringUtils.substringBetween(jobPartsResults.get(0).getCidPartLink(), "components/", "/scenarios"))
                .scenarioIdentity(StringUtils.substringAfterLast(jobPartsResults.get(0).getCidPartLink(), "/"))
                .user(currentUser)
                .build());

        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "CS1234")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "98765")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "Value 2")).isTrue();
    }

    @Test
    @TestRail(id = {11989, 112020})
    @Description("Duplicate assemblies are skipped ie if multiple revisions of an assembly are " +
        "returned and a unique scenario name is not set for each then only one assembly will be costed")
    public void testVerifyDuplicateAssemblySkipped() {
        String plmPartNumber = "0000003938";
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
            .addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, plmPartNumber)
            .clickWFQueryDefNextBtn()
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");

        AgentWorkflowJobResults agentWorkflowJobResults = this.getWorkflow()
            .invokeWorkflow()
            .trackWorkflow()
            .getJobResult();

        AgentWorkflowJobPartsResult jobPartsRevisionAResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResults, "A");
        AgentWorkflowJobPartsResult jobPartsRevisionBResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResults, "B");

        softAssertions.assertThat(jobPartsRevisionBResult.getErrorMessage().contains("This part appeared multiple times in the job"));
        softAssertions.assertThat(jobPartsRevisionAResult.getPartNumber().contains("piston_assembly_1"));
    }

    @AfterEach
    public void cleanup() {
        softAssertions.assertAll();
        CicApiTestUtil.deleteWorkFlow(workflowHome.getJsessionId(), CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName()));
    }
}