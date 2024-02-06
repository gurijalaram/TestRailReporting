package com.apriori.cic.api.tests;

import com.apriori.cic.api.enums.CICPartSelectionType;
import com.apriori.cic.api.enums.PlmPartDataType;
import com.apriori.cic.api.models.request.CostingInputs;
import com.apriori.cic.api.models.request.WorkflowPart;
import com.apriori.cic.api.models.request.WorkflowParts;
import com.apriori.cic.api.models.response.AgentWorkflowJobPartsResult;
import com.apriori.cic.api.models.response.AgentWorkflowJobResults;
import com.apriori.cic.api.models.response.PlmSearchPart;
import com.apriori.cic.api.utils.PlmApiTestUtil;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.api.utils.WorkflowDataUtil;
import com.apriori.cic.api.utils.WorkflowTestUtil;
import com.apriori.shared.util.enums.ProcessGroupEnum;
import com.apriori.shared.util.file.part.PartData;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class CicAgentSustainMetricsTest extends WorkflowTestUtil {

    private SoftAssertions softAssertions;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        plmPartData = new PlmPartsUtil().getPlmPartData();
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(id = {28435, 28396})
    @Issue("APG-1285")
    @Description("sustainability metrics NOT returned for unsupported Process Groups and verify in job and part results")
    public void testWorkflowNotSupportedSustainMetricsResults() {
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .emptyCostingInputRow()
            .isNotificationsIncluded(false, false, "")
            .isPublishResultsAttachReportInclude(false, "")
            .isPublishResultsWriteFieldsInclude(false)
            .build();
        PartData plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL);
        PlmSearchPart plmSearchPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id(plmSearchPart.getId())
                .costingInputs(CostingInputs.builder()
                    .processGroupName(ProcessGroupEnum.FORGING.getProcessGroup())
                    .scenarioName(new GenerateStringUtil().generateScenarioName())
                    .machiningMode("NOT_MACHINED")
                    .build())
                .build()))
            .build();

        this.cicLogin()
            .create()
            .getWorkflowId();

        softAssertions.assertThat(this.agentWorkflowResponse.getId()).isNotNull();

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.invokeRestWorkflow().track().getJobPartResult(plmSearchPart.getId());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getResult().getMaterialCarbon()).isNull();
        softAssertions.assertThat(agentWorkflowJobPartsResult.getResult().getProcessCarbon()).isNull();
        softAssertions.assertThat(agentWorkflowJobPartsResult.getResult().getLogisticsCarbon()).isNull();
        softAssertions.assertThat(agentWorkflowJobPartsResult.getResult().getTotalCarbon()).isNull();
        softAssertions.assertThat(agentWorkflowJobPartsResult.getResult().getAnnualManufacturingCarbon()).isNull();

        AgentWorkflowJobResults agentWorkflowJobResults = this.getJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getMaterialCarbon()).isNull();
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getProcessCarbon()).isNull();
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getLogisticsCarbon()).isNull();
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getTotalCarbon()).isNull();
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getAnnualManufacturingCarbon()).isNull();
    }


    @Test
    @TestRail(id = {28415, 28436, 28438, 28433})
    @Description("Components that do not have optional inputs targetCost, targetMass, machiningMode set " +
        "machiningMode field is not returned when set for a component costed with PG that does not support it" +
        "and verify in job and part results")
    public void testWorkflowOptionalInputNotSupportedResults() {
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .emptyCostingInputRow()
            .isNotificationsIncluded(false, false, "")
            .isPublishResultsAttachReportInclude(false, "")
            .isPublishResultsWriteFieldsInclude(false)
            .build();
        PartData plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL);
        PlmSearchPart plmSearchPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id(plmSearchPart.getId())
                .costingInputs(CostingInputs.builder()
                    .processGroupName(ProcessGroupEnum.STOCK_MACHINING.getProcessGroup())
                    .scenarioName(new GenerateStringUtil().generateScenarioName())
                    .build())
                .build()))
            .build();

        this.cicLogin()
            .create()
            .getWorkflowId();

        softAssertions.assertThat(this.agentWorkflowResponse.getId()).isNotNull();

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.invokeRestWorkflow().track().getJobPartResult(plmSearchPart.getId());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getTargetCost()).isNull();
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getTargetMass()).isNull();
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getMachiningMode()).isNull();

        AgentWorkflowJobResults agentWorkflowJobResults = this.getJobResult();
        softAssertions.assertThat(agentWorkflowJobResults.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getInput().getTargetCost()).isNull();
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getInput().getTargetMass()).isNull();
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getInput().getMachiningMode()).isNull();
    }

    @Test
    @TestRail(id = {28432, 28437, 28434})
    @Description("Default value returned for machining mode for component costed with PG that supports the input but does not have value set explicitly" +
        " Fields with value of zero are not omitted from response" +
        "verify in job and part results")
    public void testWorkflowSupportedProcessGroupResults() {
        this.workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.REST)
            .emptyCostingInputRow()
            .isNotificationsIncluded(false, false, "")
            .isPublishResultsAttachReportInclude(false, "")
            .isPublishResultsWriteFieldsInclude(false)
            .build();
        PartData plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL);
        PlmSearchPart plmSearchPart = new PlmApiTestUtil().getPlmPartByPartNumber(plmPartData.getPlmPartNumber());
        this.workflowPartsRequestDataBuilder = WorkflowParts.builder()
            .parts(Collections.singletonList(WorkflowPart.builder()
                .id(plmSearchPart.getId())
                .costingInputs(CostingInputs.builder()
                    .processGroupName(ProcessGroupEnum.CASTING_DIE.getProcessGroup())
                    .scenarioName(new GenerateStringUtil().generateScenarioName())
                    .build())
                .build()))
            .build();

        this.cicLogin()
            .create()
            .getWorkflowId();

        softAssertions.assertThat(this.agentWorkflowResponse.getId()).isNotNull();

        AgentWorkflowJobPartsResult agentWorkflowJobPartsResult = this.invokeRestWorkflow().track().getJobPartResult(plmSearchPart.getId());
        softAssertions.assertThat(agentWorkflowJobPartsResult.getInput().getMachiningMode()).isEqualTo("MAY_BE_MACHINED");
        softAssertions.assertThat(agentWorkflowJobPartsResult.getResult().getLogisticsCarbon()).isEqualTo(0.0);

        AgentWorkflowJobResults agentWorkflowJobResults = this.getJobResult();
        softAssertions.assertThat(agentWorkflowJobResults.size()).isGreaterThan(0);
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getInput().getMachiningMode()).isEqualTo("MAY_BE_MACHINED");
        softAssertions.assertThat(agentWorkflowJobResults.get(0).getResult().getLogisticsCarbon()).isEqualTo(0.0);
    }

    @AfterEach
    public void tearSetup() {
        this.deleteWorkflow();
        this.close();
        softAssertions.assertAll();
    }
}
