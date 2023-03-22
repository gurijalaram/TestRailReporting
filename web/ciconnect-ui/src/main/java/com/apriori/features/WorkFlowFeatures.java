package com.apriori.features;

import com.apriori.enums.ConnectorComponentEnum;
import com.apriori.pages.CICBasePage;
import com.apriori.pages.workflows.WorkflowHome;
import com.apriori.pages.workflows.schedule.costinginputs.CostingInputsPart;
import com.apriori.pages.workflows.schedule.details.DetailsPart;
import com.apriori.pages.workflows.schedule.details.WorkflowSchedule;
import com.apriori.pages.workflows.schedule.notifications.NotificationsPart;
import com.apriori.pages.workflows.schedule.publishresults.PublishResultsPart;
import com.apriori.pages.workflows.schedule.querydefinitions.QueryDefinitions;
import com.apriori.utils.GenerateStringUtil;

import org.openqa.selenium.WebDriver;

public class WorkFlowFeatures extends CICBasePage {

    private QueryDefinitions queryDefinitions;
    private CostingInputsPart costingInputsPart;
    private NotificationsPart notificationsPart;
    private PublishResultsPart publishResultsPart;
    private WorkflowHome workflowHome;

    public WorkFlowFeatures(WebDriver driver) {
        super(driver);
    }

    /**
     * Creates a basic workflow.
     *
     * @return WorkflowHome page object
     */
    public WorkflowHome createWorkflow() {
        if (workFlowData.getComponentName().equals(ConnectorComponentEnum.QUERY_DEFINITION.getConnectorComponentName())) {
            this.queryDefinitions = (QueryDefinitions) new DetailsPart(driver).enterWorkflowNameField(workFlowData.getWorkflowName())
                .selectWorkflowConnector(workFlowData.getConnectorName())
                .clickWFDetailsNextBtn();
        } else {
            this.costingInputsPart = (CostingInputsPart) new DetailsPart(driver).enterWorkflowNameField(workFlowData.getWorkflowName())
                .selectWorkflowConnector(workFlowData.getConnectorName())
                .clickWFDetailsNextBtn();
        }
        if (this.queryDefinitions != null) {
            this.costingInputsPart = this.queryDefinitions.addRule(workFlowData, workFlowData.getQueryDefinitionsData().size()).clickWFQueryDefNextBtn();
        }
        this.notificationsPart = this.costingInputsPart.addCostingInputFields(workFlowData.getCostingInputsData().size()).clickCINextBtn();
        this.publishResultsPart = notificationsPart.selectEmailTab().selectEmailTemplate().selectRecipient().clickCINotificationNextBtn();
        return this.publishResultsPart.selectAttachReportTab().selectReportName().selectCurrencyCode().selectCostRounding().clickSaveButton();
    }

    /**
     * Creates a scheduled workflow.
     *
     * @return WorkflowHome page object
     */
    public WorkflowHome createScheduledWorkflow(WorkflowSchedule workflowSchedule) {
        workFlowData.setWorkflowName(GenerateStringUtil.saltString(workFlowData.getWorkflowName()));
        if (workFlowData.getComponentName().equals(ConnectorComponentEnum.QUERY_DEFINITION.getConnectorComponentName())) {
            this.queryDefinitions = (QueryDefinitions) new DetailsPart(driver).enterWorkflowNameField(workFlowData.getWorkflowName())
                .selectWorkflowConnector(workFlowData.getConnectorName())
                .selectEnabledCheckbox("on")
                .setSchedule(workflowSchedule)
                .clickWFDetailsNextBtn();
        } else {
            this.costingInputsPart = (CostingInputsPart) new DetailsPart(driver).enterWorkflowNameField(workFlowData.getWorkflowName())
                .selectWorkflowConnector(workFlowData.getConnectorName())
                .clickWFDetailsNextBtn();
        }
        if (this.queryDefinitions != null) {
            this.costingInputsPart = this.queryDefinitions.addRule(workFlowData, workFlowData.getQueryDefinitionsData().size()).clickWFQueryDefNextBtn();
        }
        return this.costingInputsPart.clickCINextBtn().clickCINotificationNextBtn().clickSaveButton();
    }

    /**
     * Edit workflow name
     *
     * @return WorkflowHome page object
     */
    public WorkflowHome editWorkflow() {
        if (workFlowData.getComponentName().equals(ConnectorComponentEnum.QUERY_DEFINITION.getConnectorComponentName())) {
            this.queryDefinitions = (QueryDefinitions) new DetailsPart(driver).enterWorkflowNameField(workFlowData.getWorkflowName()).clickWFDetailsNextBtn();
        } else {
            this.costingInputsPart = (CostingInputsPart) new DetailsPart(driver).enterWorkflowNameField(workFlowData.getWorkflowName()).clickWFDetailsNextBtn();
        }
        if (this.queryDefinitions != null) {
            this.costingInputsPart = this.queryDefinitions.clickWFQueryDefNextBtn();
        }
        this.notificationsPart = this.costingInputsPart.clickCINextBtn();
        this.publishResultsPart = notificationsPart.clickCINotificationNextBtn();
        return this.publishResultsPart.clickSaveButton();
    }
}


