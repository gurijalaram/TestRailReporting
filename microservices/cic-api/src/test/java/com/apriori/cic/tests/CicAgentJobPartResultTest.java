package com.apriori.cic.tests;

import com.apriori.utils.DateFormattingUtils;
import com.apriori.utils.DateUtil;
import com.apriori.utils.TestRail;
import com.apriori.utils.enums.DigitalFactoryEnum;
import com.apriori.utils.enums.MaterialNameEnum;
import com.apriori.utils.enums.ProcessGroupEnum;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.reader.file.part.PartData;
import com.apriori.utils.reader.file.user.UserUtil;
import com.apriori.utils.web.driver.TestBase;

import entity.request.JobDefinition;
import entity.request.WorkflowParts;
import entity.request.WorkflowRequest;
import entity.response.AgentErrorMessage;
import entity.response.AgentWorkflow;
import entity.response.AgentWorkflowJob;
import entity.response.AgentWorkflowJobPartsResult;
import entity.response.AgentWorkflowJobResults;
import entity.response.AgentWorkflowJobRun;
import enums.CICAPIEnum;
import enums.CICAgentStatus;
import enums.CICPartSelectionType;
import enums.CostingInputFields;
import enums.MappingRule;
import enums.PlmPartDataType;
import enums.QueryDefinitionFieldType;
import enums.QueryDefinitionFields;
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

public class CicAgentJobPartResultTest extends TestBase {

    private static String loginSession;
    private static JobDefinition jobDefinitionData;
    private static WorkflowRequest workflowRequestDataBuilder;
    private static WorkflowParts workflowPartsRequestDataBuilder;
    private static PartData plmPartData;
    private static WorkflowTestUtil workflowTestUtil;
    private static SoftAssertions softAssertions;

    @Before
    public void testSetup() {
        softAssertions = new SoftAssertions();
        loginSession = new CicLoginUtil(driver).login(UserUtil.getUser())
            .navigateToUserMenu()
            .getWebSession();
        workflowTestUtil = new WorkflowTestUtil();
        plmPartData = new PlmPartsUtil().getPlmPartData();
        jobDefinitionData = CicApiTestUtil.getJobDefinitionData();
    }

    @Test
    @TestRail(testCaseId = {"16318", "16324", "16325"})
    @Description("Get request with valid workflow, job, part identity, verify response body and data")
    public void testGetWorkflowJobPartResult() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = workflowTestUtil.createWorkflowAndGetJobPartResult(workflowRequestDataBuilder, workflowPartsRequestDataBuilder, loginSession);

        softAssertions.assertThat(agentWorkflowJobPartsResult.getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getMaterialName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProcessGroupName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getVpeName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getAnnualVolume()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getBatchSize()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
    }

    @Test
    @TestRail(testCaseId = {"16335"})
    @Description("Get Part Results for parts re-costed with different costing inputs")
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

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = workflowTestUtil.getJobPartResult(workflowPartsRequestDataBuilder.getParts().get(0).getId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getMaterialName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getProcessGroupName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getVpeName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getAnnualVolume()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getBatchSize()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());

        workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setProcessGroupName("Sheet Metal");
        workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setMaterialName("Steel, Cold Worked, AISI 1020");
        workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setVpeName("aPriori Brazil");
        workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setAnnualVolume(5000);
        workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setBatchSize(50);

        AgentWorkflowJobPartsResult reCostedJobPartsResult = workflowTestUtil.invokeRestWorkflow(workflowPartsRequestDataBuilder)
            .track()
            .getJobPartResult(workflowPartsRequestDataBuilder.getParts().get(0).getId());

        softAssertions.assertThat(reCostedJobPartsResult.getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
        softAssertions.assertThat(reCostedJobPartsResult.getInput().getMaterialName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getMaterialName());
        softAssertions.assertThat(reCostedJobPartsResult.getInput().getProcessGroupName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getProcessGroupName());
        softAssertions.assertThat(reCostedJobPartsResult.getInput().getVpeName()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getVpeName());
        softAssertions.assertThat(reCostedJobPartsResult.getInput().getAnnualVolume()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getAnnualVolume());
        softAssertions.assertThat(reCostedJobPartsResult.getInput().getBatchSize()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().getBatchSize());
    }

