package com.apriori.cic.api.utils;

import com.apriori.cic.api.enums.CICPartSelectionType;
import com.apriori.cic.api.enums.CostingInputFields;
import com.apriori.cic.api.enums.EmailRecipientType;
import com.apriori.cic.api.enums.MappingRule;
import com.apriori.cic.api.enums.PlmTypeAttributes;
import com.apriori.cic.api.enums.PublishResultsWriteRule;
import com.apriori.cic.api.enums.QueryDefinitionFieldType;
import com.apriori.cic.api.enums.QueryDefinitionFields;
import com.apriori.cic.api.models.request.DefaultValues;
import com.apriori.cic.api.models.request.Query;
import com.apriori.cic.api.models.request.QueryFilter;
import com.apriori.cic.api.models.request.QueryFilters;
import com.apriori.cic.api.models.request.WorkflowRequest;
import com.apriori.cic.api.models.request.WorkflowRow;
import com.apriori.shared.util.dataservice.TestDataService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkflowDataUtil {

    private List<QueryFilter> queryFilters;
    private List<WorkflowRow> workflowRows;
    private WorkflowRequest workflowRequestData;
    private DefaultValues costingInputRows;
    private DefaultValues publishResultsWriteFieldRows;

    public WorkflowDataUtil(CICPartSelectionType partSelectionType) {
        if (partSelectionType.getPartSelectionType().equals("QUERY")) {
            workflowRequestData = new TestDataService().getTestData("WorkflowQueryData.json", WorkflowRequest.class);
            publishResultsWriteFieldRows = workflowRequestData.getPlmWriteConfiguration();
            queryFilters = new ArrayList<>();
        } else {
            workflowRequestData = new TestDataService().getTestData("WorkflowRestData.json", WorkflowRequest.class);
        }
        workflowRequestData.setName("CIC" + System.currentTimeMillis());
        costingInputRows = workflowRequestData.getDefaultValues();
    }

    /**
     * Set query definition group condition during workflow creation
     *
     * @param groupConditionOperator ("AND" or "OR")
     * @return current class
     */
    public WorkflowDataUtil setQueryFilters(String groupConditionOperator) {
        workflowRequestData.setQuery(Query.builder()
            .filters(QueryFilters.builder()
                .type(groupConditionOperator)
                .filters(queryFilters)
                .build())
            .build());
        return this;
    }

    /**
     * Set query filter during query definition fields with values
     *
     * @param fieldName  ("partNumber", "annualVolume" etc.,)
     * @param fieldType  ("EQ")
     * @param fieldValue (value of part number)
     * @return current class object
     */
    public WorkflowDataUtil setQueryFilter(String fieldName, String fieldType, Object fieldValue) {
        queryFilters.add(QueryFilter.builder()
            .fieldName(fieldName)
            .type(fieldType)
            .value(fieldValue)
            .build());
        return this;
    }

    /**
     * Set query filter during query definition fields with values
     *
     * @param fieldName  QueryDefinitionFields enum
     * @param fieldType  QueryDefinitionFieldType enum
     * @param fieldValue string
     * @return current class object
     */
    public WorkflowDataUtil setQueryFilter(QueryDefinitionFields fieldName, QueryDefinitionFieldType fieldType, Object fieldValue) {
        queryFilters.add(QueryFilter.builder()
            .fieldName(fieldName.getQueryDefinitionField())
            .type(fieldType.getQueryDefinitionFieldType())
            .value(fieldValue)
            .build());
        return this;
    }

    /**
     * Set query filter during query definition fields with values
     *
     * @param fieldName QueryDefinitionFields enum
     * @param fieldType QueryDefinitionFieldType enum
     * @param from      Integer value when QueryDefinitionFieldType is BETWEEN or NOTBETWEEN
     * @param to        Integer value when QueryDefinitionFieldType is BETWEEN or NOTBETWEEN
     * @return WorkflowDataUtil
     */
    public WorkflowDataUtil setQueryFilter(QueryDefinitionFields fieldName, QueryDefinitionFieldType fieldType, Object from, Object to) {
        queryFilters.add(QueryFilter.builder()
            .fieldName(fieldName.getQueryDefinitionField())
            .type(fieldType.getQueryDefinitionFieldType())
            .from(from)
            .to(to)
            .build());
        return this;
    }

    /**
     * get workflow data request builder
     *
     * @return WorkflowRequest class object
     */
    public WorkflowRequest build() {
        return workflowRequestData;
    }

    /**
     * Add costing input rows during workflow creation request
     *
     * @param costingInputField - CostingInputFields enum
     * @param mappingRule       - MappingRule enum
     * @param connectFieldValue - value
     * @return current class object
     */
    public WorkflowDataUtil addCostingInputRow(CostingInputFields costingInputField, MappingRule mappingRule, String connectFieldValue) {
        workflowRows = costingInputRows.getRows();
        workflowRows.add(WorkflowRow.builder()
            .identifier(UUID.randomUUID().toString())
            .isValid(true)
            .mappingRule(mappingRule.getMappingRule())
            .twxAttributeName(costingInputField.getCostingInputField())
            .value(connectFieldValue)
            ._isSelected(false)
            .build());

        costingInputRows.setRows(workflowRows);
        workflowRequestData.setDefaultValues(costingInputRows);
        return this;
    }

    /**
     * Add publish results write field rows during workflow creation request
     *
     * @param plmTypeAttributes - PlmTypeAttributes enum
     * @param writingRule       - PublishResultsWriteRule enum
     * @param connectFieldValue - value
     * @return current class object
     */
    public WorkflowDataUtil addPublishResultsWriteFieldsRow(PlmTypeAttributes plmTypeAttributes, PublishResultsWriteRule writingRule, String connectFieldValue) {
        workflowRows = publishResultsWriteFieldRows.getRows();
        workflowRows.add(WorkflowRow.builder()
            .identifier(UUID.randomUUID().toString())
            .isValid(true)
            .key(plmTypeAttributes.getKey())
            .value((writingRule.getWritingRule() == "CONSTANT") ? connectFieldValue : plmTypeAttributes.getValue())
            .writingRule(writingRule.getWritingRule())
            ._isSelected(false)
            .build());

        publishResultsWriteFieldRows.setRows(workflowRows);
        workflowRequestData.setPlmWriteConfiguration(publishResultsWriteFieldRows);
        return this;
    }

    /**
     * Set the boolean flag to use latest revision
     *
     * @param latestRevisionFlag
     * @return current class Object
     */
    public WorkflowDataUtil useLatestRevision(Boolean latestRevisionFlag) {
        workflowRequestData.setUseLatestRevision(latestRevisionFlag);
        return this;
    }

    /**
     * set the customer like widgets or ap-int
     *
     * @param customerName
     * @return current class object
     */
    public WorkflowDataUtil setCustomer(String customerName) {
        workflowRequestData.setCustomer(customerName);
        return this;
    }

    /**
     * Set the agent id
     *
     * @param connectorId
     * @return current class object
     */
    public WorkflowDataUtil setAgent(String connectorId) {
        workflowRequestData.setPlmSystem(connectorId);
        return this;
    }

    /**
     * Set the workflow Name
     *
     * @param workflowName
     * @return current class object
     */
    public WorkflowDataUtil setWorkflowName(String workflowName) {
        workflowRequestData.setName(workflowName);
        return this;
    }

    /**
     * workflow data with no costing input fields
     *
     * @return current class object
     */
    public WorkflowDataUtil emptyCostingInputRow() {
        workflowRequestData.setDefaultValues(null);
        return this;
    }

    /**
     * Set the agent id
     *
     * @param workflowDesc
     * @return current class object
     */
    public WorkflowDataUtil setWorkflowDescription(String workflowDesc) {
        workflowRequestData.setDescription(workflowDesc);
        return this;
    }

    /**
     * set workflow email template information
     * @param isEmailTemplate - Boolean (true or false)
     * @param reportName - reportName(Identity)
     * @param emailRecipientType - EmailRecipientType enum
     * @param emailAddress - email address
     * @return - current class object
     */
    public WorkflowDataUtil setEmailTemplate(Boolean isEmailTemplate, String reportName, EmailRecipientType emailRecipientType, String emailAddress) {
        workflowRequestData.setIsEmailTemplateSelected(isEmailTemplate);
        workflowRequestData.setSelectedEmailTemplate(reportName);
        if (isEmailTemplate) {
            workflowRequestData.setEmailRecipientType(emailRecipientType.getEmailRecipientType().toLowerCase());
            workflowRequestData.setEmailRecipientValue(emailAddress);
        }
        return this;
    }

    /**
     * set email report information during workflow creation
     * @param isEmailReportName - Boolean (true or false)
     * @param emailReportIdentity - Email Report template identity
     * @return current class object
     */
    public WorkflowDataUtil setEmailReport(Boolean isEmailReportName, String emailReportIdentity) {
        workflowRequestData.setIsEmailReportNameSelected(isEmailReportName);
        workflowRequestData.setSelectedEmailReportName(emailReportIdentity);
        if (!isEmailReportName) {
            workflowRequestData.setEmailReportConfiguration(null);
        }
        return this;
    }

    /**
     * Set PLM Write report information during workflow creation
     * @param isPlmWriteReport - Boolean (true or false)
     * @param plmWriteReportName - Report template identity
     * @return current class object
     */
    public WorkflowDataUtil setPlmWriteReport(Boolean isPlmWriteReport, String plmWriteReportName) {
        workflowRequestData.setIsPlmWriteReportNameSelected(isPlmWriteReport);
        workflowRequestData.setSelectedPlmWriteReportName(plmWriteReportName);
        return this;
    }
}
