package com.cic.tests;

import com.apriori.pages.home.CIConnectHome;
import com.apriori.pages.login.CicLoginPage;
import com.apriori.utils.DateUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.reader.file.part.PartData;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import entity.request.JobDefinition;
import entity.request.WorkflowRequest;
import entity.response.AgentWorkflowJobResults;
import enums.CICPartSelectionType;
import enums.PlmPartDataType;
import enums.QueryDefinitionFieldType;
import enums.QueryDefinitionFields;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.CicApiTestUtil;
import utils.PlmPartsUtil;
import utils.WorkflowDataUtil;
import utils.WorkflowTestUtil;

public class PlmQueryDefStrEmailRuleTests extends TestBase {

    private static final String QUERY_STRING_FIELD_VALUE = "%auto test \\- query%";
    private static JobDefinition jobDefinitionData;
    private static WorkflowRequest workflowRequestDataBuilder;
    private static SoftAssertions softAssertions;
    private static PartData plmPartData;
    private static WorkflowTestUtil workflowTestUtil;
    private CIConnectHome ciConnectHome;


    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        jobDefinitionData = CicApiTestUtil.getJobDefinitionData();
        workflowTestUtil = new WorkflowTestUtil();
        ciConnectHome = new CicLoginPage(driver).login(UserUtil.getUser());
    }

    @Test
    @TestRail(testCaseId = {"4148", "4195"})
    @Description("Test each operator for the Int data type in isolation - String Equal and String1 attribute")
    public void testWorkflowQueryDefStringEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(ciConnectHome.getSession()))
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.EQUAL, "auto test \\- query 1")
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = workflowTestUtil.createWorkflowAndGetJobResult(workflowRequestDataBuilder, ciConnectHome.getSession());

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"24382"})
    @Description("Test each operator for the Int data type in isolation - String NotEqual")
    public void testWorkflowQueryDefStringNotEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(ciConnectHome.getSession()))
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.NOT_EQUAL, "auto test \\- query 1")
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = workflowTestUtil.createWorkflowAndGetJobResult(workflowRequestDataBuilder, ciConnectHome.getSession());

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
    @TestRail(testCaseId = {"24387"})
    @Description("Test each operator for the Int data type in isolation - String contains")
    public void testWorkflowQueryDefStringContains() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(ciConnectHome.getSession()))
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, "%1%")
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = workflowTestUtil.createWorkflowAndGetJobResult(workflowRequestDataBuilder, ciConnectHome.getSession());

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"24392"})
    @Description("Test each operator for the Int data type in isolation - String Begins with")
    public void testWorkflowQueryDefStringBeginsWith() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(ciConnectHome.getSession()))
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, "auto test \\- query 1%")
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = workflowTestUtil.createWorkflowAndGetJobResult(workflowRequestDataBuilder, ciConnectHome.getSession());

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"24397"})
    @Description("Test each operator for the Int data type in isolation - String Ends with")
    public void testWorkflowQueryDefStringEndsWith() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(ciConnectHome.getSession()))
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, "%1")
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = workflowTestUtil.createWorkflowAndGetJobResult(workflowRequestDataBuilder, ciConnectHome.getSession());

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"4412"})
    @Description("Test each operator for the Int data type in isolation - Email Equal")
    public void testWorkflowQueryDefEmailEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(ciConnectHome.getSession()))
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.EMAIL, QueryDefinitionFieldType.EQUAL, "email 1")
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = workflowTestUtil.createWorkflowAndGetJobResult(workflowRequestDataBuilder, ciConnectHome.getSession());

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"24383"})
    @Description("Test each operator for the Int data type in isolation - Email Not Equal")
    public void testWorkflowQueryDefEmailNotEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(ciConnectHome.getSession()))
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.EMAIL, QueryDefinitionFieldType.NOT_EQUAL, "email 1")
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = workflowTestUtil.createWorkflowAndGetJobResult(workflowRequestDataBuilder, ciConnectHome.getSession());

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
    @TestRail(testCaseId = {"24388"})
    @Description("Test each operator for the Int data type in isolation - Email Contains")
    public void testWorkflowQueryDefEmailContains() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(ciConnectHome.getSession()))
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.EMAIL, QueryDefinitionFieldType.CONTAINS, "%1%")
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = workflowTestUtil.createWorkflowAndGetJobResult(workflowRequestDataBuilder, ciConnectHome.getSession());

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"24393"})
    @Description("Test each operator for the Int data type in isolation - Email Begins With")
    public void testWorkflowQueryDefEmailBeginsWith() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(ciConnectHome.getSession()))
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.EMAIL, QueryDefinitionFieldType.CONTAINS, "email 1%")
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = workflowTestUtil.createWorkflowAndGetJobResult(workflowRequestDataBuilder, ciConnectHome.getSession());

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"24398"})
    @Description("Test each operator for the Int data type in isolation - Email Ends With")
    public void testWorkflowQueryDefEmailEndsWith() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(ciConnectHome.getSession()))
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.EMAIL, QueryDefinitionFieldType.CONTAINS, "%1")
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = workflowTestUtil.createWorkflowAndGetJobResult(workflowRequestDataBuilder, ciConnectHome.getSession());

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @After
    public void cleanup() {
        jobDefinitionData.setJobDefinition(CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName()).getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(ciConnectHome.getSession(), jobDefinitionData);
        softAssertions.assertAll();
    }
}
