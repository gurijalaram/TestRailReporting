package com.apriori.cic.tests;

import com.apriori.http.utils.ResponseWrapper;
import com.apriori.json.JsonManager;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import entity.request.JobDefinition;
import entity.response.AgentConfiguration;
import entity.response.AgentStatus;
import entity.response.AgentWorkflow;
import entity.response.AgentWorkflowJob;
import enums.CICAPIEnum;
import enums.CICPartSelectionType;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.CicApiTestUtil;
import utils.PlmPartsUtil;
import utils.WorkflowDataUtil;
import utils.WorkflowTestUtil;

public class CicAgentTest extends WorkflowTestUtil {

    private JobDefinition jobDefinitionData;
    private SoftAssertions softAssertions;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        plmPartData = new PlmPartsUtil().getPlmPartData();
        jobDefinitionData = CicApiTestUtil.getJobDefinitionData();
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(id = {5579})
    @Description("Get CIC Agent Workflows")
    public void testAgentWorkflows() {
        ResponseWrapper<String> response = CicApiTestUtil.submitRequest(CICAPIEnum.CIC_AGENT_WORKFLOWS, null);
        AgentWorkflow[] agentWorkflows = JsonManager.deserializeJsonFromString(response.getBody(), AgentWorkflow[].class);
        softAssertions.assertThat(agentWorkflows.length).isGreaterThan(0);
    }

    @Test
    @TestRail(id = {5577})
    @Description("Get CIC Agent Status")
    public void testDCAgentStatus() {
        ResponseWrapper<AgentStatus> response = CicApiTestUtil.submitRequest(CICAPIEnum.CIC_AGENT_STATUS, AgentStatus.class);
        softAssertions.assertThat(response.getResponseEntity().getCicConnectionStatus()).isEqualTo("Connected");
    }

    @Test
    @TestRail(id = {5578})
    @Description("Get CIC Agent Configuration")
    public void testECAgentConfiguration() {
        ResponseWrapper<AgentConfiguration> response = CicApiTestUtil.submitRequest(CICAPIEnum.CIC_AGENT_CONFIG, AgentConfiguration.class);
        softAssertions.assertThat(response.getResponseEntity().getPlmType()).isEqualTo("WINDCHILL");
    }

    @Test
    @TestRail(id = {5581, 5585, 7618, 5580})
    @Description("Get CIC Agent Workflow Jobs, " +
        "Initiate the execution of Workflow, Get Workflow using workflow id and job ID" +
        "Cancel workflow using workflowId and jobId" +
        "Get CIC Agent Workflow with workflow id")
    public void testFCAgentWorkflowJobs() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .build();

        this.cicLogin()
            .create()
            .getWorkflowId()
            .invokeQueryWorkflow();
        softAssertions.assertThat(this.agentWorkflowJobRunResponse.getJobId()).isNotNull();
        ResponseWrapper<String> response = CicApiTestUtil.getCicAgentWorkflowJobs(this.agentWorkflowResponse.getId());
        AgentWorkflowJob[] agentWorkflowJobs = JsonManager.deserializeJsonFromString(response.getBody(), AgentWorkflowJob[].class);
        softAssertions.assertThat(agentWorkflowJobs.length).isGreaterThan(0);
        AgentWorkflowJob agentWorkflowJobResponse = CicApiTestUtil.getCicAgentWorkflowJobStatus(this.agentWorkflowResponse.getId(), this.agentWorkflowJobRunResponse.getJobId());
        softAssertions.assertThat(agentWorkflowJobResponse.getIdentity()).isEqualTo(agentWorkflowJobResponse.getIdentity());
        AgentWorkflow agentWorkflow = CicApiTestUtil.getCicAgentWorkflow(this.agentWorkflowResponse.getId());
        softAssertions.assertThat(agentWorkflow.getName()).isEqualTo(this.workflowRequestDataBuilder.getName());
        CicApiTestUtil.cancelWorkflow(this.agentWorkflowResponse.getId(), this.agentWorkflowJobRunResponse.getJobId());
        this.deleteWorkflow();
    }

    @After
    public void cleanup() {
        softAssertions.assertAll();
        this.close();
    }
}
