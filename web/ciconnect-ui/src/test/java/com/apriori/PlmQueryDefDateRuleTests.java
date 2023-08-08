package com.apriori;

import com.apriori.cic.enums.CICPartSelectionType;
import com.apriori.cic.enums.PlmPartDataType;
import com.apriori.cic.enums.QueryDefinitionFieldType;
import com.apriori.cic.enums.QueryDefinitionFields;
import com.apriori.cic.models.response.AgentWorkflowJobResults;
import com.apriori.cic.utils.PlmPartsUtil;
import com.apriori.cic.utils.WorkflowDataUtil;
import com.apriori.cic.utils.WorkflowTestUtil;
import com.apriori.http.utils.DateUtil;
import com.apriori.pageobjects.home.CIConnectHome;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlmQueryDefDateRuleTests extends WorkflowTestUtil {

    private static final String QUERY_STRING_FIELD_VALUE = "%auto test \\- query%";
    private static SoftAssertions softAssertions;
    private CIConnectHome ciConnectHome;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(id = {24381})
    @Description("Test each operator for the Int data type in isolation - Date Not Equal")
    public void testWorkflowQueryDefDateNotEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.DATE_TIME1, QueryDefinitionFieldType.NOT_EQUAL, DateUtil.getDateInMilliSeconds("04/21/2023 08:00:00", "GMT"))
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(4);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_NO_PARTS).getPlmPartNumber()))).isTrue();
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_INTEGER).getPlmPartNumber()))).isTrue();
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_REAL).getPlmPartNumber()))).isTrue();
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_STRING).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(id = {4149, 4873})
    @Description("Test each operator for the Int data type in isolation - Date Equal")
    public void testWorkflowQueryDefDateEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.DATE_TIME1, QueryDefinitionFieldType.EQUAL, DateUtil.getDateInMilliSeconds("04/21/2023 08:00:00", "GMT"))
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(id = {24386})
    @Description("Test each operator for the Int data type in isolation - Date Greater Than")
    public void testWorkflowQueryDefDateGreaterThan() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.DATE_TIME1, QueryDefinitionFieldType.GREATER_THAN, DateUtil.getDateInMilliSeconds("04/24/2023 08:00:00", "GMT"))
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_NO_PARTS).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(id = {24391})
    @Description("Test each operator for the Int data type in isolation - Date Greater Than or Equal")
    public void testWorkflowQueryDefDateGreaterThanEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.DATE_TIME1, QueryDefinitionFieldType.GREATER_THAN_EQUAL, DateUtil.getDateInMilliSeconds("04/24/2023 08:00:00", "GMT"))
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(2);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_NO_PARTS).getPlmPartNumber()))).isTrue();
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_STRING).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(id = {24396})
    @Description("Test each operator for the Int data type in isolation - Date Between")
    public void testWorkflowQueryDefDateBetween() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.DATE_TIME1, QueryDefinitionFieldType.BETWEEN, DateUtil.getDateInMilliSeconds("04/22/2023 08:00:00", "GMT"), DateUtil.getDateInMilliSeconds("04/23/2023 08:00:00", "GMT"))
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(2);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_INTEGER).getPlmPartNumber()))).isTrue();
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_REAL).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(id = {24401})
    @Description("Test each operator for the Int data type in isolation - Date Not Between")
    public void testWorkflowQueryDefDateNotBetween() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.DATE_TIME1, QueryDefinitionFieldType.NOT_BETWEEN, DateUtil.getDateInMilliSeconds("04/22/2023 08:00:00", "GMT"), DateUtil.getDateInMilliSeconds("04/24/2023 08:00:00", "GMT"))
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(2);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_NO_PARTS).getPlmPartNumber()))).isTrue();
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(id = {24404})
    @Description("Test each operator for the Int data type in isolation - Date Less Than")
    public void testWorkflowQueryDefDateLessThan() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.DATE_TIME1, QueryDefinitionFieldType.LESS_THAN, DateUtil.getDateInMilliSeconds("04/22/2023 08:00:00", "GMT"))
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(id = {24407})
    @Description("Test each operator for the Int data type in isolation - Date Less Than Equal")
    public void testWorkflowQueryDefDateLessThanEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.DATE_TIME1, QueryDefinitionFieldType.LESS_THAN_EQUAL, DateUtil.getDateInMilliSeconds("04/22/2023 08:00:00", "GMT"))
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(2);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_REAL).getPlmPartNumber()))).isTrue();
    }

    @AfterEach
    public void cleanup() {
        this.deleteWorkflow();
        softAssertions.assertAll();
    }
}
