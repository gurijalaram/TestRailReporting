package com.apriori.cic.tests;

import com.apriori.utils.TestRail;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.reader.file.part.PartData;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import entity.request.JobDefinition;
import entity.request.WorkflowParts;
import entity.request.WorkflowRequest;
import entity.response.AgentErrorMessage;
import entity.response.AgentWorkflowJobResults;
import enums.CICAPIEnum;
import enums.CICAgentStatus;
import enums.CICPartSelectionType;
import enums.PlmPartDataType;
import io.qameta.allure.Description;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.CicApiTestUtil;
import utils.CicLoginUtil;
import utils.PlmPartsUtil;
import utils.WorkflowDataUtil;
import utils.WorkflowTestUtil;

import java.util.HashMap;

public class CicAgentJobResultsTest extends TestBase {

    private static String loginSession;
    private static JobDefinition jobDefinitionData;
    private static PartData plmPartData;
    private static WorkflowTestUtil workflowTestUtil;
    private static SoftAssertions softAssertions;
    private WorkflowRequest workflowRequestDataBuilder;
    private WorkflowParts workflowPartsRequestDataBuilder;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        loginSession = new CicLoginUtil(driver).login(UserUtil.getUser())
            .navigateToUserMenu()
            .getWebSession();
        plmPartData = new PlmPartsUtil().getPlmPartData();
        workflowTestUtil = new WorkflowTestUtil();
        jobDefinitionData = CicApiTestUtil.getJobDefinitionData();
    }

    @Test
    @TestRail(testCaseId = {"16282", "16288", "16289"})
    @Description("Get Job results with valid workflow and job identity and validate response body data")
    public void testGetWorkflowJobResult() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);

        AgentWorkflowJobResults agentWorkflowJobResult = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName())
            .invokeRestWorkflow(workflowPartsRequestDataBuilder)
            .track()
            .getJobResult();

        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getMaterialName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getProcessGroupName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getVpeName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getAnnualVolume()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getBatchSize()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
    }

    @Test
    @TestRail(testCaseId = {"16305"})
    @Description("Get Job results with fields that have no value")
    public void testGetWorkflowJobResultWithNoFields() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .setWorkflowDescription("")
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        AgentWorkflowJobResults agentWorkflowJobResult = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName())
            .invokeRestWorkflow(workflowPartsRequestDataBuilder)
            .track()
            .getJobResult();

        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getDescription()).isNull();
    }

    @Test
    @TestRail(testCaseId = {"16308"})
    @Description("Get Job results with workflow created using part selection type query")
    public void testGetWorkflowJobResultPartSelectionQuery() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .setQueryFilter("partNumber", "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResult = workflowTestUtil.createWorkflowAndGetJobResult(workflowRequestDataBuilder, loginSession);
        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getPartNumber()).isEqualTo(workflowRequestDataBuilder.getQuery().getFilters().getFilters().get(0).getValue());
    }

    @Test
    @TestRail(testCaseId = {"16291", "16309", "16315"})
    @Description("get Job Results maximum parts job size for part selection type REST and windchill agent")
    public void testGetWorkflowJobResultWithMaxLimitParts() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .setWorkflowDescription("")
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 5);

        AgentWorkflowJobResults agentWorkflowJobResult = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName())
            .invokeRestWorkflow(workflowPartsRequestDataBuilder)
            .track()
            .getJobResult();
        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(5);
    }

    @Test
    @TestRail(testCaseId = {"16285"})
    @Description("Get Job results with invalid workflow Identity")
    public void testGetJobResultWithInvalidWorkFlow() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .setWorkflowDescription("")
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);

        workflowTestUtil = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName())
            .invokeRestWorkflow(workflowPartsRequestDataBuilder)
            .track();

        AgentErrorMessage agentErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobResult("INVALID",
            workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(),
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(agentErrorMessage.getMessage()).contains(String.format("Job '%s' in workflow 'INVALID' not found", workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId()));
    }

    @Test
    @TestRail(testCaseId = {"16286"})
    @Description("Get Job results with valid workflow Identity and invalid job identity")
    public void testGetJobResultWithInvalidJobIdentity() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        workflowTestUtil = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName())
            .invokeRestWorkflow(workflowPartsRequestDataBuilder)
            .track();

        AgentErrorMessage agentErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobResult(workflowTestUtil.getAgentWorkflowResponse().getId(),
            "INVALID",
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(agentErrorMessage.getMessage()).contains(String.format("Job 'INVALID' in workflow '%s' not found", workflowTestUtil.getAgentWorkflowResponse().getId()));
    }

    @Test
    @TestRail(testCaseId = {"16306"})
    @Description("Get Job results with cancelled job identity")
    public void testGetWorkflowJobResultWithCancelledJob() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        workflowTestUtil = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName())
            .invokeRestWorkflow(workflowPartsRequestDataBuilder)
            .track();

        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(workflowTestUtil.getAgentWorkflowResponse().getId(), workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(), CICAgentStatus.COSTING)).isTrue();
        CicApiTestUtil.cancelWorkflow(workflowTestUtil.getAgentWorkflowResponse().getId(), workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId());
        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(workflowTestUtil.getAgentWorkflowResponse().getId(), workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(), CICAgentStatus.CANCELLED)).isTrue();

        AgentWorkflowJobResults agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(workflowTestUtil.getAgentWorkflowResponse().getId(),
            workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(),
            AgentWorkflowJobResults.class,
            HttpStatus.SC_OK);

        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getMaterialName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getProcessGroupName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getVpeName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getAnnualVolume()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getBatchSize()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
    }

    @Test
    @TestRail(testCaseId = {"16310"})
    @Description("Get Job results with errored job identity")
    public void testGetWorkflowJobResultWithErroredJob() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setProcessGroupName("Plastic");
        AgentWorkflowJobResults agentWorkflowJobResult = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName())
            .invokeRestWorkflow(workflowPartsRequestDataBuilder)
            .track()
            .getJobResult();

        softAssertions.assertThat(agentWorkflowJobResult.get(0).getCicStatus()).isEqualTo("ERRORED");
    }

    @Test
    @TestRail(testCaseId = {"16287"})
    @Description("Get Job results during processing with different status")
    public void testGetWorkflowJobInProgress() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        workflowTestUtil = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName())
            .invokeRestWorkflow(workflowPartsRequestDataBuilder);

        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(workflowTestUtil.getAgentWorkflowResponse().getId(),
            workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(),
            CICAgentStatus.QUERY_IN_PROGRESS)).isTrue();

        AgentErrorMessage agentWorkflowJobResult = CicApiTestUtil.getCicAgentWorkflowJobResult(workflowTestUtil.getAgentWorkflowResponse().getId(),
            workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(),
            AgentErrorMessage.class,
            HttpStatus.SC_CONFLICT);

        softAssertions.assertThat(agentWorkflowJobResult.getMessage()).contains(String.format("The job '%s' is not in a terminal state", workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId()));

        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(workflowTestUtil.getAgentWorkflowResponse().getId(), workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(), CICAgentStatus.COSTING)).isTrue();

        AgentErrorMessage agentJobCostingResult = CicApiTestUtil.getCicAgentWorkflowJobResult(workflowTestUtil.getAgentWorkflowResponse().getId(), workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(),
            AgentErrorMessage.class,
            HttpStatus.SC_CONFLICT);

        softAssertions.assertThat(agentJobCostingResult.getMessage()).contains(String.format("The job '%s' is not in a terminal state", workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId()));

        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(workflowTestUtil.getAgentWorkflowResponse().getId(), workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(), CICAgentStatus.PLM_WRITE_ACTION)).isTrue();

        AgentErrorMessage agentJobPlmWriteResult = CicApiTestUtil.getCicAgentWorkflowJobResult(workflowTestUtil.getAgentWorkflowResponse().getId(), workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(),
            AgentErrorMessage.class,
            HttpStatus.SC_CONFLICT);

        softAssertions.assertThat(agentJobPlmWriteResult.getMessage()).contains(String.format("The job '%s' is not in a terminal state", workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId()));
    }

    @Test
    @TestRail(testCaseId = {"16284"})
    @Description("get Job Results for part selection type REST and windchill agent")
    public void testGetPartResultInvalidAuthenticationKey() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        workflowTestUtil = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName())
            .invokeRestWorkflow(workflowPartsRequestDataBuilder);

        AgentErrorMessage agentErrorMessage = (AgentErrorMessage) HTTPRequest.build(RequestEntityUtil.init(CICAPIEnum.CIC_AGENT_WORKFLOW_JOB_RESULT, AgentErrorMessage.class)
            .inlineVariables(workflowTestUtil.getAgentWorkflowResponse().getId(), workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId())
            .expectedResponseCode(HttpStatus.SC_FORBIDDEN)
            .headers(new HashMap<String, String>() {
                {
                    put("Accept", "application/json");
                    put("Content-Type", "application/json");
                    put("Authorization", "InvalidKey");
                }
            })).get().getResponseEntity();

        softAssertions.assertThat(agentErrorMessage.getMessage()).isEqualTo("Access Denied");
    }

    @Test
    @TestRail(testCaseId = {"16312"})
    @Description("Get Job Results for parts re-costed with different costing inputs")
    public void testGetPartResultWithReCostedParts() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();

        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        workflowTestUtil = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName())
            .invokeRestWorkflow(workflowPartsRequestDataBuilder)
            .track();

        AgentWorkflowJobResults agentWorkflowJobResult = workflowTestUtil.getJobResult();

        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getMaterialName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getProcessGroupName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getVpeName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getAnnualVolume()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getInput().getBatchSize()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());

        workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setProcessGroupName("Sheet Metal");
        workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setMaterialName("Steel, Cold Worked, AISI 1020");
        workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setVpeName("aPriori Brazil");
        workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setAnnualVolume(5000);
        workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setBatchSize(50);

        AgentWorkflowJobResults agentReCostedJobResult = workflowTestUtil.invokeRestWorkflow(workflowPartsRequestDataBuilder)
            .track()
            .getJobResult();

        softAssertions.assertThat(agentReCostedJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentReCostedJobResult.get(0).getInput().getMaterialName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentReCostedJobResult.get(0).getInput().getProcessGroupName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(agentReCostedJobResult.get(0).getInput().getVpeName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(agentReCostedJobResult.get(0).getInput().getAnnualVolume()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(agentReCostedJobResult.get(0).getInput().getBatchSize()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
    }

    @Test
    @TestRail(testCaseId = {"16311"})
    @Description("Get Job results with deleted workflow Identity")
    public void testGetJobResultWithDeletedWorkFlow() {
        JobDefinition jDData = CicApiTestUtil.getJobDefinitionData();
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        workflowTestUtil = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName())
            .invokeRestWorkflow(workflowPartsRequestDataBuilder);

        jDData.setJobDefinition(workflowTestUtil.getAgentWorkflowResponse().getId() + "_Job");
        CicApiTestUtil.deleteWorkFlow(loginSession, jDData);

        AgentErrorMessage agentErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobResult(workflowTestUtil.getAgentWorkflowResponse().getId(),
            workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(),
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(agentErrorMessage.getMessage()).contains(String.format("Job '%s' in workflow '%s' not found", workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(), workflowTestUtil.getAgentWorkflowResponse().getId()));
    }

    @After
    public void cleanup() {
        CicApiTestUtil.deleteWorkFlow(loginSession, CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName()));
        softAssertions.assertAll();
    }
}