    @Test
    @TestRail(testCaseId = {"16330"})
    @Description("Get part results with errored Part identity")
    public void testGetPartResultWithErroredPart() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        workflowPartsRequestDataBuilder.getParts().get(0).getCostingInputs().setProcessGroupName("Plastic");
        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = workflowTestUtil.createWorkflowAndGetJobPartResult(workflowRequestDataBuilder, workflowPartsRequestDataBuilder, loginSession);
        softAssertions.assertThat(agentWorkflowJobPartsResult.getErrorMessage()).contains("Unable to find process group with name 'Plastic'");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getCicStatus()).isEqualTo("ERRORED");
    }

    @Test
    @TestRail(testCaseId = {"16332", "16338"})
    @Description("get Part Results for part selection type REST and windchill agent")
    public void testGetPartResultWithRestForWC() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = workflowTestUtil.createWorkflowAndGetJobPartResult(workflowRequestDataBuilder, workflowPartsRequestDataBuilder, loginSession);

        softAssertions.assertThat(agentWorkflowJobPartsResult.getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
    }

    @Test
    @TestRail(testCaseId = {"16328"})
    @Description("Get part results with fields that have no value")
    public void testGetPartResultWithNoFields() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .setWorkflowDescription("")
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = workflowTestUtil.createWorkflowAndGetJobPartResult(workflowRequestDataBuilder, workflowPartsRequestDataBuilder, loginSession);

        softAssertions.assertThat(agentWorkflowJobPartsResult.getPartId()).isEqualTo(workflowPartsRequestDataBuilder.getParts().get(0).getId());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getDescription()).isNull();
    }

    @Test
    @TestRail(testCaseId = {"16321"})
    @Description("Get Part results with invalid workflow Identity")
    public void testGetPartResultWithInvalidWorkFlow() {
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

        AgentErrorMessage aJobPartsResultError = CicApiTestUtil.getCicAgentWorkflowJobPartsResult("INVALID",
            workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(),
            workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(aJobPartsResultError.getMessage()).contains(String.format("Job '%s' in workflow 'INVALID' not found", workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId()));
    }

    @Test
    @TestRail(testCaseId = {"16322"})
    @Description("Get Part results with valid workflow Identity and invalid job identity")
    public void testGetPartResultWithInvalidJobIdentity() {
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
        AgentErrorMessage agentPartErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(workflowTestUtil.getAgentWorkflowResponse().getId(),
            "INVALID",
            workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(agentPartErrorMessage.getMessage()).contains(String.format("Job 'INVALID' in workflow '%s' not found", workflowTestUtil.getAgentWorkflowResponse().getId()));
    }

    @Test
    @TestRail(testCaseId = {"16341"})
    @Description("Get Part results with valid workflow, job identity and invalid part identity")
    public void testGetPartResultWithInvalidPartIdentity() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .emptyCostingInputRow()
            .build();
        workflowPartsRequestDataBuilder = CicApiTestUtil.getWorkflowPartDataBuilder(plmPartData, 1);
        workflowTestUtil =  workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName())
            .invokeRestWorkflow(workflowPartsRequestDataBuilder)
            .track();

        AgentErrorMessage agentPartErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(workflowTestUtil.getAgentWorkflowResponse().getId(),
            workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(),
            "INVALID",
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(agentPartErrorMessage.getMessage()).contains(String.format("Part 'INVALID' for job '%s' in workflow '%s' not found", workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(), workflowTestUtil.getAgentWorkflowResponse().getId()));
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

        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(workflowTestUtil.getAgentWorkflowResponse().getId(), workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(), CICAgentStatus.QUERY_IN_PROGRESS)).isTrue();

        AgentErrorMessage agentPartErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(workflowTestUtil.getAgentWorkflowResponse().getId(),
            workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(),
            workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentErrorMessage.class,
            HttpStatus.SC_CONFLICT);

        softAssertions.assertThat(agentPartErrorMessage.getMessage()).contains(String.format("The job '%s' is not in a terminal state", workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId()));

        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(workflowTestUtil.getAgentWorkflowResponse().getId(), workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(), CICAgentStatus.COSTING)).isTrue();

        AgentErrorMessage agentCostingErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(workflowTestUtil.getAgentWorkflowResponse().getId(),
            workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(),
            workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentErrorMessage.class,
            HttpStatus.SC_CONFLICT);

        softAssertions.assertThat(agentCostingErrorMessage.getMessage()).contains(String.format("The job '%s' is not in a terminal state", workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId()));

        softAssertions.assertThat(CicApiTestUtil.waitUntilExpectedJobStatusMatched(workflowTestUtil.getAgentWorkflowResponse().getId(), workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(), CICAgentStatus.PLM_WRITE_ACTION)).isTrue();

        AgentErrorMessage agentPlmWriteErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(workflowTestUtil.getAgentWorkflowResponse().getId(),
            workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(),
            workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentErrorMessage.class,
            HttpStatus.SC_CONFLICT);

        softAssertions.assertThat(agentPlmWriteErrorMessage.getMessage()).contains(String.format("The job '%s' is not in a terminal state", workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId()));
    }

    @Test
    @TestRail(testCaseId = {"16320"})
    @Description("get Part Results for part selection type REST and windchill agent")
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

        AgentErrorMessage agentErrorMessage = (AgentErrorMessage) HTTPRequest.build(RequestEntityUtil.init(CICAPIEnum.CIC_AGENT_WORKFLOW_JOB_PART_RESULT, AgentErrorMessage.class)
            .inlineVariables(workflowTestUtil.getAgentWorkflowResponse().getId(), workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(), workflowPartsRequestDataBuilder.getParts().get(0).getId())
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
    @TestRail(testCaseId = {"16334"})
    @Description("Get part results with deleted workflow Identity")
    public void testGetPartResultWithDeletedWorkFlow() {
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

        AgentErrorMessage agentErrorMessage = CicApiTestUtil.getCicAgentWorkflowJobPartsResult(workflowTestUtil.getAgentWorkflowResponse().getId(),
            workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(),
            workflowPartsRequestDataBuilder.getParts().get(0).getId(),
            AgentErrorMessage.class,
            HttpStatus.SC_NOT_FOUND);

        softAssertions.assertThat(agentErrorMessage.getMessage()).contains(String.format("Job '%s' in workflow '%s' not found", workflowTestUtil.getAgentWorkflowJobRunResponse().getJobId(), workflowTestUtil.getAgentWorkflowResponse().getId()));
    }

    @Test
    @TestRail(testCaseId = {"16331"})
    @Description("Get Part results with workflow created using part selection type query")
    public void testGetWorkflowJobResultPartSelectionQuery() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setCustomer(CicApiTestUtil.getCustomerName())
            .setAgent(CicApiTestUtil.getAgent(loginSession))
            .setQueryFilter("partNumber", "EQ", new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MAPPED).getPlmPartNumber())
            .setQueryFilters("AND")
            .build();

        AgentWorkflowJobResults agentWorkflowJobResult  = workflowTestUtil.create(workflowRequestDataBuilder, loginSession)
            .getWorkflowId(workflowRequestDataBuilder.getName())
            .invoke()
            .track()
            .getJobResult();

        softAssertions.assertThat(agentWorkflowJobResult.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResult.get(0).getPartNumber()).isEqualTo(workflowRequestDataBuilder.getQuery().getFilters().getFilters().get(0).getValue());

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = workflowTestUtil.getJobPartResult(agentWorkflowJobResult.get(0).getPartId());

        softAssertions.assertThat(agentWorkflowJobPartsResult.getPartNumber()).isEqualTo(workflowRequestDataBuilder.getQuery().getFilters().getFilters().get(0).getValue());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getDescription()).isNull();
    }

    @After
    public void cleanup() {
        CicApiTestUtil.deleteWorkFlow(loginSession, CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName()));
        softAssertions.assertAll();
    }
}