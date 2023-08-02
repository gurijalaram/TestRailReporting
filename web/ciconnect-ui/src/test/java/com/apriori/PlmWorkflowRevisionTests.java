package com.apriori;

import com.apriori.cic.enums.CICPartSelectionType;
import com.apriori.cic.enums.CostingInputFields;
import com.apriori.cic.enums.MappingRule;
import com.apriori.cic.enums.PlmPartDataType;
import com.apriori.cic.enums.QueryDefinitionFields;
import com.apriori.cic.models.response.AgentWorkflowJobPartsResult;
import com.apriori.cic.models.response.AgentWorkflowJobResults;
import com.apriori.cic.utils.CicApiTestUtil;
import com.apriori.cic.utils.PlmPartsUtil;
import com.apriori.cic.utils.WorkflowDataUtil;
import com.apriori.enums.CheckboxState;
import com.apriori.enums.ProcessGroupEnum;
import com.apriori.pageobjects.home.CIConnectHome;
import com.apriori.pageobjects.login.CicLoginPage;
import com.apriori.pageobjects.workflows.WorkflowHome;
import com.apriori.reader.file.part.PartData;
import com.apriori.reader.file.user.UserUtil;
import com.apriori.testrail.TestRail;

import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.CicGuiTestUtil;

public class PlmWorkflowRevisionTests extends CicGuiTestUtil {

