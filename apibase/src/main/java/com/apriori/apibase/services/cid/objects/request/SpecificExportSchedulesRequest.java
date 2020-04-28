package com.apriori.apibase.services.cid.objects.request;

/**
 *  Request data example
 *  {
 *    "type" : "exportSetRequestBean",
 *    "disabled" : false,
 *    "name" : "All Delta.Default Scenarios",
 *    "schedules" : [ {
 *       "type" : "SIMPLE",
 *       "startTime" : "1578921720000",
 *       "repeatCount" : 0,
 *       "repeatInterval" : 0
 *    } ],
 *    "description" : "",
 *    "exportDynamicRollups" : false,
 *    "exportScope" : "All Delta",
 *    "sourceSchema" : "Default Scenarios"
 * }
 */
public class SpecificExportSchedulesRequest extends ExportSchedulesRequest {

    private ScenarioKeyRequest scenarioKeyRequest;

    public ScenarioKeyRequest getScenarioKeyRequest() {
        return scenarioKeyRequest;
    }

    public SpecificExportSchedulesRequest setScenarioKeyRequest(ScenarioKeyRequest scenarioKeyRequest) {
        this.scenarioKeyRequest = scenarioKeyRequest;
        return this;
    }
}
