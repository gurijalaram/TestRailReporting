package com.apriori.pagedata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkFlowData {

    private String workflowName;
    private String workflowDescription;
    private String connectorName;
    private String componentName;
    private ScheduleData scheduleData;
    private List<QueryDefinitionsData> queryDefinitionsData;
    private List<CostingInputsData> costingInputsData;
    private NotificationsData notificationsData;
    private PublishResultsData publishResultsData;
}
