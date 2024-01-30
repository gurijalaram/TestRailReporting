package com.apriori.bcm.api.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum BcmAppAPIEnum implements ExternalEndpointEnum {

    //WORKSHEETS
    WORKSHEETS("worksheets"),
    WORKSHEET_BY_ID("worksheets/%s"),
    WORKSHEET_INPUT_NAME("worksheets/%s/inputRows");


    private final String endpoint;

    BcmAppAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return PropertiesContext.get("bcm.api_url")
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}

