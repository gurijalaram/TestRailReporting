package com.integration.tests;

import com.apriori.cic.api.enums.PlmPartDataType;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.models.request.AgentPort;
import com.apriori.cic.api.models.request.JobDefinition;
import com.apriori.cic.api.models.response.AgentWorkflow;
import com.apriori.cic.api.models.response.AgentWorkflowJobRun;
import com.apriori.cic.api.models.response.AgentWorkflowReportTemplates;
import com.apriori.cic.api.models.response.ReportTemplatesRow;
import com.apriori.cic.api.utils.CicApiTestUtil;
import com.apriori.cic.api.utils.CicLoginUtil;
import com.apriori.cic.api.utils.PlmPartsUtil;
import com.apriori.cic.ui.enums.RuleOperatorEnum;
import com.apriori.cic.ui.enums.SortedOrderType;
import com.apriori.cic.ui.enums.WorkflowListColumns;
import com.apriori.cic.ui.pagedata.WorkFlowData;
import com.apriori.cic.ui.pageobjects.login.CicLoginPage;
import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;
import com.apriori.cic.ui.pageobjects.workflows.schedule.SchedulePage;
import com.apriori.cic.ui.pageobjects.workflows.schedule.costinginputs.CostingInputsPart;
import com.apriori.cic.ui.pageobjects.workflows.schedule.notifications.NotificationsPart;
import com.apriori.cic.ui.pageobjects.workflows.schedule.publishresults.PublishResultsPart;
import com.apriori.cic.ui.pageobjects.workflows.schedule.querydefinitions.QueryDefinitions;
import com.apriori.nts.api.reports.partscost.PartsCost;
import com.apriori.shared.util.ExcelService;
import com.apriori.shared.util.dataservice.TestDataService;
import com.apriori.shared.util.email.GraphEmailService;
import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.utils.GenerateStringUtil;
import com.apriori.shared.util.http.utils.ResponseWrapper;
import com.apriori.shared.util.models.response.EmailMessage;
import com.apriori.shared.util.properties.PropertiesContext;
import com.apriori.shared.util.testconfig.TestBaseUI;
import com.apriori.shared.util.testrail.TestRail;

