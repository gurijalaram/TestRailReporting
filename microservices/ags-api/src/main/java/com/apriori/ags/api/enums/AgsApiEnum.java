package com.apriori.ags.api.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum AgsApiEnum implements ExternalEndpointEnum {
    WORKSHEETS("/worksheets"),
    WORKSHEET_BY_ID("/worksheets/%s"),
    WORKSHEET_INPUT_NAME("/worksheets/%s/inputRows"),
    ANALYSIS_INPUTS("/worksheets/%s/analysisInputs"),
    DELETE_INPUTS("/worksheets/%s/inputRows/delete"),
    EDIT_INPUTS("/worksheets/%s/inputRows/edit"),
    COST_WORKSHEET("/worksheets/%s/cost"),
    MULTIPLE_ROWS("/worksheets/%s/inputRowGroup"),
    DELETE_MULTIPLE_WORKSHEETS("/worksheets/delete"),
    CANDIDATES("/worksheets/%s/candidates");

    private final String endpoint;

    AgsApiEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("${env}.ags.api_url") + String.format(getEndpointString(), variables);
    }
}