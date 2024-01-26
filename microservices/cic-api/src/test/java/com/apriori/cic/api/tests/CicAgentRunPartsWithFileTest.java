package com.apriori.cic.api.tests;

import com.apriori.cic.api.enums.CICPartSelectionType;
import com.apriori.cic.api.enums.CICReportType;
import com.apriori.cic.api.enums.CostingInputFields;
import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmPartDataType;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.enums.PublishResultsWriteRule;
import com.apriori.cic.api.enums.ReportsEnum;
import com.apriori.cic.api.models.request.CostingInputs;
import com.apriori.cic.api.models.request.PlmFieldDefinitions;
import com.apriori.cic.api.models.request.WorkflowPart;
import com.apriori.cic.api.models.request.WorkflowParts;
import com.apriori.cic.api.models.response.AgentWorkflowJobPartsResult;
import com.apriori.cic.api.models.response.AgentWorkflowJobResults;
import com.apriori.cic.api.models.response.AgentWorkflowReportTemplates;
import com.apriori.cic.api.models.response.PlmPartResponse;
import com.apriori.cic.api.models.response.PlmSearchPart;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.PlmApiTestUtil;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.api.utils.WorkflowDataUtil;
import com.apriori.cic.api.utils.WorkflowTestUtil;
import com.apriori.shared.util.email.GraphEmailService;
import com.apriori.shared.util.enums.DigitalFactoryEnum;
import com.apriori.shared.util.enums.MaterialNameEnum;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.models.response.EmailMessage;
import com.apriori.shared.util.models.response.EmailMessageAttachments;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(TestRulesAPI.class)
public class CicAgentRunPartsWithFileTest extends WorkflowTestUtil {

