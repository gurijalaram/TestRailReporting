package com.apriori.cic.api.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.API_SANITY;

import com.apriori.cic.api.enums.CICAPIEnum;
import com.apriori.cic.api.enums.CICPartSelectionType;
import com.apriori.cic.api.models.request.JobDefinition;
import com.apriori.cic.api.models.response.AgentConfiguration;
import com.apriori.cic.api.models.response.AgentStatus;
import com.apriori.cic.api.models.response.AgentWorkflow;
import com.apriori.cic.api.models.response.AgentWorkflowJob;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.api.utils.WorkflowDataUtil;
import com.apriori.cic.api.utils.WorkflowTestUtil;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.json.JsonManager;
import com.apriori.shared.util.rules.TestRulesAPI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestRulesAPI.class)
public class CicAgentTest extends WorkflowTestUtil {

    private JobDefinition jobDefinitionData;
    private SoftAssertions softAssertions;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        plmPartData = new PlmPartsUtil().getPlmPartData();
        jobDefinitionData = CicApiTestUtil.getJobDefinitionData();
        currentUser = UserUtil.getUser();
    }

    @Test
    @Tag(API_SANITY)
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

    @AfterEach
    public void cleanup() {
        softAssertions.assertAll();
        this.close();
    }
}
