package com.cic.tests;

import com.apriori.cidappapi.entity.builder.ComponentInfoBuilder;
import com.apriori.cidappapi.entity.response.componentiteration.ComponentIteration;
import com.apriori.cidappapi.utils.ComponentsUtil;
import com.apriori.cidappapi.utils.IterationsUtil;
import com.apriori.pages.home.CIConnectHome;
import com.apriori.pages.login.CicLoginPage;
import com.apriori.utils.DateFormattingUtils;
import com.apriori.utils.DateUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.MaterialNameEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.part.PartData;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import entity.request.JobDefinition;
import entity.request.WorkflowRequest;
import entity.response.AgentWorkflow;
import entity.response.AgentWorkflowJobPartsResult;
import entity.response.AgentWorkflowJobResults;
import entity.response.AgentWorkflowJobRun;
import entity.response.PlmSearchPart;
import enums.CICPartSelectionType;
import enums.CostingInputFields;
import enums.MappingRule;
import enums.PlmPartDataType;
import enums.PlmPartsSearch;
import enums.PlmWCType;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.CicApiTestUtil;
import utils.PlmPartsUtil;
import utils.SearchFilter;
import utils.WorkflowDataUtil;
import utils.WorkflowTestUtil;

public class PlmUdaTests extends WorkflowTestUtil {
    private final IterationsUtil iterationsUtil = new IterationsUtil();
    private static SoftAssertions softAssertions;
    private static PartData plmPartData;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(testCaseId = {"5074", "22711"})
    @Description("Test setting multiselect UDA using mapping rule 'Mapped from PLM' with value set in PLM System, " +
        "Test reading UDA of all data types with mapping rule 'Mapped from PLM' with value set in PLM")
    public void testMultiSelectUdaMapSetInPlm() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MAPPED).getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.SCENARIO_NAME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.ANNUAL_VOLUME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.BATCH_SIZE, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.PRODUCTION_LIFE, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.DESCRIPTION, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.CUSTOM_MULTI, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.CUSTOM_STRING, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.CUSTOM_DATE, MappingRule.MAPPED_FROM_PLM, "")
            .build();

        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(),
                workflowRequestDataBuilder.getQuery().getFilters().getFilters().get(0).getValue()))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmSearchPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeQueryWorkflow()
            .track()
            .getJobPartResult(plmPart.getId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getCidPartLink()).isNotNull();

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(
            ComponentInfoBuilder.builder()
                .componentIdentity(StringUtils.substringBetween(agentWorkflowJobPartsResult.getCidPartLink(), "components/", "/scenarios"))
                .scenarioIdentity(StringUtils.substringAfterLast(agentWorkflowJobPartsResult.getCidPartLink(), "/"))
                .user(currentUser)
                .build());

        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "[Value 1, Value 3]")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "PLM Test")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "[Value 2]")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "2023-04-02")).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"5218", "4411"})
    @Description("Test setting multiselect UDA using mapping rule 'Mapped from PLM' with NO value set in PLM System," +
        "Test reading UDA of all data types with mapping rule 'Mapped from PLM' with NO value set in PLM")
    public void testMultiSelectUdaMapNotInPlm() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_NOT_MAPPED);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.SCENARIO_NAME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.ANNUAL_VOLUME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.BATCH_SIZE, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.PRODUCTION_LIFE, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.DESCRIPTION, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.CUSTOM_MULTI, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.CUSTOM_STRING, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.CUSTOM_NUMBER, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.CUSTOM_DATE, MappingRule.MAPPED_FROM_PLM, "")
            .build();

        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(),
                plmPartData.getPlmPartNumber()))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmSearchPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeQueryWorkflow()
            .track()
            .getJobPartResult(plmPart.getId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getCidPartLink()).isNotNull();

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(
            ComponentInfoBuilder.builder()
                .componentIdentity(StringUtils.substringBetween(agentWorkflowJobPartsResult.getCidPartLink(), "components/", "/scenarios"))
                .scenarioIdentity(StringUtils.substringAfterLast(agentWorkflowJobPartsResult.getCidPartLink(), "/"))
                .user(currentUser)
                .build());

        softAssertions.assertThat(componentIterationResponse.getResponseEntity().getScenarioCustomAttributes().size()).isEqualTo(1);
    }

    @Test
    @TestRail(testCaseId = {"22709", "22713"})
    @Description("Test setting multiselect UDA using mapping rule 'Default if NO PLM Value' with Value set in PLM system, " +
        "Test reading UDA of all data types with mapping rule 'Default if no PLM value' with value set in PLM")
    public void testMultiSelectUdaMapDefaultSetInPlm() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MAPPED);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.DEFAULT_NO_PLM_VALUE, plmPartData.getProcessGroup())
            .addCostingInputRow(CostingInputFields.SCENARIO_NAME, MappingRule.DEFAULT_NO_PLM_VALUE, "scenario name 1234")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.DEFAULT_NO_PLM_VALUE, "aPriori Brazil")
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.DEFAULT_NO_PLM_VALUE, plmPartData.getMaterial())
            .addCostingInputRow(CostingInputFields.ANNUAL_VOLUME, MappingRule.DEFAULT_NO_PLM_VALUE, "3100")
            .addCostingInputRow(CostingInputFields.BATCH_SIZE, MappingRule.DEFAULT_NO_PLM_VALUE, "2100")
            .addCostingInputRow(CostingInputFields.PRODUCTION_LIFE, MappingRule.DEFAULT_NO_PLM_VALUE, "1")
            .addCostingInputRow(CostingInputFields.DESCRIPTION, MappingRule.DEFAULT_NO_PLM_VALUE, "description")
            .addCostingInputRow(CostingInputFields.CUSTOM_MULTI, MappingRule.DEFAULT_NO_PLM_VALUE, "[\"Value 1\"]")
            .addCostingInputRow(CostingInputFields.CUSTOM_STRING, MappingRule.DEFAULT_NO_PLM_VALUE, "WF Test")
            .addCostingInputRow(CostingInputFields.CUSTOM_NUMBER, MappingRule.DEFAULT_NO_PLM_VALUE, "123")
            .addCostingInputRow(CostingInputFields.CUSTOM_DATE, MappingRule.DEFAULT_NO_PLM_VALUE, DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ))
            .build();

        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(),
                plmPartData.getPlmPartNumber()))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmSearchPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeQueryWorkflow()
            .track()
            .getJobPartResult(plmPart.getId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getCidPartLink()).isNotNull();
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getMaterialName()).isEqualTo(plmPartData.getMaterial());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProcessGroupName()).isEqualTo(plmPartData.getProcessGroup());

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(
            ComponentInfoBuilder.builder()
                .componentIdentity(StringUtils.substringBetween(agentWorkflowJobPartsResult.getCidPartLink(), "components/", "/scenarios"))
                .scenarioIdentity(StringUtils.substringAfterLast(agentWorkflowJobPartsResult.getCidPartLink(), "/"))
                .user(currentUser)
                .build());

        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "[Value 1, Value 3]")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "PLM Test")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "777")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "2023-04-02")).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"22710", "22712"})
    @Description("Test setting multiselect UDA using mapping rule 'Default if no PLM value' with NO value set in PLM System, " +
        " \"Test reading UDA of all data types with mapping rule 'Default if no PLM value' with NO value set in PLM\"")
    public void testMultiSelectUdaNotMappedInPlm() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_NOT_MAPPED);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.DEFAULT_NO_PLM_VALUE, plmPartData.getProcessGroup())
            .addCostingInputRow(CostingInputFields.SCENARIO_NAME, MappingRule.DEFAULT_NO_PLM_VALUE, "scenario name 1234")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.DEFAULT_NO_PLM_VALUE, DigitalFactoryEnum.APRIORI_BRAZIL.getDigitalFactory())
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.DEFAULT_NO_PLM_VALUE, plmPartData.getMaterial())
            .addCostingInputRow(CostingInputFields.ANNUAL_VOLUME, MappingRule.DEFAULT_NO_PLM_VALUE, "3100")
            .addCostingInputRow(CostingInputFields.BATCH_SIZE, MappingRule.DEFAULT_NO_PLM_VALUE, "2100")
            .addCostingInputRow(CostingInputFields.PRODUCTION_LIFE, MappingRule.DEFAULT_NO_PLM_VALUE, "1")
            .addCostingInputRow(CostingInputFields.DESCRIPTION, MappingRule.DEFAULT_NO_PLM_VALUE, "description")
            .addCostingInputRow(CostingInputFields.CUSTOM_MULTI, MappingRule.DEFAULT_NO_PLM_VALUE, "[\"Value 2\"]")
            .addCostingInputRow(CostingInputFields.CUSTOM_STRING, MappingRule.DEFAULT_NO_PLM_VALUE, "WF Test")
            .addCostingInputRow(CostingInputFields.CUSTOM_NUMBER, MappingRule.DEFAULT_NO_PLM_VALUE, "123")
            .addCostingInputRow(CostingInputFields.CUSTOM_DATE, MappingRule.DEFAULT_NO_PLM_VALUE, DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ))
            .build();

        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(),
                plmPartData.getPlmPartNumber()))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmSearchPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeQueryWorkflow()
            .track()
            .getJobPartResult(plmPart.getId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getMaterialName()).isEqualTo(plmPartData.getMaterial());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProcessGroupName()).isEqualTo(plmPartData.getProcessGroup());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getScenarioName()).isEqualTo("scenario name 1234");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getCidPartLink()).isNotNull();

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(
            ComponentInfoBuilder.builder()
                .componentIdentity(StringUtils.substringBetween(agentWorkflowJobPartsResult.getCidPartLink(), "components/", "/scenarios"))
                .scenarioIdentity(StringUtils.substringAfterLast(agentWorkflowJobPartsResult.getCidPartLink(), "/"))
                .user(currentUser)
                .build());

        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "[Value 2]")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "WF Test")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "123")).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"10511"})
    @Description("Invalid value for Digital Factory read from PLM system results in part processing failure and useful error message")
    public void testMultiSelectUdaWithInvalidVpe() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_INVALID_VPE);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.SCENARIO_NAME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.MAPPED_FROM_PLM, "")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResult = this.createQueryWorkflowAndGetJobResult();

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = CicApiTestUtil.getMatchedPlmPartResult(agentWorkflowJobResult, plmPartData.getPlmPartNumber());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getCicStatus()).isEqualTo("ERRORED");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getErrorMessage()).contains(String.format("Unable to find active digital factory with name '%s'", plmPartData.getDigitalFactory()));

    }

    @Test
    @TestRail(testCaseId = {"10512"})
    @Description("Invalid value for Process Group read from PLM system results in part processing failure and useful error message")
    public void testMultiSelectUdaWithInvalidProcessGroup() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_INVALID_PG);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.SCENARIO_NAME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.ANNUAL_VOLUME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.BATCH_SIZE, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.DESCRIPTION, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.CUSTOM_MULTI, MappingRule.MAPPED_FROM_PLM, "")
            .build();

        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(),
                plmPartData.getPlmPartNumber()))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmSearchPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeQueryWorkflow()
            .track()
            .getJobPartResult(plmPart.getId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getCicStatus()).isEqualTo("ERRORED");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getErrorMessage()).contains(String.format("Unable to find process group with name '%s'", plmPartData.getProcessGroup()));
    }

    @Test
    @TestRail(testCaseId = {"5076"})
    @Description("Test invalid value for multiselect UDA read from PLM")
    public void testMultiSelectUdaWithInvalidUda() {
        PartData plmInvalidUdaFormatData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_INVALID_UDA_FORMAT);
        PartData plmInvalidUdaValueData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_INVALID_UDA_VALUE);

        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmInvalidUdaFormatData.getPlmPartNumber())
            .setQueryFilter("partNumber", "EQ", plmInvalidUdaValueData.getPlmPartNumber())
            .setQueryFilters("OR")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.SCENARIO_NAME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.ANNUAL_VOLUME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.BATCH_SIZE, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.DESCRIPTION, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.CUSTOM_MULTI, MappingRule.MAPPED_FROM_PLM, "")
            .build();

        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(),
                plmInvalidUdaFormatData.getPlmPartNumber()))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmSearchPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        AgentWorkflowJobPartsResult jobPartsResultInvalidFormat = this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeQueryWorkflow()
            .track()
            .getJobPartResult(plmPart.getId());

        softAssertions.assertThat(jobPartsResultInvalidFormat.getCicStatus()).isEqualTo("ERRORED");
        softAssertions.assertThat(jobPartsResultInvalidFormat.getErrorMessage()).contains(String.format("Custom attribute with name 'UDA4' and value '%s' does not match any of the allowed values", plmInvalidUdaFormatData.getUdas()));

        SearchFilter searchFilter1 = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(),
                plmInvalidUdaValueData.getPlmPartNumber()))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmSearchPart plmInvalidValuePart = CicApiTestUtil.getPlmPart(searchFilter);

        AgentWorkflowJobPartsResult jobPartsResultInvalidValue = this.getJobPartResult(plmInvalidValuePart.getId());

        softAssertions.assertThat(jobPartsResultInvalidValue.getCicStatus()).isEqualTo("ERRORED");
        softAssertions.assertThat(jobPartsResultInvalidValue.getErrorMessage()).contains("Custom attribute with name 'UDA4' and value 'invalid value 2' does not match any of the allowed values");

    }

    @Test
    @Issue("APG-981")
    @TestRail(testCaseId = {"10513"})
    @Description("Invalid value for Material read from PLM system results in part processing failure and useful error message")
    public void testMultiSelectUdaWithInvalidMaterial() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_INVALID_MATERIAL);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.SCENARIO_NAME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.ANNUAL_VOLUME, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.BATCH_SIZE, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.DESCRIPTION, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.CUSTOM_MULTI, MappingRule.MAPPED_FROM_PLM, "")
            .build();

        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(),
                plmPartData.getPlmPartNumber()))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmSearchPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeQueryWorkflow()
            .track()
            .getJobPartResult(plmPart.getId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getCicStatus()).isEqualTo("ERRORED");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getErrorMessage()).contains(String.format("Unable to find material with name '%s'", plmPartData.getMaterial()));
    }

    @Test
    @TestRail(testCaseId = {"5073", "22716"})
    @Description("Test setting multiselect UDA using mapping rule constant, " +
        " UDAs of all data types can be set in workflow with mapping rule constant")
    public void testMultiSelectUdaConstantMappedRule() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_INVALID_UDA_VALUE);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup())
            .addCostingInputRow(CostingInputFields.SCENARIO_NAME, MappingRule.CONSTANT, "scenario name 1234")
            .addCostingInputRow(CostingInputFields.DIGITAL_FACTORY, MappingRule.CONSTANT, DigitalFactoryEnum.APRIORI_BRAZIL.getDigitalFactory())
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.CONSTANT, MaterialNameEnum.STAINLESS_STEEL_AISI_316.getMaterialName())
            .addCostingInputRow(CostingInputFields.ANNUAL_VOLUME, MappingRule.CONSTANT, "3100")
            .addCostingInputRow(CostingInputFields.BATCH_SIZE, MappingRule.CONSTANT, "2100")
            .addCostingInputRow(CostingInputFields.PRODUCTION_LIFE, MappingRule.CONSTANT, "1")
            .addCostingInputRow(CostingInputFields.DESCRIPTION, MappingRule.CONSTANT, "description")
            .addCostingInputRow(CostingInputFields.CUSTOM_MULTI, MappingRule.CONSTANT, "[\"Value 1\",\"Value 3\"]")
            .addCostingInputRow(CostingInputFields.CUSTOM_STRING, MappingRule.CONSTANT, "Constant Test")
            .addCostingInputRow(CostingInputFields.CUSTOM_NUMBER, MappingRule.CONSTANT, "777")
            .addCostingInputRow(CostingInputFields.CUSTOM_DATE, MappingRule.CONSTANT, DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMddTHHmmssSSSZ))
            .build();

        SearchFilter searchFilter = new SearchFilter()
            .buildParameter(PlmPartsSearch.PLM_WC_PART_FILTER.getFilterKey() + String.format(PlmPartsSearch.PLM_WC_PART_NUMBER_EQ.getFilterKey(),
                plmPartData.getPlmPartNumber()))
            .buildParameter(PlmPartsSearch.PLM_WC_PART_TYPE_ID.getFilterKey() + PlmWCType.PLM_WC_PART_TYPE.getPartType())
            .build();
        PlmSearchPart plmPart = CicApiTestUtil.getPlmPart(searchFilter);
        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeQueryWorkflow()
            .track()
            .getJobPartResult(plmPart.getId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getCidPartLink()).isNotNull();
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProcessGroupName()).isEqualTo(ProcessGroupEnum.PLASTIC_MOLDING.getProcessGroup());

        ResponseWrapper<ComponentIteration> componentIterationResponse = iterationsUtil.getComponentIterationLatest(
            ComponentInfoBuilder.builder()
                .componentIdentity(StringUtils.substringBetween(agentWorkflowJobPartsResult.getCidPartLink(), "components/", "/scenarios"))
                .scenarioIdentity(StringUtils.substringAfterLast(agentWorkflowJobPartsResult.getCidPartLink(), "/"))
                .user(currentUser)
                .build());

        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "[Value 1, Value 3]")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "Constant Test")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), "777")).isTrue();
        softAssertions.assertThat(new ComponentsUtil().checkScenarioCustomAttribute(componentIterationResponse.getResponseEntity(), DateUtil.getCurrentDate(DateFormattingUtils.dtf_yyyyMMdd))).isTrue();

    }

    @After
    public void cleanup() {
        this.deleteWorkflow();
        softAssertions.assertAll();
    }
}
