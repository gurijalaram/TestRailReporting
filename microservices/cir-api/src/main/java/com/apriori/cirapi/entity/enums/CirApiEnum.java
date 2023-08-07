package com.apriori.cirapi.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CirApiEnum implements ExternalEndpointEnum {

    SPEND_ANALYSIS_VALUE_TRACKING("reports/aPriori/reports/solutions/sourcing/spendAnalysisValueTracking/inputControls/projectRollup;exportDate;costMetric;currencyCode/values"),
    RECOMMENDED_TEST_PARTS("reports/aPriori/reports/Upgrade%sProcess/recommendedTestParts/inputControls/useLatestExport;".concat(
        "earliestExportDate;latestExportDate;exportSetName;exportEventId;processGroup;materials;processes;numberOfTopValuesShown/values/pagination?freshData=false&includeTotalCount=true")),

    DTC_METRICS("reports/aPriori/reports/DTC%sMetrics/casting/castingDTC/inputControls/useLatestExport;".concat(
        "earliestExportDate;latestExportDate;exportSetName;rollup;costMetric;massMetric;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;outlierDistance;partsSelect/values")),
    REPORT_EXECUTIONS("reportExecutions"),
    REPORT_EXPORT_BY_REQUEST_ID("reportExecutions/%s/exports"),
    REPORT_OUTPUT_STATUS_BY_REQUEST_EXPORT_IDs("reportExecutions/%s/exports/%s/status"),
    REPORT_OUTPUT_RESOURCE_BY_REQUEST_EXPORT_IDs("reportExecutions/%s/exports/%s/outputResource"),
    REPORT_OUTPUT_COMPONENT_JSON_BY_REQUEST_EXPORT_IDs("reportExecutions/%s/exports/%s/attachments/reportComponents.json");

    private final String endpoint;

    CirApiEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("reports.ui_url") + "rest_v2/" + String.format(getEndpointString(), variables);
    }
}
