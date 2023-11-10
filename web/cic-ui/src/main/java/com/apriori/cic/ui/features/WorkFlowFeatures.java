package com.apriori.cic.ui.features;

import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.ui.enums.ConnectorComponentEnum;
import com.apriori.cic.ui.enums.RuleOperatorEnum;
import com.apriori.cic.ui.pageobjects.CICBasePage;
import com.apriori.cic.ui.pageobjects.workflows.WorkflowHome;
import com.apriori.cic.ui.pageobjects.workflows.schedule.costinginputs.CostingInputsPart;
import com.apriori.cic.ui.pageobjects.workflows.schedule.details.DetailsPart;
import com.apriori.cic.ui.pageobjects.workflows.schedule.details.WorkflowSchedule;
import com.apriori.cic.ui.pageobjects.workflows.schedule.querydefinitions.QueryDefinitions;
import com.apriori.shared.util.http.utils.GenerateStringUtil;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@Slf4j
public class WorkFlowFeatures extends CICBasePage {

    private QueryDefinitions queryDefinitions;
    private CostingInputsPart costingInputsPart;

    public WorkFlowFeatures(WebDriver driver) {
        super(driver);
    }

    /**
     * Creates a scheduled workflow.
     *
     * @return WorkflowHome page object
     */
    public WorkflowHome createScheduledWorkflow(WorkflowSchedule workflowSchedule) {
        workFlowData.setWorkflowName(GenerateStringUtil.saltString(workFlowData.getWorkflowName()));
        try {
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
                this.costingInputsPart = this.queryDefinitions.addRule(PlmTypeAttributes.PLM_PART_NUMBER, RuleOperatorEnum.EQUAL, workFlowData.getQueryDefinitionsData().get(0).getFieldValue()).clickWFQueryDefNextBtn();
            }
            return this.costingInputsPart.clickCINextBtn().clickCINotificationNextBtn().clickSaveButton();
        } catch (Exception e) {
            log.error("FAILED TO CREATE WORKFLOW!!!");
            throw new IllegalArgumentException(e);
        }
    }
}


