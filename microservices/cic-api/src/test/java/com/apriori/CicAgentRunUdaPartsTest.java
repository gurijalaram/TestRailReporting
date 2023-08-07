package com.apriori;

import com.apriori.cic.enums.CICPartSelectionType;
import com.apriori.cic.enums.CostingInputFields;
import com.apriori.cic.enums.MappingRule;
import com.apriori.cic.enums.PlmPartDataType;
import com.apriori.cic.models.request.CostingInputs;
import com.apriori.cic.models.request.WorkflowPart;
import com.apriori.cic.models.request.WorkflowParts;
import com.apriori.cic.models.response.AgentWorkflowJobPartsResult;
import com.apriori.cic.models.response.AgentWorkflowJobResults;
import com.apriori.cic.models.response.PlmSearchPart;
import com.apriori.cic.utils.CicApiTestUtil;
import com.apriori.cic.utils.PlmApiTestUtil;
import com.apriori.cic.utils.PlmPartsUtil;
import com.apriori.cic.utils.WorkflowDataUtil;
import com.apriori.cic.utils.WorkflowTestUtil;
import com.apriori.cidappapi.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.models.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.IterationsUtil;
import com.apriori.enums.DigitalFactoryEnum;
import com.apriori.enums.MaterialNameEnum;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.http.utils.ResponseWrapper;
import com.apriori.reader.file.part.PartData;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class CicAgentRunUdaPartsTest extends WorkflowTestUtil {

    private final IterationsUtil iterationsUtil = new IterationsUtil();
    private SoftAssertions softAssertions;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(id = 16699)
    @Description("RunPartList - all standard costing inputs overridden by runPartList request when all costing inputs are set in workflow to be read from PLM")
    public void testWorkflowRunPartsOverriddenWithMapped() {
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.MAPPED_FROM_PLM, "")
            .build();
        this.workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartsDataBuilder(PlmPartDataType.PLM_MAPPED);

        AgentWorkflowJobResults agentWorkflowJobResult = this.createRestWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getMaterialName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getProcessGroupName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getVpeName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getAnnualVolume()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getBatchSize()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
    }

    @Test
    @TestRail(id = {16703, 16712})
    @Description("RunPartList - UDA of all supported data types can be overridden by runPartList request when they are set in workflow with constant value, " +
        " Searchable UDA set in runPartList")
    public void testWorkflowRunPartsOverriddenWithUdaConstant() {
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .addCostingInputRow(CostingInputFields.CUSTOM_NUMBER, MappingRule.CONSTANT, "222")
            .addCostingInputRow(CostingInputFields.CUSTOM_STRING, MappingRule.CONSTANT, "testValue1")
            .addCostingInputRow(CostingInputFields.CUSTOM_DATE, MappingRule.CONSTANT, DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ))
            .build();
        PartData plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_VALID_UDA);
        PlmSearchPart plmSearchPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id(plmSearchPart.getId())
                .costingInputs(CostingInputs.builder()
                    .customString("test value 2")
                    .customNumber("333")
                    .customDate(DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ))
                    .build())
                .build()))
            .build();

        this.cicLogin()
            .create()
            .getWorkflowId();

        softAssertions.assertThat(this.agentWorkflowResponse.getId()).isNotNull();

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.invokeRestWorkflow().track().getJobPartResult(plmSearchPart.getId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getCidPartLink()).isNotNull();

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(
            ComponentInfoBuilder.builder()
                .componentIdentity(StringUtils.substringBetween(agentWorkflowJobPartsResult.getCidPartLink(), "components/", "/scenarios"))
                .scenarioIdentity(StringUtils.substringAfterLast(agentWorkflowJobPartsResult.getCidPartLink(), "/"))
                .user(currentUser)
                .build());

        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "test value 2")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "333")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMdd))).isTrue();
    }

    @Test
    @TestRail(id = 16705)
    @Description("RunPartList- a subset of UDAs overridden by runPartList request when UDAs of all data types are set in workflow with constant value constant")
    public void testWorkflowRunPartsSubOverriddenWithUda() {
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .addCostingInputRow(CostingInputFields.CUSTOM_NUMBER, MappingRule.CONSTANT, "222")
            .addCostingInputRow(CostingInputFields.CUSTOM_STRING, MappingRule.CONSTANT, "testValue1")
            .addCostingInputRow(CostingInputFields.CUSTOM_DATE, MappingRule.CONSTANT, DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ))
            .build();
        PartData plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_VALID_UDA);
        PlmSearchPart plmSearchPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id(plmSearchPart.getId())
                .costingInputs(CostingInputs.builder()
                    .customString("test value 4")
                    .build())
                .build()))
            .build();

        this.cicLogin()
            .create()
            .getWorkflowId();

        softAssertions.assertThat(this.agentWorkflowResponse.getId()).isNotNull();

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.invokeRestWorkflow().track().getJobPartResult(plmSearchPart.getId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getCidPartLink()).isNotNull();

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(
            ComponentInfoBuilder.builder()
                .componentIdentity(StringUtils.substringBetween(agentWorkflowJobPartsResult.getCidPartLink(), "components/", "/scenarios"))
                .scenarioIdentity(StringUtils.substringAfterLast(agentWorkflowJobPartsResult.getCidPartLink(), "/"))
                .user(currentUser)
                .build());

        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "test value 4")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "333")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMdd))).isTrue();

    }

    @Test
    @TestRail(id = 16704)
    @Description("RunPartList - UDAs of all supported data types overridden by runPartList request when they set in workflow to be read from PLM")
    public void testWorkflowRunPartsOverriddenUdaMapped() {
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .addCostingInputRow(CostingInputFields.CUSTOM_NUMBER, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.CUSTOM_STRING, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.CUSTOM_DATE, MappingRule.MAPPED_FROM_PLM, "")
            .build();
        PartData plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_VALID_UDA);
        PlmSearchPart plmSearchPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id(plmSearchPart.getId())
                .costingInputs(CostingInputs.builder()
                    .customString("test rpl string")
                    .customNumber("777")
                    .customDate(DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ))
                    .build())
                .build()))
            .build();

        this.cicLogin()
            .create()
            .getWorkflowId();

        softAssertions.assertThat(this.agentWorkflowResponse.getId()).isNotNull();

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.invokeRestWorkflow().track().getJobPartResult(plmSearchPart.getId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getCidPartLink()).isNotNull();

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(
            ComponentInfoBuilder.builder()
                .componentIdentity(StringUtils.substringBetween(agentWorkflowJobPartsResult.getCidPartLink(), "components/", "/scenarios"))
                .scenarioIdentity(StringUtils.substringAfterLast(agentWorkflowJobPartsResult.getCidPartLink(), "/"))
                .user(currentUser)
                .build());

        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "test rpl string")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "777")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMdd))).isTrue();

    }

    @Test
    @TestRail(id = 16709)
    @Description("RunPartList - Multiselect UDA can be set with runPartList request")
    public void testWorkflowRunPartsWithMultiSelectUDA() {
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .addCostingInputRow(CostingInputFields.CUSTOM_NUMBER, MappingRule.CONSTANT, "222")
            .addCostingInputRow(CostingInputFields.CUSTOM_STRING, MappingRule.CONSTANT, "test rpl value1")
            .addCostingInputRow(CostingInputFields.CUSTOM_DATE, MappingRule.CONSTANT, DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ))
            .build();
        PartData plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_VALID_UDA);
        PlmSearchPart plmSearchPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id(plmSearchPart.getId())
                .costingInputs(CostingInputs.builder()
                    .customMulti("[\"Value 1\",\"Value 3\"]")
                    .build())
                .build()))
            .build();

        this.cicLogin()
            .create()
            .getWorkflowId();

        softAssertions.assertThat(this.agentWorkflowResponse.getId()).isNotNull();

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.invokeRestWorkflow().track().getJobPartResult(plmSearchPart.getId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getCidPartLink()).isNotNull();

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(
            ComponentInfoBuilder.builder()
                .componentIdentity(StringUtils.substringBetween(agentWorkflowJobPartsResult.getCidPartLink(), "components/", "/scenarios"))
                .scenarioIdentity(StringUtils.substringAfterLast(agentWorkflowJobPartsResult.getCidPartLink(), "/"))
                .user(currentUser)
                .build());

        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "test rpl value1")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "222")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMdd))).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "[Value 1, Value 3]")).isTrue();
    }

    @Test
    @TestRail(id = 16949)
    @Description("RunPartList - invalid file path")
    public void testRunPartsListWithInvalidCadFilePath() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .addCostingInputRow(CostingInputFields.SCENARIO_NAME, MappingRule.CONSTANT, "testScenario123")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.CONSTANT, DigitalFactoryEnum.APRIORI_BRAZIL.getDigitalFactory())
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.CONSTANT, MaterialNameEnum.STAINLESS_STEEL_AISI_316.getMaterialName())
            .addCostingInputRow(CostingInputFields.ANNUAL_VOLUME, MappingRule.CONSTANT, "3100")
            .addCostingInputRow(CostingInputFields.BATCH_SIZE, MappingRule.CONSTANT, "2100")
            .addCostingInputRow(CostingInputFields.PRODUCTION_LIFE, MappingRule.CONSTANT, "1")
            .build();

        PartData plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_VALID_UDA);
        PlmSearchPart plmSearchPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id(plmSearchPart.getId())
                .relativeCadFilePath("Invalid")
                .costingInputs(CostingInputs.builder()
                    .processGroupName(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
                    .materialName(MaterialNameEnum.ABS.getMaterialName())
                    .scenarioName("testScenario987")
                    .batchSize(5)
                    .annualVolume(8000)
                    .productionLife(8)
                    .build())
                .build()))
            .build();

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeRestWorkflow()
            .track()
            .getJobPartResult(plmSearchPart.getId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getCicStatus()).isEqualTo("CAD_DOWNLOAD_FAILED");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getErrorMessage()).contains("Unsupported file format");
    }

    @Test
    @TestRail(id = 16710)
    @Description("RunPartList - Override UDA with default value")
    public void testWorkflowRunPartsOverrideMultiSelectUDA() {
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .addCostingInputRow(CostingInputFields.CUSTOM_MULTI, MappingRule.CONSTANT, "[\"Value 1\"]")
            .build();
        PartData plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_VALID_UDA);
        PlmSearchPart plmSearchPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id(plmSearchPart.getId())
                .costingInputs(CostingInputs.builder()
                    .customMulti("[\"Value 1\",\"Value 3\"]")
                    .build())
                .build()))
            .build();

        this.cicLogin()
            .create()
            .getWorkflowId();

        softAssertions.assertThat(this.agentWorkflowResponse.getId()).isNotNull();

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.invokeRestWorkflow().track().getJobPartResult(plmSearchPart.getId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getCidPartLink()).isNotNull();

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(
            ComponentInfoBuilder.builder()
                .componentIdentity(StringUtils.substringBetween(agentWorkflowJobPartsResult.getCidPartLink(), "components/", "/scenarios"))
                .scenarioIdentity(StringUtils.substringAfterLast(agentWorkflowJobPartsResult.getCidPartLink(), "/"))
                .user(currentUser)
                .build());

        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "[Value 1, Value 3]")).isTrue();
    }

    @Test
    @TestRail(id = 16713)
    @Description("RunPartList - Non-searchable UDA set in runPartList")
    public void testWorkflowRunPartsWithNonSearchUda() {
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .addCostingInputRow(CostingInputFields.CUSTOM_STRING, MappingRule.CONSTANT, "testValue1")
            .build();
        PartData plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_VALID_UDA);
        PlmSearchPart plmSearchPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id(plmSearchPart.getId())
                .costingInputs(CostingInputs.builder()
                    .nonSearchUda("rpl test")
                    .build())
                .build()))
            .build();

        this.cicLogin()
            .create()
            .getWorkflowId();

        softAssertions.assertThat(this.agentWorkflowResponse.getId()).isNotNull();

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.invokeRestWorkflow().track().getJobPartResult(plmSearchPart.getId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getCidPartLink()).isNotNull();

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(
            ComponentInfoBuilder.builder()
                .componentIdentity(StringUtils.substringBetween(agentWorkflowJobPartsResult.getCidPartLink(), "components/", "/scenarios"))
                .scenarioIdentity(StringUtils.substringAfterLast(agentWorkflowJobPartsResult.getCidPartLink(), "/"))
                .user(currentUser)
                .build());

        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "rpl test")).isTrue();
    }

    @AfterEach
    public void cleanup() {
        this.deleteWorkflow();
        softAssertions.assertAll();
        this.close();
    }
}
