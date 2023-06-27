package com.apriori.cic.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertNotNull;

import com.apriori.utils.TestRail;
import com.apriori.utils.dataservice.TestDataService;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.json.utils.JsonManager;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import entity.request.JobDefinition;
import entity.response.AgentConfiguration;
import entity.response.AgentStatus;
import entity.response.AgentWorkflow;
import entity.response.AgentWorkflowJob;
import entity.response.AgentWorkflowJobRun;
import enums.CICAPIEnum;
import io.qameta.allure.Description;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.CicApiTestUtil;
import utils.CicLoginUtil;

public class CicAgentTest extends TestBase {

    private static String loginSession;
    private static AgentWorkflow agentWorkflowResponse;
    private static JobDefinition jobDefinitionData;
    private static AgentWorkflowJobRun agentWorkflowJobRunResponse;
    private static String workflowName = StringUtils.EMPTY;
    private static String workflowData;

    @Before
    public void testSetup() {
        workflowName = "CIC_AGENT" + System.currentTimeMillis();
        String scenarioName = "SN" + System.currentTimeMillis();
        loginSession = new CicLoginUtil(driver).login(UserUtil.getUser())
            .navigateToUserMenu()
            .getWebSession();
        workflowData = String.format(CicApiTestUtil.getWorkflowData("CicGuiCreateQueryWorkFlowData.json"), CicApiTestUtil.getCustomerName(),CicApiTestUtil.getAgent(loginSession),workflowName, scenarioName);
        CicApiTestUtil.createWorkflow(loginSession, workflowData);
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowName);
        jobDefinitionData = new TestDataService().getTestData("CicGuiDeleteJobDefData.json", JobDefinition.class);
    }

    @Test
    @TestRail(testCaseId = {"5579"})
    @Description("Get CIC Agent Workflows")
    public void testAgentWorkflows() {
        ResponseWrapper<String> response = CicApiTestUtil.submitRequest(CICAPIEnum.CIC_AGENT_WORKFLOWS, null);
        AgentWorkflow[] agentWorkflows = JsonManager.deserializeJsonFromString(response.getBody(), AgentWorkflow[].class);
        assertThat(agentWorkflows.length, greaterThan((0)));
    }

    @Test
    @TestRail(testCaseId = {"5577"})
    @Description("Get CIC Agent Status")
    public void testDCAgentStatus() {
        ResponseWrapper<AgentStatus> response = CicApiTestUtil.submitRequest(CICAPIEnum.CIC_AGENT_STATUS, AgentStatus.class);
        assertThat(response.getResponseEntity().getCicConnectionStatus(), is(equalTo("Connected")));
    }

    @Test
    @TestRail(testCaseId = {"5578"})
    @Description("Get CIC Agent Configuration")
    public void testECAgentConfiguration() {
        ResponseWrapper<AgentConfiguration> response = CicApiTestUtil.submitRequest(CICAPIEnum.CIC_AGENT_CONFIG, AgentConfiguration.class);
        assertThat(response.getResponseEntity().getPlmType(), is(equalTo("WINDCHILL")));
    }

    @Test
    @TestRail(testCaseId = {"5581", "5585", "7618"})
    @Description("Get CIC Agent Workflow Jobs, " +
        "Initiate the execution of Workflow, Get Workflow using workflow id and job ID" +
        "Cancel workflow using workflowId and jobId")
    public void testFCAgentWorkflowJobs() {
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        assertNotNull(agentWorkflowJobRunResponse.getJobId());
        ResponseWrapper<String> response = CicApiTestUtil.getCicAgentWorkflowJobs(agentWorkflowResponse.getId());
        AgentWorkflowJob[] agentWorkflowJobs = JsonManager.deserializeJsonFromString(response.getBody(), AgentWorkflowJob[].class);
        assertThat(agentWorkflowJobs.length, greaterThan((0)));
        AgentWorkflowJob agentWorkflowJobResponse = CicApiTestUtil.getCicAgentWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId());
        assertThat(agentWorkflowJobResponse.getIdentity(), is(equalTo(agentWorkflowJobResponse.getIdentity())));
        CicApiTestUtil.cancelWorkflow(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId());
    }

    @Test
    @TestRail(testCaseId = {"5580"})
    @Description("Get CIC Agent Workflow with workflow id")
    public void testGCAgentWorkflow() {
        AgentWorkflow agentWorkflow = CicApiTestUtil.getCicAgentWorkflow(agentWorkflowResponse.getId());
        assertThat(agentWorkflow.getName(), is(equalTo(workflowName)));
    }

    @After
    public void cleanup() {
        jobDefinitionData.setJobDefinition(CicApiTestUtil.getMatchedWorkflowId(workflowName).getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(loginSession, jobDefinitionData);
    }
}
