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
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import entity.request.workflow.JobDefinition;
import entity.request.workflow.Workflow;
import entity.response.AgentConfiguration;
import entity.response.AgentStatus;
import entity.response.AgentWorkflow;
import entity.response.AgentWorkflowJob;
import entity.response.AgentWorkflowJobRun;
import enums.CICAPIEnum;
import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.CicApiTestUtil;

public class CicAgentTest extends TestBase {

    private static String loginSession;
    UserCredentials currentUser = UserUtil.getUser();
    private static Workflow workFlowData;
    private static JobDefinition jobDefinitionData;
    private static AgentWorkflow agentWorkflowResponse;
    private static ResponseWrapper<AgentWorkflowJobRun> agentWorkflowJobRunResponse;

    @BeforeClass
    public static void testSetup() {
        workFlowData = new TestDataService().getTestData("CicGuiCreateWorkFlowData.json", Workflow.class);
        workFlowData.setName(workFlowData.getName() + System.currentTimeMillis());
        workFlowData.setPlmSystem(PropertiesContext.get("${env}.ci-connect.plm_agent_id"));
        jobDefinitionData = new TestDataService().getTestData("CicGuiDeleteJobDefData.json", JobDefinition.class);
    }

    @Test
    @TestRail(testCaseId = {"5579"})
    @Description("Get CIC Agent Workflows")
    public void testAgentWorkflows() {
        loginSession = CicApiTestUtil.getLoginSession(currentUser, driver);
        ResponseWrapper<String> responseWrapper = CicApiTestUtil.CreateWorkflow(loginSession, workFlowData);
        assertThat(responseWrapper.getBody(), is(containsString("CreateJobDefinition")));
        assertThat(responseWrapper.getBody(), is(containsString(">true<")));
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workFlowData.getName());

        ResponseWrapper<String> response = CicApiTestUtil.submitRequest(CICAPIEnum.CIC_AGENT_WORKFLOWS, null);
        AgentWorkflow[] agentWorkflows = JsonManager.deserializeJsonFromString(response.getBody(), AgentWorkflow[].class);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(agentWorkflows.length, greaterThan((0)));
    }

    @Test
    @TestRail(testCaseId = {"5585"})
    @Description("Initiate the execution of Workflow")
    public void testCAgentWorkflowJobRun() {
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId());
        assertThat(agentWorkflowJobRunResponse.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertNotNull(agentWorkflowJobRunResponse.getResponseEntity().getJobId());
    }

    @Test
    @TestRail(testCaseId = {"5577"})
    @Description("Get CIC Agent Status")
    public void testDCAgentStatus() {
        ResponseWrapper<AgentStatus> response = CicApiTestUtil.submitRequest(CICAPIEnum.CIC_AGENT_STATUS, AgentStatus.class);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getCicConnectionStatus(), is(equalTo("Connected")));
    }

    @Test
    @TestRail(testCaseId = {"5578"})
    @Description("Get CIC Agent Configuration")
    public void testECAgentConfiguration() {
        ResponseWrapper<AgentConfiguration> response = CicApiTestUtil.submitRequest(CICAPIEnum.CIC_AGENT_CONFIG, AgentConfiguration.class);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getPlmType(), is(equalTo("WINDCHILL")));
    }

    @Test
    @TestRail(testCaseId = {"5581"})
    @Description("Get CIC Agent Workflow Jobs")
    public void testFCAgentWorkflowJobs() {
        ResponseWrapper<String> response = CicApiTestUtil.getCicAgentWorkflowJobs(agentWorkflowResponse.getId());
        AgentWorkflowJob[] agentWorkflowJobs = JsonManager.deserializeJsonFromString(response.getBody(), AgentWorkflowJob[].class);
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(agentWorkflowJobs.length, greaterThan((0)));
    }

    @Test
    @TestRail(testCaseId = {"5580"})
    @Description("Get CIC Agent Workflow with workflow id")
    public void testGCAgentWorkflow() {
        ResponseWrapper<AgentWorkflow> response = CicApiTestUtil.getCicAgentWorkflow(agentWorkflowResponse.getId());
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getName(), is(equalTo(workFlowData.getName())));
    }

    @Test
    @TestRail(testCaseId = {"5582"})
    @Description("Get Workflow using workflow id and job ID")
    public void testHCAgentWorkflowJob() {
        ResponseWrapper<AgentWorkflowJob> response = CicApiTestUtil.getCicAgentWorkflowJob(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getResponseEntity().getJobId());
        assertThat(response.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
        assertThat(response.getResponseEntity().getIdentity(), is(equalTo(agentWorkflowJobRunResponse.getResponseEntity().getJobId())));
    }

    @Test
    @TestRail(testCaseId = {"7618"})
    @Description("Cancel workflow using workflowId and jobId")
    public void testICCancelWorkflowJob() {
        ResponseWrapper<String> cancelResponse = CicApiTestUtil.cancelWorkflow(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getResponseEntity().getJobId());
        assertThat(cancelResponse.getStatusCode(), is(equalTo(HttpStatus.SC_ACCEPTED)));
    }

    @AfterClass
    public static void cleanup() {
        jobDefinitionData.setJobDefinition(CicApiTestUtil.getMatchedWorkflowId(workFlowData.getName()).getId() + "_Job");
        ResponseWrapper<String> responseWrapper = CicApiTestUtil.deleteWorkFlow(loginSession, jobDefinitionData);
        assertThat(responseWrapper.getStatusCode(), is(equalTo(HttpStatus.SC_OK)));
    }
}
