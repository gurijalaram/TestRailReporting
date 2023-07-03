package com.apriori.cic.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;
import com.apriori.utils.reader.file.part.PartData;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import entity.request.JobDefinition;
import entity.request.WorkflowRequest;
import entity.response.AgentConfiguration;
import entity.response.AgentStatus;
import entity.response.AgentWorkflow;
import entity.response.AgentWorkflowJob;
import enums.CICAPIEnum;
import enums.CICPartSelectionType;
import enums.PlmPartDataType;
import io.qameta.allure.Description;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.CicApiTestUtil;
import utils.CicLoginUtil;
import utils.PlmPartsUtil;
import utils.WorkflowDataUtil;
import utils.WorkflowTestUtil;

public class CicAgentTest extends TestBase {

    private static String loginSession;
    private static AgentWorkflow agentWorkflowResponse;
    private static JobDefinition jobDefinitionData;
    private static WorkflowRequest workflowRequestDataBuilder;
    private static PartData plmPartData;
    private static WorkflowTestUtil workflowTestUtil;
    private static String workflowName = StringUtils.EMPTY;
    private static String workflowData;
    private static SoftAssertions softAssertions;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        plmPartData = new PlmPartsUtil().getPlmPartData();
        workflowTestUtil = new WorkflowTestUtil();
        jobDefinitionData = CicApiTestUtil.getJobDefinitionData();
        loginSession = new CicLoginUtil(getDriver()).login(UserUtil.getUser())
            .navigateToUserMenu()
            .getWebSession();
    }

    @Test
    @TestRail(testCaseId = {"5579"})
    @Description("Get CIC Agent Workflows")
    public void testAgentWorkflows() {
        ResponseWrapper<String> response = CicApiTestUtil.submitRequest(CICAPIEnum.CIC_AGENT_WORKFLOWS, null);
        AgentWorkflow[] agentWorkflows = JsonManager.deserializeJsonFromString(response.getBody(), AgentWorkflow[].class);
        softAssertions.assertThat(agentWorkflows.length).isGreaterThan(0);
    }

    @Test
    @TestRail(testCaseId = {"5577"})
    @Description("Get CIC Agent Status")
    public void testDCAgentStatus() {
        ResponseWrapper<AgentStatus> response = CicApiTestUtil.submitRequest(CICAPIEnum.CIC_AGENT_STATUS, AgentStatus.class);
        softAssertions.assertThat(response.getResponseEntity().getCicConnectionStatus()).isEqualTo("Connected");
    }

    @Test
    @TestRail(testCaseId = {"5578"})
    @Description("Get CIC Agent Configuration")
    public void testECAgentConfiguration() {
        ResponseWrapper<AgentConfiguration> response = CicApiTestUtil.submitRequest(CICAPIEnum.CIC_AGENT_CONFIG, AgentConfiguration.class);
        softAssertions.assertThat(response.getResponseEntity().getPlmType()).isEqualTo("WINDCHILL");
    }

    @Test
    @TestRail(testCaseId = {"5581", "5585", "7618", "5580"})
    @Description("Get CIC Agent Workflow Jobs, " +
        "Initiate the execution of Workflow, Get Workflow using workflow id and job ID" +
        "Cancel workflow using workflowId and jobId" +
        "Get CIC Agent Workflow with workflow id")
    public void testFCAgentWorkflowJobs() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .build();

        workflowTestUtil = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName())
            .invoke();
        softAssertions.assertThat(workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId()).isNotNull();
        ResponseWrapper<String> response = CicApiTestUtil.getCicAgentWorkflowJobs(workflowTestUtil.getAgentWorkflowResponse().getId());
        AgentWorkflowJob[] agentWorkflowJobs = JsonManager.deserializeJsonFromString(response.getBody(), AgentWorkflowJob[].class);
        softAssertions.assertThat(agentWorkflowJobs.length).isGreaterThan(0);
        AgentWorkflowJob agentWorkflowJobResponse = CicApiTestUtil.getCicAgentWorkflowJobStatus(workflowTestUtil.getAgentWorkflowResponse().getId(), workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId());
        softAssertions.assertThat(agentWorkflowJobResponse.getIdentity()).isEqualTo(agentWorkflowJobResponse.getIdentity());
        AgentWorkflow agentWorkflow = CicApiTestUtil.getCicAgentWorkflow(workflowTestUtil.getAgentWorkflowResponse().getId());
        softAssertions.assertThat(agentWorkflow.getName()).isEqualTo(workflowRequestDataBuilder.getName());
        CicApiTestUtil.cancelWorkflow(workflowTestUtil.getAgentWorkflowResponse().getId(), workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId());
        CicApiTestUtil.deleteWorkFlow(loginSession, workflowTestUtil.getAgentWorkflowResponse());
    }

    @After
    public void cleanup() {
        softAssertions.assertAll();
    }
}
