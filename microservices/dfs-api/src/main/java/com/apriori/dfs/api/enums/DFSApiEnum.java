package com.apriori.dfs.api.enums;

import com.apriori.interfaces.ExternalEndpointEnum;
import com.apriori.properties.PropertiesContext;

import java.util.Arrays;

public enum DFSApiEnum implements ExternalEndpointEnum {

    // DIGITAL FACTORY
    DIGITAL_FACTORIES("digital-factories"),
    DIGITAL_FACTORIES_BY_PATH_PARAMETER("digital-factories/%s");

    private final String endpoint;

    DFSApiEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        String endpoint = PropertiesContext.get("dfs.api_url") + String.format(getEndpointString(), variables);
        String sharedSecretKey = "key=";
        if (Arrays.stream(variables).anyMatch(o -> o.toString().contains(sharedSecretKey) || o.toString().isEmpty())) {
            return endpoint;
        }
        return endpoint + this.addQuery(getEndpointString());
    }

    @Override
    public String addQuery(String endpointString) {
        return endpointString.contains("?") ? "&" : "?" + "key=" + PropertiesContext.get("dfs.authorization_key");
    }
}