    private static SoftAssertions softAssertions;
    private static PartData plmPartData;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
    }

    @Test
    @TestRail(id = {5972, 5978})
    @Description("Test 'Return Latest Revision' functions correctly for NEW workflow when ENABLED," +
        "Test 'Return Latest Revision' for job that contains only parts with multiple revision")
    public void testWorkflowRevisionWhenReturnOnlyEnabled() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_PARTS);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .useLatestRevision(true)
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        AgentWorkflowJobPartsResult jobPartsRevisionResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResults, "B");

        softAssertions.assertThat(jobPartsRevisionResult.getPartType()).isEqualTo("PART");
        softAssertions.assertThat(jobPartsRevisionResult.getInput()).isNotNull();
        softAssertions.assertThat(jobPartsRevisionResult.getResult()).isNotNull();
    }

    @Test
    @TestRail(id = {5973})
    @Description("Test 'Return Latest Revision' functions correctly for NEW workflow when DISABLED")
    public void testWorkflowMapSetRevisionDisabled() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_PARTS);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .useLatestRevision(false)
            .build();

        AgentWorkflowJobResults agentWorkflowJobResult = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(2);
        AgentWorkflowJobPartsResult jobPartsRevisionAResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResult, "A");
        AgentWorkflowJobPartsResult jobPartsRevisionBResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResult, "B");

        softAssertions.assertThat(jobPartsRevisionAResult.getPartType()).isEqualTo("PART");
        softAssertions.assertThat(jobPartsRevisionAResult.getInput()).isNotNull();
        softAssertions.assertThat(jobPartsRevisionBResult.getErrorMessage()).contains("Skipped - This part appeared multiple times in the job");
    }

    @Test
    @TestRail(id = {5976})
    @Description("Test 'Return Latest Revision' enabled with a query for a particular revision")
    public void testWorkflowMapSetSpecificRevision() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilter(QueryDefinitionFields.REVISION_NUMBER.getQueryDefinitionField(), "EQ", "B")
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .useLatestRevision(true)
            .build();

        AgentWorkflowJobResults agentWorkflowJobResult = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(1);
        AgentWorkflowJobPartsResult jobPartsRevisionResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResult, "B");

        softAssertions.assertThat(jobPartsRevisionResult.getPartType()).isEqualTo("PART");
        softAssertions.assertThat(jobPartsRevisionResult.getInput()).isNotNull();
    }

    @Test
    @TestRail(id = {5977})
    @Description("Test 'Return Latest Revision' for job that contains no parts with multiple revision")
    public void testWorkflowNoPartWithMultiRev() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_NO_PARTS);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .useLatestRevision(true)
            .build();

        AgentWorkflowJobResults agentWorkflowJobResult = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(1);
        AgentWorkflowJobPartsResult jobPartsRevisionResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResult, "A");

        softAssertions.assertThat(jobPartsRevisionResult.getPartType()).isEqualTo("PART");
        softAssertions.assertThat(jobPartsRevisionResult.getInput()).isNotNull();
    }

    @Test
    @TestRail(id = {5979})
    @Description("Test 'Return Latest Revision' for job that contains a mix of parts with multiple and single revision")
    public void testWorkflowMixedPartsWithMultipleAndSingleRev() {
        PartData plmPartWithParts = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_PARTS);
        PartData plmPartWithNoParts = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_NO_PARTS);

        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartWithParts.getPlmPartNumber())
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartWithNoParts.getPlmPartNumber())
            .setQueryFilters("OR")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .useLatestRevision(true)
            .build();

        AgentWorkflowJobResults agentWorkflowJobResult = this.createQueryWorkflowAndGetJobResult();

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
    @TestRail(id = {5981})
    @Description("Verify no errors are displayed when 'Return Latest Revision' is enabled and job contains no parts")
    public void testWorkflowVerifyRevisionWithInValidPart() {
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", "invalid")
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .useLatestRevision(true)
            .build();

        AgentWorkflowJobResults agentWorkflowJobResult = this.createQueryWorkflowAndGetJobResult();

        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(0);
    }

    @Test
    @TestRail(id = {5974})
    @Description("Test editing workflow to enable 'Return Latest Revision'")
    public void testWorkflowEditDisabledRevision() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_PARTS);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setWorkflowName(GenerateStringUtil.saltString("----0WFCR"))
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .useLatestRevision(false)
            .build();

        this.cicGuiLogin()
            .createWorkflow()
            .getWorkflow();

        WorkflowHome workflowHome = this.ciConnectHome.clickWorkflowMenu()
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

        AgentWorkflowJobResults agentWorkflowJobResults = this.getWorkflow()
            .invokeWorkflow()
            .trackWorkflow()
            .getJobResult();

        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(1);
        AgentWorkflowJobPartsResult jobPartsRevisionResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResults, "B");

        softAssertions.assertThat(jobPartsRevisionResult.getPartType()).isEqualTo("PART");
        softAssertions.assertThat(jobPartsRevisionResult.getInput()).isNotNull();
        softAssertions.assertThat(jobPartsRevisionResult.getResult()).isNotNull();
    }

    @Test
    @TestRail(id = {5975})
    @Description("Test editing workflow to disable 'Return Latest Revision'")
    public void testWorkflowEditEnabledRevision() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_PARTS);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setAgent(ciConnectHome.getSession())
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .useLatestRevision(true)
            .build();

        this.cicGuiLogin()
            .createWorkflow()
            .getWorkflow();

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

        AgentWorkflowJobResults agentWorkflowJobResult = this.getWorkflow()
            .invokeWorkflow()
            .trackWorkflow()
            .getJobResult();

        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(2);
        AgentWorkflowJobPartsResult jobPartsRevisionAResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResult, "A");
        AgentWorkflowJobPartsResult jobPartsRevisionBResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResult, "B");

        softAssertions.assertThat(jobPartsRevisionAResult.getPartType()).isEqualTo("PART");
        softAssertions.assertThat(jobPartsRevisionAResult.getInput()).isNotNull();
        softAssertions.assertThat(jobPartsRevisionBResult.getErrorMessage()).contains("Skipped - This part appeared multiple times in the job");
    }

    @Test
    @TestRail(id = {5984})
    @Description("Verify 'Return Latest Revision' setting is preserved when a workflow query is edited")
    public void testWorkflowEditEnabledPreserveRevision() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_MULTI_REVISION_PARTS);
        CIConnectHome ciConnectHome = new CicLoginPage(driver).login(currentUser);
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setWorkflowName(GenerateStringUtil.saltString("----0WFCR"))
            .setQueryFilter(QueryDefinitionFields.PART_NUMBER.getQueryDefinitionField(), "EQ", plmPartData.getPlmPartNumber())
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.CONSTANT, ProcessGroupEnum.SHEET_METAL.getProcessGroup())
            .useLatestRevision(true)
            .build();

        this.cicGuiLogin()
            .createWorkflow()
            .getWorkflow();

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

        AgentWorkflowJobResults agentWorkflowJobResult = this.getWorkflow()
            .invokeWorkflow()
            .trackWorkflow()
            .getJobResult();

        softAssertions.assertThat(agentWorkflowJobResult.size()).isEqualTo(1);
        AgentWorkflowJobPartsResult jobPartsRevisionResult = CicApiTestUtil.getMatchedRevisionWorkflowPartResult(agentWorkflowJobResult, "B");

        softAssertions.assertThat(jobPartsRevisionResult.getPartType()).isEqualTo("PART");
        softAssertions.assertThat(jobPartsRevisionResult.getInput()).isNotNull();
        softAssertions.assertThat(jobPartsRevisionResult.getResult()).isNotNull();
    }

    @AfterEach
    public void cleanup() {
        this.deleteWorkflow();
        softAssertions.assertAll();
        this.close();
    }
}