    private SoftAssertions softAssertions;
    private PlmSearchPart plmPart;
    private PlmApiTestUtil plmApiTestUtil;
    private PlmFieldDefinitions plmFieldDefinitions;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        plmApiTestUtil = new PlmApiTestUtil();
        plmFieldDefinitions = new PlmFieldDefinitions();
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(id = {28440})
    @Description("RunPartList - No costing inputs set in request body")
    public void testWorkflowRunPartsNoCostingInput() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .isNotificationsIncluded(false, false, "")
            .isPublishResultsAttachReportInclude(false, "")
            .isPublishResultsWriteFieldsInclude(false)
            .build();
        PlmSearchPart plmSearchPart = new PlmApiTestUtil().getPlmPartByPartNumber(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PARTIAL).getPlmPartNumber());
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id(plmSearchPart.getId())
                .costingInputs(CostingInputs.builder().build())
                .build()))
            .build();

        AgentWorkflowJobResults agentWorkflowJobResult = this.createRestWorkflowAndGetJobResult();
        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getProcessGroupName()).isNotNull();
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getVpeName()).isNotNull();
    }

    @Test
    @TestRail(id = {16948})
    @Description("RunPartList - Valid File Path")
    public void testRunPartsWithValidRelativeFile() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .isNotificationsIncluded(false, false, "")
            .isPublishResultsAttachReportInclude(false, "")
            .isPublishResultsWriteFieldsInclude(false)
            .build();
        PlmSearchPart plmSearchPart = new PlmApiTestUtil().getPlmPartByPartNumber(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber());
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id(plmSearchPart.getId())
                .relativeCadFilePath(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup() + "/piston_pin.prt.1")
                .costingInputs(CostingInputs.builder()
                    .scenarioName(scenarioName)
                    .processGroupName(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
                    .materialName(MaterialNameEnum.ABS.getMaterialName())
                    .vpeName(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
                    .build())
                .build()))
            .build();

        AgentWorkflowJobResults agentWorkflowJobResult = this.createRestWorkflowAndGetJobResult();
        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getProcessGroupName()).isEqualTo(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getVpeName()).isEqualTo(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory());
    }

    @Test
    @TestRail(id = {26897})
    @Description("RunPartList - valid file path PLM read successful")
    public void testRunPartsWithValidFileReadFromPLM() {
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.MAPPED_FROM_PLM, "")
            .isNotificationsIncluded(false, false, "")
            .isPublishResultsAttachReportInclude(false, "")
            .isPublishResultsWriteFieldsInclude(false)
            .build();
        PlmSearchPart plmSearchPart = new PlmApiTestUtil().getPlmPartByPartNumber(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PARTIAL).getPlmPartNumber());
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id(plmSearchPart.getId())
                .relativeCadFilePath(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup() + "/piston_pin.prt.1")
                .costingInputs(CostingInputs.builder().build())
                .build()))
            .build();

        AgentWorkflowJobResults agentWorkflowJobResult = this.createRestWorkflowAndGetJobResult();
        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getProcessGroupName()).isEqualTo(ProcessGroupEnum.CASTING_DIE.getProcessGroup());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getVpeName()).isEqualTo(DigitalFactoryEnum.APRIORI_BRAZIL.getDigitalFactory());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getMaterialName()).isEqualTo(MaterialNameEnum.ALUMINIUM_ANSI_AL380.getMaterialName());
    }

    @Test
    @TestRail(id = {26888})
    @Description("RunPartList - valid file path PLM write successful")
    public void testRunPartsWithValidFilePLMWrite() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL);
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .isNotificationsIncluded(false, false, "")
            .isPublishResultsAttachReportInclude(false, "")
            .isPublishResultsWriteFieldsInclude(true)
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_MATERIAL_NAME, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_PROCESS_GROUP, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_DIGITAL_FACTORY, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_ANNUAL_VOLUME, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_BATCH_SIZE, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .build();

        PlmSearchPart plmSearchPart = plmApiTestUtil.getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id(plmSearchPart.getId())
                .relativeCadFilePath(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup() + "/piston_pin.prt.1")
                .costingInputs(CostingInputs.builder()
                    .scenarioName(scenarioName)
                    .processGroupName(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
                    .materialName(MaterialNameEnum.ABS.getMaterialName())
                    .vpeName(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
                    .build())
                .build()))
            .build();

        AgentWorkflowJobResults agentWorkflowJobResult = this.createRestWorkflowAndGetJobResult();
        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(1);

        plmPart = plmApiTestUtil.getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        PlmPartResponse plmPartResponse = plmApiTestUtil.plmCsrfToken().getPartInfoFromPlm(plmPart.getId()).getPlmPartResponse();
        softAssertions.assertThat(plmPartResponse).isNotNull();
        softAssertions.assertThat(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup()).isEqualTo(plmPartResponse.getApPG());
        softAssertions.assertThat(MaterialNameEnum.ABS.getMaterialName()).isEqualTo(plmPartResponse.getApMaterial());
        softAssertions.assertThat(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory()).isEqualTo(plmPartResponse.getVpe());
        plmApiTestUtil.updatePartInfoToPlm(plmPart.getId(), plmFieldDefinitions, "Update part to reset the data");

    }

    @Test
    @TestRail(id = {26815})
    @Description("RunPartList - valid file path and request containing at least 1 valid PLM Part ID and 1 invalid PLM Part ID")
    public void testRunPartsWithValidAndInvalidFile() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        List<WorkflowPart> workflowParts = new ArrayList<>();
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL);
        PlmSearchPart plmSearchPart = plmApiTestUtil.getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .isNotificationsIncluded(false, false, "")
            .isPublishResultsAttachReportInclude(false, "")
            .isPublishResultsWriteFieldsInclude(false)
            .build();
        workflowParts.add(WorkflowPart.builder()
            .id(plmSearchPart.getId())
            .relativeCadFilePath(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup() + "/piston_pin.prt.1")
            .costingInputs(CostingInputs.builder()
                .scenarioName(scenarioName)
                .processGroupName(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
                .materialName(MaterialNameEnum.ABS.getMaterialName())
                .vpeName(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
                .build())
            .build());

        workflowParts.add(WorkflowPart.builder()
            .id("INVALID")
            .relativeCadFilePath(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup() + "/autoparttorso.prt.1")
            .costingInputs(CostingInputs.builder()
                .scenarioName(scenarioName)
                .processGroupName(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
                .materialName(MaterialNameEnum.ABS.getMaterialName())
                .vpeName(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
                .build())
            .build());

        this.workflowPartsRequestDataBuilder = WorkflowParts.builder().parts(workflowParts).build();

        AgentWorkflowJobResults agentWorkflowJobResult = this.createRestWorkflowAndGetJobResult();
        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(2);
        AgentWorkflowJobPartsResult validPartResult = CicApiTestUtil.getMatchedPlmPartResultByPartId(agentWorkflowJobResult, plmSearchPart.getId());
        softAssertions.assertThat(validPartResult.getInput().getProcessGroupName()).isEqualTo(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup());
        softAssertions.assertThat(validPartResult.getInput().getVpeName()).isEqualTo(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory());

        AgentWorkflowJobPartsResult invalidPartResult = CicApiTestUtil.getMatchedPlmPartResultByPartId(agentWorkflowJobResult, "INVALID");
        softAssertions.assertThat(invalidPartResult.getCicStatus()).isEqualTo("CAD_DOWNLOAD_FAILED");
    }

    @Test
    @TestRail(id = {26889})
    @Description("RunPartList - nonexistent file, valid format")
    public void testRunPartsWithInvalidFilePath() {
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .isNotificationsIncluded(false, false, "")
            .isPublishResultsAttachReportInclude(false, "")
            .isPublishResultsWriteFieldsInclude(false)
            .build();
        PlmSearchPart plmSearchPart = new PlmApiTestUtil().getPlmPartByPartNumber(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PARTIAL).getPlmPartNumber());
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id(plmSearchPart.getId())
                .relativeCadFilePath("invalid/piston_pin.prt.1")
                .costingInputs(CostingInputs.builder().build())
                .build()))
            .build();

        AgentWorkflowJobResults agentWorkflowJobResult = this.createRestWorkflowAndGetJobResult();
        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getCicStatus()).isEqualTo("CAD_DOWNLOAD_FAILED");
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getErrorMessage()).isEqualTo("CAD file 'invalid/piston_pin.prt.1' does not exist.");
    }

    @Test
    @TestRail(id = {26896})
    @Description("RunPartList - nonexistent file, valid format, no PLM CAD data")
    public void testRunPartsWithInvalidPart() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .isNotificationsIncluded(false, false, "")
            .isPublishResultsAttachReportInclude(false, "")
            .isPublishResultsWriteFieldsInclude(false)
            .build();
        PlmSearchPart plmSearchPart = new PlmApiTestUtil().getPlmPartByPartNumber("0000003908");
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id(plmSearchPart.getId())
                .relativeCadFilePath(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup() + "/nonexistent.prt.1")
                .costingInputs(CostingInputs.builder()
                    .scenarioName(scenarioName)
                    .processGroupName(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
                    .materialName(MaterialNameEnum.ABS.getMaterialName())
                    .vpeName(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
                    .build())
                .build()))
            .build();

        AgentWorkflowJobResults agentWorkflowJobResult = this.createRestWorkflowAndGetJobResult();
        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getCicStatus()).isEqualTo("CAD_DOWNLOAD_FAILED");
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getErrorMessage()).isEqualTo("CAD file 'Plastic Molding/nonexistent.prt.1' does not exist.");
    }

    @Test
    @TestRail(id = {26895})
    @Description("RunPartList - valid file path no PLM CAD data")
    public void testRunPartsFileWithNoCadData() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .isNotificationsIncluded(false, false, "")
            .isPublishResultsAttachReportInclude(false, "")
            .isPublishResultsWriteFieldsInclude(false)
            .build();
        PlmSearchPart plmSearchPart = new PlmApiTestUtil().getPlmPartByPartNumber("0000003908");
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id(plmSearchPart.getId())
                .relativeCadFilePath(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup() + "/piston_pin.prt.1")
                .costingInputs(CostingInputs.builder()
                    .scenarioName(scenarioName)
                    .processGroupName(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
                    .materialName(MaterialNameEnum.ABS.getMaterialName())
                    .vpeName(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
                    .build())
                .build()))
            .build();

        AgentWorkflowJobResults agentWorkflowJobResult = this.createRestWorkflowAndGetJobResult();
        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getProcessGroupName()).isEqualTo(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getVpeName()).isEqualTo(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory());

    }

    @Test
    @TestRail(id = {26898})
    @Description("RunPartList - valid file path and request containing at least 1 valid PLM Part ID and 1 invalid PLM Part ID - PLM read functions correctly ")
    public void testRunPartsWithValidAndInvalidFilePlmRead() {
        String scenarioName = new GenerateStringUtil().generateScenarioName();
        List<WorkflowPart> workflowParts = new ArrayList<>();
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PARTIAL);
        PlmSearchPart plmSearchPart = plmApiTestUtil.getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.MAPPED_FROM_PLM, "")
            .isNotificationsIncluded(false, false, "")
            .isPublishResultsAttachReportInclude(false, "")
            .isPublishResultsWriteFieldsInclude(false)
            .build();
        workflowParts.add(WorkflowPart.builder()
            .id(plmSearchPart.getId())
            .relativeCadFilePath(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup() + "/piston_pin.prt.1")
            .costingInputs(CostingInputs.builder().build())
            .build());

        workflowParts.add(WorkflowPart.builder()
            .id("INVALID")
            .relativeCadFilePath(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup() + "/autoparttorso.prt.1")
            .costingInputs(CostingInputs.builder()
                .scenarioName(scenarioName)
                .processGroupName(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
                .materialName(MaterialNameEnum.ABS.getMaterialName())
                .vpeName(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
                .build())
            .build());

        this.workflowPartsRequestDataBuilder = WorkflowParts.builder().parts(workflowParts).build();

        AgentWorkflowJobResults agentWorkflowJobResult = this.createRestWorkflowAndGetJobResult();
        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(2);
        AgentWorkflowJobPartsResult validPartResult = CicApiTestUtil.getMatchedPlmPartResultByPartId(agentWorkflowJobResult, plmSearchPart.getId());
        softAssertions.assertThat(validPartResult.getInput().getProcessGroupName()).isNotNull();
        softAssertions.assertThat(validPartResult.getInput().getVpeName()).isNotNull();

        AgentWorkflowJobPartsResult invalidPartResult = CicApiTestUtil.getMatchedPlmPartResultByPartId(agentWorkflowJobResult, "INVALID");
        softAssertions.assertThat(invalidPartResult.getCicStatus()).isEqualTo("CAD_DOWNLOAD_FAILED");
    }

    @Test
    @TestRail(id = {26891, 26892, 26890})
    @Description("RunPartList - valid file path Watchpoint report action successful and valid file path DTC Component Summary Report action successful and PLM Write")
    public void testRunPartsValidFileDTCCSR() {
        String scenarioName = PropertiesContext.get("customer") + RandomStringUtils.randomNumeric(6);
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL);
        plmPartData.setPartName("piston_pin");
        plmPartData.setScenarioName(scenarioName);
        plmPart = plmApiTestUtil.getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        plmApiTestUtil.updatePartInfoToPlm(plmPart.getId(), plmFieldDefinitions, "Update part to reset the data");
        this.cicLogin();
        AgentWorkflowReportTemplates reportTemplateNames = CicApiTestUtil.getAgentReportTemplates(CICReportType.EMAIL, this.cicLoginUtil.getWebSession());
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "NULL")
            .isNotificationsIncluded(true, true,  CicApiTestUtil.getAgentReportTemplate(reportTemplateNames, ReportsEnum.DTC_COMPONENT_SUMMARY).getValue())
            .addNotificationAttachReportRow(PlmTypeAttributes.PLM_CURRENCY_CODE)
            .addNotificationAttachReportRow(PlmTypeAttributes.PLM_COST_ROUNDING)
            .isPublishResultsWriteFieldsInclude(true)
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_MATERIAL_NAME, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_PROCESS_GROUP, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_DIGITAL_FACTORY, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_ANNUAL_VOLUME, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_BATCH_SIZE, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .isPublishResultsAttachReportInclude(false, "")
            .build();

        PlmSearchPart plmSearchPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id(plmSearchPart.getId())
                .relativeCadFilePath(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup() + "/piston_pin.prt.1")
                .costingInputs(CostingInputs.builder()
                    .processGroupName(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
                    .materialName(MaterialNameEnum.ABS.getMaterialName())
                    .vpeName(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
                    .scenarioName(scenarioName)
                    .build())
                .build()))
            .build();

        AgentWorkflowJobResults agentWorkflowJobResult = this.create()
            .getWorkflowId()
            .invokeRestWorkflow()
            .track()
            .getJobResult();

        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);

        EmailMessage emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        softAssertions.assertThat(emailMessage.getBody().getContent().contains(scenarioName)).isTrue();
        EmailMessageAttachments emailMessageAttachments = emailMessage.emailMessageAttachments();
        softAssertions.assertThat(this.verifyEmailAttachedReportName(emailMessageAttachments, Collections.singletonList(plmPartData))).isTrue();
        softAssertions.assertThat(this.verifyPdfDocumentContent(emailMessageAttachments, Collections.singletonList(plmPartData))).isTrue();

        plmPart = plmApiTestUtil.getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        PlmPartResponse plmPartResponse = plmApiTestUtil.plmCsrfToken().getPartInfoFromPlm(plmPart.getId()).getPlmPartResponse();
        softAssertions.assertThat(plmPartResponse).isNotNull();
        softAssertions.assertThat(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup()).isEqualTo(plmPartResponse.getApPG());
        softAssertions.assertThat(MaterialNameEnum.ABS.getMaterialName()).isEqualTo(plmPartResponse.getApMaterial());
    }

    @Test
    @TestRail(id = {26893})
    @Description("RunPartList - valid file path DFM Multiple Component Summary Report action successful")
    public void testRunPartsValidFileDFMCSR() {
        String scenarioName = PropertiesContext.get("customer") + RandomStringUtils.randomNumeric(6);
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PARTIAL);
        this.cicLogin();
        AgentWorkflowReportTemplates reportTemplateNames = CicApiTestUtil.getAgentReportTemplates(CICReportType.EMAIL, this.cicLoginUtil.getWebSession());
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "NULL")
            .isNotificationsIncluded(true, true,  CicApiTestUtil.getAgentReportTemplate(reportTemplateNames, ReportsEnum.DTC_MULTIPLE_COMPONENT_SUMMARY).getValue())
            .addNotificationAttachReportRow(PlmTypeAttributes.PLM_COST_METRIC)
            .addNotificationAttachReportRow(PlmTypeAttributes.PLM_CURRENCY_CODE)
            .addNotificationAttachReportRow(PlmTypeAttributes.PLM_MASS_METRIC)
            .addNotificationAttachReportRow(PlmTypeAttributes.PLM_RISK_RATING)
            .addNotificationAttachReportRow(PlmTypeAttributes.PLM_SORT_METRIC)
            .isPublishResultsWriteFieldsInclude(true)
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_MATERIAL_NAME, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_PROCESS_GROUP, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_DIGITAL_FACTORY, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_ANNUAL_VOLUME, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .addPublishResultsWriteFieldsRow(PlmTypeAttributes.PLM_BATCH_SIZE, PublishResultsWriteRule.WORKFLOW_GENERATED_VALUE, "")
            .isPublishResultsAttachReportInclude(false, "")
            .build();

        PlmSearchPart plmSearchPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id(plmSearchPart.getId())
                .relativeCadFilePath(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup() + "/piston_pin.prt.1")
                .costingInputs(CostingInputs.builder()
                    .processGroupName(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
                    .materialName(MaterialNameEnum.ABS.getMaterialName())
                    .vpeName(DigitalFactoryEnum.APRIORI_USA.getDigitalFactory())
                    .scenarioName(scenarioName)
                    .build())
                .build()))
            .build();

        AgentWorkflowJobResults agentWorkflowJobResult = this.create()
            .getWorkflowId()
            .invokeRestWorkflow()
            .track()
            .getJobResult();

        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);

        EmailMessage emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        softAssertions.assertThat(emailMessage.getBody().getContent().contains(scenarioName)).isTrue();
        EmailMessageAttachments emailMessageAttachments = emailMessage.emailMessageAttachments();
        softAssertions.assertThat(this.verifyEmailAttachedReportName(emailMessageAttachments, Collections.singletonList(plmPartData))).isTrue();
    }

    @AfterEach
    public void cleanup() {
        softAssertions.assertAll();
        this.deleteWorkflow();
        this.close();
    }
}