import io.qameta.allure.Description;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class CICIntegrationTests extends TestBaseUI {

    private static String loginSession;
    private static AgentWorkflow agentWorkflowResponse;
    private static JobDefinition jobDefinitionData;
    private static AgentWorkflowJobRun agentWorkflowJobRunResponse;
    private static String workflowName;
    private static String scenarioName;
    private static String workflowData;
    private static SoftAssertions softAssertions;
    private static AgentWorkflowReportTemplates reportTemplateNames;
    private static ReportTemplatesRow reportTemplateName;
    private UserCredentials currentUser;
    private AgentPort agentPort;
    private String plmPartNumber;

    public CICIntegrationTests() {
        super();
    }

    @BeforeEach
    public void setup() {
        softAssertions = new SoftAssertions();
        jobDefinitionData = new TestDataService().getTestData("CicGuiDeleteJobDefData.json", JobDefinition.class);
        currentUser = UserUtil.getUser();
        agentPort = CicApiTestUtil.getAgentPortData();
        String randomNumber = RandomStringUtils.randomNumeric(6);
        workflowName = "CIC_REPORT" + randomNumber;
        scenarioName = PropertiesContext.get("customer") + randomNumber;
        plmPartNumber = new PlmPartsUtil().getPlmPartData(PlmPartDataType.PLM_PART_GENERAL).getPlmPartNumber();
    }

    @Test
    @TestRail(id = 12045)
    @Description("Test creating, invoking, tracking and deletion of a workflow")
    public void testCreateAndDeleteWorkflow() {
        WorkFlowData workFlowData = new TestDataService().getTestData("WorkFlowData.json", WorkFlowData.class);
        workFlowData.setWorkflowName(GenerateStringUtil.saltString(workFlowData.getWorkflowName()));
        log.info(String.format("Start Creating Workflow >> %s <<", workFlowData.getWorkflowName()));
        SchedulePage schedulePage = new CicLoginPage(driver)
            .login(currentUser)
            .clickWorkflowMenu()
            .setTestData(workFlowData)
            .selectScheduleTab();

        QueryDefinitions queryDefinitions = (QueryDefinitions) schedulePage
            .clickNewButton()
            .enterWorkflowNameField(workFlowData.getWorkflowName())
            .selectWorkflowConnector(workFlowData.getConnectorName())
            .clickWFDetailsNextBtn();

        CostingInputsPart costingInputsPart = queryDefinitions.addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, plmPartNumber)
            .clickWFQueryDefNextBtn();

        NotificationsPart notificationsPart = costingInputsPart.addCostingInputFields(workFlowData.getCostingInputsData().size()).clickCINextBtn();
        PublishResultsPart publishResultsPart = notificationsPart.selectEmailTab().selectEmailTemplate().selectRecipient().clickCINotificationNextBtn();
        WorkflowHome workflowHome = publishResultsPart.selectAttachReportTab().selectReportName().selectCurrencyCode().selectCostRounding().clickSaveButton();
        log.info(String.format("created Workflow confirmation >> %s <<", workflowHome.getWorkFlowStatusMessage()));

        softAssertions.assertThat(workflowHome.getWorkFlowStatusMessage()).isEqualTo("Job definition created");
        softAssertions.assertThat(schedulePage.isWorkflowListIsSorted(WorkflowListColumns.Name, SortedOrderType.ASCENDING, workFlowData.getWorkflowName())).isTrue();

        log.info(String.format("Delete Workflow >> %s <<", workFlowData.getWorkflowName()));

        softAssertions.assertThat(schedulePage.deleteWorkFlow(workFlowData.getWorkflowName())).isTrue();
        softAssertions.assertAll();
    }

    @Test
    @TestRail(id = 12046)
    @Description("Create Workflow, Invoke workflow, verify Parts Cost watchpoint report from email and delete workflow")
    public void testVerifyWatchPointReport() {
        loginSession = new CicLoginUtil(driver).login(currentUser).navigateToUserMenu().getWebSession();
        workflowData = String.format(CicApiTestUtil.getWorkflowData("WatchPointReportData.json"), CicApiTestUtil.getCustomerName(),
            CicApiTestUtil.getAgent(loginSession), workflowName, scenarioName);
        workflowData.replace("<PART_NUMBER>", plmPartNumber);
        // Create WorkFlow
        PartsCost xlsWatchPointReportExpectedData = new TestDataService().getReportData("PartCostReport.json", PartsCost.class);
        ResponseWrapper<String> responseWrapper = CicApiTestUtil.createWorkflow(loginSession, workflowData);
        softAssertions.assertThat(responseWrapper.getBody()).contains("CreateJobDefinition");
        softAssertions.assertThat(responseWrapper.getBody()).contains(">true<");
        agentWorkflowResponse = CicApiTestUtil.getMatchedWorkflowId(workflowName);
        softAssertions.assertThat(agentWorkflowResponse.getId()).isNotNull();

        //Run the workflow
        agentWorkflowJobRunResponse = CicApiTestUtil.runCicAgentWorkflow(agentWorkflowResponse.getId(), AgentWorkflowJobRun.class, HttpStatus.SC_OK);
        softAssertions.assertThat(agentWorkflowJobRunResponse.getJobId()).isNotNull();

        // Verify workflow job is finished.
        softAssertions.assertThat(CicApiTestUtil.trackWorkflowJobStatus(agentWorkflowResponse.getId(), agentWorkflowJobRunResponse.getJobId())).isTrue();

        //Verify Email Notification
        EmailMessage emailMessage = GraphEmailService.searchEmailMessageWithAttachments(scenarioName);
        softAssertions.assertThat(emailMessage).isNotNull();
        ExcelService excelReport = (ExcelService) emailMessage.emailMessageAttachment().getFileAttachment();
        softAssertions.assertThat(excelReport).isNotNull();
        softAssertions.assertThat(excelReport.getSheetCount()).isEqualTo(7);
        softAssertions.assertThat(excelReport.getFirstCellRowNum("Part Number")).isGreaterThan(0);
        softAssertions.assertThat(excelReport.getSheetNames()).contains(xlsWatchPointReportExpectedData.getPartCostReport().getTitle());
        emailMessage.deleteEmailMessage();
    }

    @AfterEach
    public void cleanup() {
        CicApiTestUtil.deleteWorkFlow(loginSession, CicApiTestUtil.getMatchedWorkflowId(workflowName));
        softAssertions.assertAll();
    }
}
