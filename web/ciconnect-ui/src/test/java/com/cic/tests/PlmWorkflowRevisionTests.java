package com.cic.tests;

import com.apriori.enums.CheckboxState;
import com.apriori.pages.home.CIConnectHome;
import com.apriori.pages.login.CicLoginPage;
import com.apriori.pages.workflows.WorkflowHome;
import com.apriori.utils.GenerateStringUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.part.PartData;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import entity.request.JobDefinition;
import entity.request.WorkflowRequest;
import entity.response.AgentWorkflow;
import entity.response.AgentWorkflowJobPartsResult;
import entity.response.AgentWorkflowJobResults;
import entity.response.AgentWorkflowJobRun;
import enums.CICPartSelectionType;
import enums.CostingInputFields;
import enums.MappingRule;
import enums.PlmPartDataType;
import enums.QueryDefinitionFields;
import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.CicApiTestUtil;
import utils.PlmPartsUtil;
import utils.WorkflowDataUtil;

public class PlmWorkflowRevisionTests extends TestBase {

    private static AgentWorkflow agentWorkflowResponse;
    private static JobDefinition jobDefinitionData;
    private static WorkflowRequest workflowRequestDataBuilder;
    private static ResponseWrapper<String> createWorkflowResponse;
    private static AgentWorkflowJobRun agentWorkflowJobRunResponse;
    private static SoftAssertions softAssertions;
    private static PartData plmPartData;
    private CIConnectHome ciConnectHome;


    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        jobDefinitionData = CicApiTestUtil.getJobDefinitionData();
        ciConnectHome = new CicLoginPage(driver).login(UserUtil.getUser());
    }

    @Test
    @TestRail(testCaseId = {"5972", "5978"})
    @Description("Test 'Return Latest Revision' functions correctly for NEW workflow when ENABLED," +
        "Test 'Return Latest Revision' for job that contains only parts with multiple revision")
    public void testWorkflowRevisionWhenReturnOnlyEnabled() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_PARTS);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(ciConnectHome.getSession()))
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .useLatestRevision(true)
            .build();

        createWorkflowResponse = CicApiTestUtil.createWorkflow(workflowRequestDataBuilder, ciConnectHome.getSession());
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");

        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobResults agentWorkflowJobResults = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        AgentWorkflowJobPartsResult jobPartsRevisionResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResults, "B");

        softAssertions.assertThat(jobPartsRevisionResult.getPartType()).isEqualTo("PART");
        softAssertions.assertThat(jobPartsRevisionResult.getInput()).isNotNull();
        softAssertions.assertThat(jobPartsRevisionResult.getResult()).isNotNull();
    }

    @Test
    @TestRail(testCaseId = {"5973"})
    @Description("Test 'Return Latest Revision' functions correctly for NEW workflow when DISABLED")
    public void testWorkflowMapSetRevisionDisabled() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_PARTS);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(ciConnectHome.getSession()))
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .useLatestRevision(false)
            .build();

        createWorkflowResponse = CicApiTestUtil.createWorkflow(workflowRequestDataBuilder, ciConnectHome.getSession());
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");

        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobResults agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(2);
        AgentWorkflowJobPartsResult jobPartsRevisionAResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResult, "A");
        AgentWorkflowJobPartsResult jobPartsRevisionBResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResult, "B");

        softAssertions.assertThat(jobPartsRevisionAResult.getPartType()).isEqualTo("PART");
        softAssertions.assertThat(jobPartsRevisionAResult.getInput()).isNotNull();
        softAssertions.assertThat(jobPartsRevisionBResult.getErrorMessage()).contains("Skipped - This part appeared multiple times in the job");
    }

    @Test
    @TestRail(testCaseId = {"5976"})
    @Description("Test 'Return Latest Revision' enabled with a query for a particular revision")
    public void testWorkflowMapSetSpecificRevision() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(ciConnectHome.getSession()))
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilter(QueryDefinitionFields.REVISION_NUMBER.getQueryDefinitionField(), "EQ", "B")
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .useLatestRevision(true)
            .build();

        createWorkflowResponse = CicApiTestUtil.createWorkflow(workflowRequestDataBuilder, ciConnectHome.getSession());
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");

        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobResults agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(1);
        AgentWorkflowJobPartsResult jobPartsRevisionResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResult, "B");

        softAssertions.assertThat(jobPartsRevisionResult.getPartType()).isEqualTo("PART");
        softAssertions.assertThat(jobPartsRevisionResult.getInput()).isNotNull();
    }

    @Test
    @TestRail(testCaseId = {"5977"})
    @Description("Test 'Return Latest Revision' for job that contains no parts with multiple revision")
    public void testWorkflowNoPartWithMultiRev() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_NO_PARTS);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(ciConnectHome.getSession()))
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .useLatestRevision(true)
            .build();

        createWorkflowResponse = CicApiTestUtil.createWorkflow(workflowRequestDataBuilder, ciConnectHome.getSession());
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");

        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobResults agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(1);
        AgentWorkflowJobPartsResult jobPartsRevisionResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResult, "A");

        softAssertions.assertThat(jobPartsRevisionResult.getPartType()).isEqualTo("PART");
        softAssertions.assertThat(jobPartsRevisionResult.getInput()).isNotNull();
    }

    @Test
    @TestRail(testCaseId = {"5979"})
    @Description("Test 'Return Latest Revision' for job that contains a mix of parts with multiple and single revision")
    public void testWorkflowMixedPartsWithMultipleAndSingleRev() {
        PartData plmPartWithParts = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_PARTS);
        PartData plmPartWithNoParts = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_NO_PARTS);

        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(ciConnectHome.getSession()))
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartWithParts.getPlmPartNumber())
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartWithNoParts.getPlmPartNumber())
            .setQueryFilters("OR")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .useLatestRevision(true)
            .build();

        createWorkflowResponse = CicApiTestUtil.createWorkflow(workflowRequestDataBuilder, ciConnectHome.getSession());
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");

        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobResults agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(2);
        AgentWorkflowJobPartsResult jobPartsRevisionAResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResult, "A");
        AgentWorkflowJobPartsResult jobPartsRevisionBResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResult, "B");

        softAssertions.assertThat(jobPartsRevisionAResult.getPartType()).isEqualTo("PART");
        softAssertions.assertThat(jobPartsRevisionAResult.getInput()).isNotNull();
        softAssertions.assertThat(jobPartsRevisionAResult.getPartNumber()).isEqualTo("0000003933");

        softAssertions.assertThat(jobPartsRevisionBResult.getPartType()).isEqualTo("PART");
        softAssertions.assertThat(jobPartsRevisionBResult.getInput()).isNotNull();
        softAssertions.assertThat(jobPartsRevisionBResult.getPartNumber()).isEqualTo("0000003932");

    }

    @Test
    @TestRail(testCaseId = {"5981"})
    @Description("Verify no errors are displayed when 'Return Latest Revision' is enabled and job contains no parts")
    public void testWorkflowVerifyRevisionWithInValidPart() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(ciConnectHome.getSession()))
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", "invalid")
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .useLatestRevision(true)
            .build();

        createWorkflowResponse = CicApiTestUtil.createWorkflow(workflowRequestDataBuilder, ciConnectHome.getSession());
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");

        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobResults agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(0);
    }

    @Test
    @TestRail(testCaseId = {"5974"})
    @Description("Test editing workflow to enable 'Return Latest Revision'")
    public void testWorkflowEditDisabledRevision() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_PARTS);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(ciConnectHome.getSession()))
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .useLatestRevision(false)
            .build();
        workflowRequestDataBuilder.setName(GenerateStringUtil.saltString("----0WFCR"));

        createWorkflowResponse = CicApiTestUtil.createWorkflow(workflowRequestDataBuilder, ciConnectHome.getSession());
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");

        WorkflowHome workflowHome = ciConnectHome.clickWorkflowMenu()
            .selectScheduleTab()
            .selectWorkflow(workflowRequestDataBuilder.getName())
            .clickEditWorkflowBtn()
            .clickNextBtnInDetailsTab()
            .selectReturnLatestRevisionOnlyCheckbox(CheckboxState.on)
            .clickWFQueryDefNextBtn()
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition updated!");
        workflowHome.closeMessageAlertBox();

        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobResults agentWorkflowJobResults = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        AgentWorkflowJobPartsResult jobPartsRevisionResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResults, "B");

        softAssertions.assertThat(jobPartsRevisionResult.getPartType()).isEqualTo("PART");
        softAssertions.assertThat(jobPartsRevisionResult.getInput()).isNotNull();
        softAssertions.assertThat(jobPartsRevisionResult.getResult()).isNotNull();
    }

    @Test
    @TestRail(testCaseId = {"5975"})
    @Description("Test editing workflow to disable 'Return Latest Revision'")
    public void testWorkflowEditEnabledRevision() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_PARTS);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(ciConnectHome.getSession()))
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .useLatestRevision(true)
            .build();
        workflowRequestDataBuilder.setName(GenerateStringUtil.saltString("----0WFCR"));

        createWorkflowResponse = CicApiTestUtil.createWorkflow(workflowRequestDataBuilder, ciConnectHome.getSession());
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");

        WorkflowHome workflowHome = ciConnectHome.clickWorkflowMenu()
            .selectScheduleTab()
            .selectWorkflow(workflowRequestDataBuilder.getName())
            .clickEditWorkflowBtn()
            .clickNextBtnInDetailsTab()
            .selectReturnLatestRevisionOnlyCheckbox(CheckboxState.off)
            .clickWFQueryDefNextBtn()
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition updated!");
        workflowHome.closeMessageAlertBox();

        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobResults agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(2);
        AgentWorkflowJobPartsResult jobPartsRevisionAResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResult, "A");
        AgentWorkflowJobPartsResult jobPartsRevisionBResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResult, "B");

        softAssertions.assertThat(jobPartsRevisionAResult.getPartType()).isEqualTo("PART");
        softAssertions.assertThat(jobPartsRevisionAResult.getInput()).isNotNull();
        softAssertions.assertThat(jobPartsRevisionBResult.getErrorMessage()).contains("Skipped - This part appeared multiple times in the job");
    }

    @Test
    @TestRail(testCaseId = {"5984"})
    @Description("Verify 'Return Latest Revision' setting is preserved when a workflow query is edited")
    public void testWorkflowEditEnabledPreserveRevision() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_PARTS);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(ciConnectHome.getSession()))
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .useLatestRevision(true)
            .build();
        workflowRequestDataBuilder.setName(GenerateStringUtil.saltString("----0WFCR"));

        createWorkflowResponse = CicApiTestUtil.createWorkflow(workflowRequestDataBuilder, ciConnectHome.getSession());
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(createWorkflowResponse.getBody()).contains(">true<");

        WorkflowHome workflowHome = ciConnectHome.clickWorkflowMenu()
            .selectScheduleTab()
            .selectWorkflow(workflowRequestDataBuilder.getName())
            .clickEditWorkflowBtn()
            .clickNextBtnInDetailsTab()
            .clickWFQueryDefNextBtn()
            .clickCINextBtn()
            .clickCINotificationNextBtn()
            .clickSaveButton();

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition updated!");
        workflowHome.closeMessageAlertBox();

        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName());
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        AgentWorkflowJobResults agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(agentWorkflowResponse.getId(),
            agentWorkflowJobRunResponse.getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(1);
        AgentWorkflowJobPartsResult jobPartsRevisionResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResult, "B");

        softAssertions.assertThat(jobPartsRevisionResult.getPartType()).isEqualTo("PART");
        softAssertions.assertThat(jobPartsRevisionResult.getInput()).isNotNull();
        softAssertions.assertThat(jobPartsRevisionResult.getResult()).isNotNull();
    }

    @After
    public void cleanup() {
        jobDefinitionData.setJobDefinition(CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName()).getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(ciConnectHome.getSession(), jobDefinitionData);
        softAssertions.assertAll();
    }
}
