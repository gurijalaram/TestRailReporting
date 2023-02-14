package com.apriori.cirapi.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CIRAPIEnum implements ExternalEndpointEnum {

    //Application Metadata
    DTC_METRICS("reports/aPriori/reports/DTC%sMetrics/casting/castingDTC/inputControls/useLatestExport;earliestExportDate;latestExportDate;exportSetName;rollup;costMetric;massMetric;sortOrder;currencyCode;annualSpendMin;processGroup;exportEventId;metricStatistic;dtcScore;outlierDistance;partsSelect/values"),
    REPORT_EXECUTIONS("reportExecutions"),
    REPORT_EXPORT_BY_REQUEST_ID("reportExecutions/%s/exports"),
    REPORT_OUTPUT_RESOURCE_BY_REQUEST_EXPORT_IDs("reportExecutions/%s/exports/%s/outputResource"),
    REPORT_OUTPUT_COMPONENT_JSON_BY_REQUEST_EXPORT_IDs("reportExecutions/%s/exports/%s/attachments/reportComponents.json");


    private final String endpoint;

    CIRAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("${env}.reports.ui_url") + "rest_v2/" + String.format(getEndpointString(), variables);
    }
}