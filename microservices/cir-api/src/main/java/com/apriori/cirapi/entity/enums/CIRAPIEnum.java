package com.apriori.cirapi.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CIRAPIEnum implements ExternalEndpointEnum {

    //Application Metadata
    REPORT_EXECUTIONS("rest_v2/reportExecutions"),
    REPORT_EXPORT_BY_REQUEST_ID("rest_v2/reportExecutions/%s/exports"),
    REPORT_OUTPUT_RESOURCE_BY_REQUEST_EXPORT_IDs("rest_v2/reportExecutions/%s/exports/%s/outputResource"),
    REPORT_OUTPUT_COMPONENT_JSON_BY_REQUEST_EXPORT_IDs("rest_v2/reportExecutions/%s/exports/%s/attachments/reportComponents.json");


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
        return PropertiesContext.get("${env}.reports.on_prem_vm_url") + String.format(getEndpointString(), variables);
    }
}