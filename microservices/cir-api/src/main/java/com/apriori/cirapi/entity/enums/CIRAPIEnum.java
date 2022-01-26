package com.apriori.cirapi.entity.enums;

import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

public enum CIRAPIEnum implements ExternalEndpointEnum {

    //Application Metadata
    CASTING_DTC_REPORT("rest_v2/reportExecutions"),
    REPORT_DETAILS_BY_REQUEST_ID("rest_v2/reportExecutions/%s"),
    REPORT_EXPORT_BY_REQUEST_ID("rest_v2/reportExecutions/%s/exports"),
    REPORT_OUTPUT_RESOURCE_BY_REQUEST_EXPORT_IDs("rest_v2/reportExecutions/%s/exports/%s/outputResource"),
    REPORT_OUTPUT_COMPONENT_JSON_BY_REQUEST_EXPORT_IDs("rest_v2/reportExecutions/%s/exports/%s/attachments/reportComponents.json");
//    CASTING_DTC_REPORT("/aPriori/reports/DTC Metrics/casting/castingDTC");


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
        return "https://conqbaci02/jasperserver-pro/" + String.format(getEndpointString(), variables);
    }
}