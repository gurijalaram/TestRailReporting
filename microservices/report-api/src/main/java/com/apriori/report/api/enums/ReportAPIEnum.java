package com.apriori.report.api.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum ReportAPIEnum implements ExternalEndpointEnum {

    REPORT_EXECUTE("/customers/%s/reports/report-1/execute"),
    REPORT_STATUS("/customers/%s/reports/report-1/executions/%s/status");

    private final String endpoint;

    ReportAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return String.format((PropertiesContext.get("report_replica.api_url")).concat("%s"), String.format(getEndpointString(), variables));
    }
}
