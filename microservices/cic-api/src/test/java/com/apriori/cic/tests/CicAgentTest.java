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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.CicApiTestUtil;

public class CicAgentTest extends TestBase {

    private static String loginSession;
    UserCredentials currentUser = UserUtil.getUser();
    private static AgentWorkflow agentWorkflowResponse;
    private static JobDefinition jobDefinitionData;
    private static AgentWorkflowJobRun agentWorkflowJobRunResponse;
    private static String workflowName = StringUtils.EMPTY;
    private static String workflowData;

    @BeforeClass
    public static void testSetup() {
        workflowName = "CIC_AGENT" + System.currentTimeMillis();
        String scenarioName = "SN" + System.currentTimeMillis();
        workflowData = String.format(CicApiTestUtil.getWorkflowData("CicGuiCreateQueryWorkFlowData.json"), CicApiTestUtil.getCustomerName(),CicApiTestUtil.getAgent(),workflowName, scenarioName);
        jobDefinitionData = new TestDataService().getTestData("CicGuiDeleteJobDefData.json", JobDefinition.class);
    }

    @Test
    @TestRail(testCaseId = {"5579"})
    @Description("Get CIC Agent Workflows")
    public void testAgentWorkflows() {
        loginSession = CicApiTestUtil.getLoginSession(currentUser, driver);
        ResponseWrapper<String> responseWrapper = CicApiTestUtil.CreateWorkflow(loginSession, workflowData);
        assertThat(responseWrapper.getBody(), is(containsString("CreateJobDefinition")));
        assertThat(responseWrapper.getBody(), is(containsString(">true<")));
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowName);

        ResponseWrapper<String> response = CicApiTestUtil.submitRequest(CICAPIEnum.CIC_AGENT_WORKFLOWS, null);
        AgentWorkflow[] agentWorkflows = JsonManager.deserializeJsonFromString(response.getBody(), AgentWorkflow[].class);
        assertThat(agentWorkflows.length, greaterThan((0)));
    }

    @Test
    @TestRail(testCaseId = {"5585"})
    @Description("Initiate the execution of Workflow")
    public void testCAgentWorkflowJobRun() {
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId());
        assertNotNull(agentWorkflowJobRunResponse.getJobId());
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
    @TestRail(testCaseId = {"5581"})
    @Description("Get CIC Agent Workflow Jobs")
    public void testFCAgentWorkflowJobs() {
        ResponseWrapper<String> response = CicApiTestUtil.getCicAgentWorkflowJobs(agentWorkflowResponse.getId());
        AgentWorkflowJob[] agentWorkflowJobs = JsonManager.deserializeJsonFromString(response.getBody(), AgentWorkflowJob[].class);
        assertThat(agentWorkflowJobs.length, greaterThan((0)));
    }

    @Test
    @TestRail(testCaseId = {"5580"})
    @Description("Get CIC Agent Workflow with workflow id")
    public void testGCAgentWorkflow() {
        AgentWorkflow agentWorkflow = CicApiTestUtil.getCicAgentWorkflow(agentWorkflowResponse.getId());
        assertThat(agentWorkflow.getName(), is(equalTo(workflowName)));
    }

    @Test
    @TestRail(testCaseId = {"5582"})
    @Description("Get Workflow using workflow id and job ID")
    public void testHCAgentWorkflowJob() {
        AgentWorkflowJob agentWorkflowJobResponse = CicApiTestUtil.getCicAgentWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId());
        assertThat(agentWorkflowJobResponse.getIdentity(), is(equalTo(agentWorkflowJobResponse.getIdentity())));
    }

    @Test
    @TestRail(testCaseId = {"7618"})
    @Description("Cancel workflow using workflowId and jobId")
    public void testICCancelWorkflowJob() {
        CicApiTestUtil.cancelWorkflow(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId());
    }

    @AfterClass
    public static void cleanup() {
        jobDefinitionData.setJobDefinition(CicApiTestUtil.getMatchedWorkflowId(workflowName).getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(loginSession, jobDefinitionData);
    }
}
