package com.apriori.cic.ui.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cic.api.enums.CICPartSelectionType;
import com.apriori.cic.api.enums.PlmPartDataType;
import com.apriori.cic.api.enums.QueryDefinitionFieldType;
import com.apriori.cic.api.enums.QueryDefinitionFields;
import com.apriori.cic.api.models.response.AgentWorkflowJobResults;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.api.utils.WorkflowDataUtil;
import com.apriori.cic.api.utils.WorkflowTestUtil;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class PlmQueryDefStrEmailRuleTests extends WorkflowTestUtil {

    private static final String QUERY_STRING_FIELD_VALUE = "%auto test \\- query%";
    private static SoftAssertions softAssertions;


    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {4148, 4195})
    @Description("Test each operator for the Int data type in isolation - String Equal and String1 attribute")
    public void testWorkflowQueryDefStringEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.EQUAL, "auto test \\- query 1")
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(id = {24382})
    @Description("Test each operator for the Int data type in isolation - String NotEqual")
    public void testWorkflowQueryDefStringNotEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.NOT_EQUAL, "auto test \\- query 1")
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
    @TestRail(id = {24387})
    @Description("Test each operator for the Int data type in isolation - String contains")
    public void testWorkflowQueryDefStringContains() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, "%1%")
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(id = {24392})
    @Description("Test each operator for the Int data type in isolation - String Begins with")
    public void testWorkflowQueryDefStringBeginsWith() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, "auto test \\- query 1%")
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(id = {24397})
    @Description("Test each operator for the Int data type in isolation - String Ends with")
    public void testWorkflowQueryDefStringEndsWith() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, "%1")
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(id = {4412})
    @Description("Test each operator for the Int data type in isolation - Email Equal")
    public void testWorkflowQueryDefEmailEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.EMAIL, QueryDefinitionFieldType.EQUAL, "email 1")
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(id = {24383})
    @Description("Test each operator for the Int data type in isolation - Email Not Equal")
    public void testWorkflowQueryDefEmailNotEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.EMAIL, QueryDefinitionFieldType.NOT_EQUAL, "email 1")
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
    @TestRail(id = {24388})
    @Description("Test each operator for the Int data type in isolation - Email Contains")
    public void testWorkflowQueryDefEmailContains() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.EMAIL, QueryDefinitionFieldType.CONTAINS, "%1%")
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(id = {24393})
    @Description("Test each operator for the Int data type in isolation - Email Begins With")
    public void testWorkflowQueryDefEmailBeginsWith() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.EMAIL, QueryDefinitionFieldType.CONTAINS, "email 1%")
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(id = {24398})
    @Description("Test each operator for the Int data type in isolation - Email Ends With")
    public void testWorkflowQueryDefEmailEndsWith() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.EMAIL, QueryDefinitionFieldType.CONTAINS, "%1")
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @AfterEach
    public void cleanup() {
        this.deleteWorkflow();
        softAssertions.assertAll();
    }
}
