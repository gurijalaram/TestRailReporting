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

public class PlmQueryDefDateRuleTests extends TestBase {

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
    @TestRail(testCaseId = {"24381"})
    @Description("Test each operator for the Int data type in isolation - Date Not Equal")
    public void testWorkflowQueryDefDateNotEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.DATE_TIME1, QueryDefinitionFieldType.NOT_EQUAL, DateUtil.getDateInMilliSeconds("04/21/2023 08:00:00", "GMT"))
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
    @TestRail(testCaseId = {"4149", "4873"})
    @Description("Test each operator for the Int data type in isolation - Date Equal")
    public void testWorkflowQueryDefDateEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.DATE_TIME1, QueryDefinitionFieldType.EQUAL, DateUtil.getDateInMilliSeconds("04/21/2023 08:00:00", "GMT"))
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = workflowTestUtil.createWorkflowAndGetJobResult(workflowRequestDataBuilder, ciConnectHome.getSession());

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"24386"})
    @Description("Test each operator for the Int data type in isolation - Date Greater Than")
    public void testWorkflowQueryDefDateGreaterThan() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.DATE_TIME1, QueryDefinitionFieldType.GREATER_THAN, DateUtil.getDateInMilliSeconds("04/24/2023 08:00:00", "GMT"))
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = workflowTestUtil.createWorkflowAndGetJobResult(workflowRequestDataBuilder, ciConnectHome.getSession());

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_NO_PARTS).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"24391"})
    @Description("Test each operator for the Int data type in isolation - Date Greater Than or Equal")
    public void testWorkflowQueryDefDateGreaterThanEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.DATE_TIME1, QueryDefinitionFieldType.GREATER_THAN_EQUAL, DateUtil.getDateInMilliSeconds("04/24/2023 08:00:00", "GMT"))
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = workflowTestUtil.createWorkflowAndGetJobResult(workflowRequestDataBuilder, ciConnectHome.getSession());

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(2);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_NO_PARTS).getPlmPartNumber()))).isTrue();
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_STRING).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"24396"})
    @Description("Test each operator for the Int data type in isolation - Date Between")
    public void testWorkflowQueryDefDateBetween() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.DATE_TIME1, QueryDefinitionFieldType.BETWEEN, DateUtil.getDateInMilliSeconds("04/22/2023 08:00:00", "GMT"), DateUtil.getDateInMilliSeconds("04/23/2023 08:00:00", "GMT"))
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = workflowTestUtil.createWorkflowAndGetJobResult(workflowRequestDataBuilder, ciConnectHome.getSession());

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(2);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_INTEGER).getPlmPartNumber()))).isTrue();
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_REAL).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"24401"})
    @Description("Test each operator for the Int data type in isolation - Date Not Between")
    public void testWorkflowQueryDefDateNotBetween() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.DATE_TIME1, QueryDefinitionFieldType.NOT_BETWEEN, DateUtil.getDateInMilliSeconds("04/22/2023 08:00:00", "GMT"), DateUtil.getDateInMilliSeconds("04/24/2023 08:00:00", "GMT"))
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = workflowTestUtil.createWorkflowAndGetJobResult(workflowRequestDataBuilder, ciConnectHome.getSession());

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(2);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_NO_PARTS).getPlmPartNumber()))).isTrue();
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"24404"})
    @Description("Test each operator for the Int data type in isolation - Date Less Than")
    public void testWorkflowQueryDefDateLessThan() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.DATE_TIME1, QueryDefinitionFieldType.LESS_THAN, DateUtil.getDateInMilliSeconds("04/22/2023 08:00:00", "GMT"))
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = workflowTestUtil.createWorkflowAndGetJobResult(workflowRequestDataBuilder, ciConnectHome.getSession());

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
    }

    @Test
    @TestRail(testCaseId = {"24407"})
    @Description("Test each operator for the Int data type in isolation - Date Less Than Equal")
    public void testWorkflowQueryDefDateLessThanEqual() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1, QueryDefinitionFieldType.CONTAINS, QUERY_STRING_FIELD_VALUE)
            .setQueryFilter(QueryDefinitionFields.DATE_TIME1, QueryDefinitionFieldType.LESS_THAN_EQUAL, DateUtil.getDateInMilliSeconds("04/22/2023 08:00:00", "GMT"))
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = workflowTestUtil.createWorkflowAndGetJobResult(workflowRequestDataBuilder, ciConnectHome.getSession());

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(2);
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_DATE).getPlmPartNumber()))).isTrue();
        softAssertions.assertThat(agentWorkflowJobResults.stream().anyMatch(r -> r.getPartNumber()
            .equals(new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_WITH_REAL).getPlmPartNumber()))).isTrue();
    }

    @After
    public void cleanup() {
        jobDefinitionData.setJobDefinition(CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName()).getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(ciConnectHome.getSession(), jobDefinitionData);
        softAssertions.assertAll();
    }
}
