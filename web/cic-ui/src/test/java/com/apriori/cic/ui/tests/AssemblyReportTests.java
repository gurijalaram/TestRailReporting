package com.apriori.cic.ui.tests;

import static com.apriori.shared.util.testconfig.TestSuiteType.TestSuite.SMOKE;

import com.apriori.cic.api.enums.CICPartSelectionType;
import com.apriori.cic.api.enums.CICReportType;
import com.apriori.cic.api.enums.CostingInputFields;
import com.apriori.cic.api.enums.EmailRecipientType;
import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmPartDataType;
import com.apriori.cic.api.enums.QueryDefinitionFields;
import com.apriori.cic.api.enums.ReportsEnum;
import com.apriori.cic.api.models.response.AgentWorkflowJobResults;
import com.apriori.cic.api.models.response.AgentWorkflowReportTemplates;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.api.utils.WorkflowDataUtil;
import com.apriori.cic.ui.utils.CicGuiTestUtil;
import com.apriori.shared.util.email.GraphEmailService;
import com.apriori.shared.util.file.part.PartData;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.models.response.EmailMessage;
import com.apriori.shared.util.models.response.EmailMessageAttachments;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AssemblyReportTests extends CicGuiTestUtil {

    private static String scenarioName;
    private static SoftAssertions softAssertions;
    private static List<PartData> plmPartData;
    private AgentWorkflowJobResults agentWorkflowJobResults;

    @BeforeEach
    public void testSetup() {
        softAssertions = new SoftAssertions();
        currentUser = UserUtil.getUser();
        String randomNumber = RandomStringUtils.randomNumeric(6);
        scenarioName = PropertiesContext.get("customer") + randomNumber;
    }

    @Test
    @Tag(SMOKE)
    @TestRail(id = {11103, 11096, 11109, 11968})
    @Description("Assemblies are returned from Windchill with default agent configuration" +
        "DTC Component Summary Reports are generated for assemblies")
    public void testVerifyDTCReportForAssemblies() {
        plmPartData = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_ASSEMBLY, 6);
        this.cicGuiLogin();
        AgentWorkflowReportTemplates reportTemplateNames = CicApiTestUtil.getAgentReportTemplates(CICReportType.EMAIL, this.ciConnectHome.getSession());
        workflowRequestDataBuilder = new WorkflowDataUtil(CICPartSelectionType.QUERY)
            .setQueryFilter(QueryDefinitionFields.STRING1.getQueryDefinitionField(), "EQ", "Auto Asm Test 1")
            .setQueryFilters("AND")
            .addCostingInputRow(CostingInputFields.PROCESS_GROUP, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.MATERIAL, MappingRule.MAPPED_FROM_PLM, "")
            .addCostingInputRow(CostingInputFields.SCENARIO_NAME, MappingRule.CONSTANT, scenarioName)
            .setEmailTemplate(true, "dfmPartSummary", EmailRecipientType.CONSTANT, PropertiesContext.get("global.report_email_address"))
            .setEmailReport(true, CicApiTestUtil.getAgentReportTemplate(reportTemplateNames, ReportsEnum.DTC_COMPONENT_SUMMARY).getValue())
            .setPlmWriteReport(true, CicApiTestUtil.getAgentReportTemplate(reportTemplateNames, ReportsEnum.DTC_COMPONENT_SUMMARY).getValue())
            .useLatestRevision(true)
            .build();

        AgentWorkflowJobResults agentWorkflowJobResults = this.createWorkflow().getWorkflow().invokeWorkflow().trackWorkflow().getJobResult();
        softAssertions.assertThat(agentWorkflowJobResults.size()).isEqualTo(6);
        softAssertions.assertThat(agentWorkflowJobResults.stream().filter(result -> result.getPartType().equals("ASSEMBLY")).count()).isEqualTo(3);
        softAssertions.assertThat(agentWorkflowJobResults.stream().filter(result -> result.getPartType().equals("PART")).count()).isEqualTo(3);
        softAssertions.assertThat(this.verifyPartNumberInJobResult(agentWorkflowJobResults, plmPartData)).isTrue();
        softAssertions.assertThat(this.verifyPartNameInJobResult(agentWorkflowJobResults, plmPartData)).isTrue();


        // Read the email and verify content and attached watch point report
        EmailMessage emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        softAssertions.assertThat(emailMessage.getBody().getContent().contains(scenarioName)).isTrue();
        softAssertions.assertThat(emailMessage.getBody().getContent().contains("aP Generate Analysis Summary")).isTrue();
        EmailMessageAttachments emailMessageAttachments = emailMessage.emailMessageAttachments();
        softAssertions.assertThat(emailMessageAttachments.value.size()).isEqualTo(6);

        softAssertions.assertThat(this.verifyEmailAttachedReportName(emailMessageAttachments, plmPartData)).isTrue();
        softAssertions.assertThat(this.verifyPdfDocumentContent(emailMessageAttachments, plmPartData)).isTrue();

        emailMessage.deleteEmailMessage();

    }

    @AfterEach
    public void cleanup() {
        softAssertions.assertAll();
        CicApiTestUtil.deleteWorkFlow(this.ciConnectHome.getSession(), CicApiTestUtil.getMatchedWorkflowId(workflowRequestDataBuilder.getName()));
    }
}