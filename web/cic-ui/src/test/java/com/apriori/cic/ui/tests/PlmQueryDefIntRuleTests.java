package com.apriori.cic.ui.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cic.api.enums.CICPartSelectionType;
import com.apriori.cic.api.enums.CostingInputFields;
import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmPartDataType;
import com.apriori.cic.api.enums.QueryDefinitionFieldType;
import com.apriori.cic.api.enums.QueryDefinitionFields;
import com.apriori.cic.api.models.response.AgentWorkflowJobResults;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.api.utils.WorkflowDataUtil;
import com.apriori.cic.api.utils.WorkflowTestUtil;
import com.apriori.cic.ui.pageobjects.home.CIConnectHome;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class PlmQueryDefIntRuleTests extends WorkflowTestUtil {

    private static final String QUERY_STRING_FIELD_VALUE = "%auto test \\- query%";
    private static SoftAssertions softAssertions;
    private CIConnectHome ciConnectHome;


    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {4137, 4150, 4340})
    @Description("Test each operator for the Int data type in isolation - Integer Equal, verify AND operator and data type")
    public void testWorkflowQueryDefIntegerEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.INTEGER1, QueryDefinitionFieldType.EQUAL, 1)
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber())))
            .isTrue();
    }

    @Test
    @TestRail(id = {24379})
    @Description("Test each operator for the Int data type in isolation - Integer NOT Equal")
    public void testWorkflowQueryDefIntegerNotEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.INTEGER1, QueryDefinitionFieldType.NOT_EQUAL, 1)
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
    @TestRail(id = {24384})
    @Description("Test each operator for the Int data type in isolation - Integer Greater Than")
    public void testWorkflowQueryDefIntegerGreaterThan() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.INTEGER1, QueryDefinitionFieldType.GREATER_THAN, 4)
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_NO_PARTS).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(id = {24389})
    @Description("Test each operator for the Int data type in isolation - Integer Greater Than or Equal")
    public void testWorkflowQueryDefIntegerGreaterThanEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.INTEGER1, QueryDefinitionFieldType.GREATER_THAN_EQUAL, 4)
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
    @TestRail(id = {24394})
    @Description("Test each operator for the Int data type in isolation - Integer between")
    public void testWorkflowQueryDefIntegerBetween() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.INTEGER1, QueryDefinitionFieldType.BETWEEN, 2,3)
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
    @TestRail(id = {24399})
    @Description("Test each operator for the Int data type in isolation - Integer Not between")
    public void testWorkflowQueryDefIntegerNotBetween() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.INTEGER1, QueryDefinitionFieldType.NOT_BETWEEN, 2,4)
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
    @TestRail(id = {24402})
    @Description("Test each operator for the Int data type in isolation - Integer less than")
    public void testWorkflowQueryDefIntegerLessThan() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.INTEGER1, QueryDefinitionFieldType.LESS_THAN, 2)
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(id = {24405})
    @Description("Test each operator for the Int data type in isolation - Integer less than or equal")
    public void testWorkflowQueryDefIntegerLessThanEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.INTEGER1, QueryDefinitionFieldType.LESS_THAN_EQUAL, 2)
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
