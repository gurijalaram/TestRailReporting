package utils;

import com.apriori.utils.dataservice.TestDataService;

import entity.request.DefaultValues;
import entity.request.Query;
import entity.request.QueryFilter;
import entity.request.QueryFilters;
import entity.request.WorkflowRequest;
import entity.request.WorkflowRow;
import enums.CICPartSelectionType;
import enums.CostingInputFields;
import enums.MappingRule;
import enums.QueryDefinitionFieldType;
import enums.QueryDefinitionFields;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WorkflowDataUtil {

    private List<QueryFilter> queryFilters;
    private List<WorkflowRow> workflowRows;
    private WorkflowRequest workflowRequestData;
    private DefaultValues costingInputRows;

    public WorkflowDataUtil(CICPartSelectionType partSelectionType) {
        if (partSelectionType.getPartSelectionType().equals("QUERY")) {
            workflowRequestData = new TestDataService().getTestData("WorkflowQueryData.json", WorkflowRequest.class);
            workflowRequestData.setCustomer(CicApiTestUtil.getCustomerName());
            workflowRequestData.setPlmSystem(CicApiTestUtil.getAgent());
            workflowRequestData.setName("CIC" + System.currentTimeMillis());
            costingInputRows = workflowRequestData.getDefaultValues();
            queryFilters = new ArrayList<>();
        }
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
     * Set the boolean flag to use latest revision
     *
     * @param latestRevisionFlag
     * @return current class Object
     */
    public WorkflowDataUtil useLatestRevision(Boolean latestRevisionFlag) {
        workflowRequestData.setUseLatestRevision(latestRevisionFlag);
        return this;
    }
}
