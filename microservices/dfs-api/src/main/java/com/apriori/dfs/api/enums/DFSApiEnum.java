package com.apriori.dfs.api.enums;

import com.apriori.interfaces.ExternalEndpointEnum;
import com.apriori.properties.PropertiesContext;

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
        return PropertiesContext.get("dfs.api_url") + String.format(getEndpointString(), variables) + this.getSharedSecret(String.format(getEndpointString(), variables));
    }

    private String getSharedSecret(String endpointString) {
        if (endpointString.endsWith("/") || endpointString.contains("key=")) {
            return "";
        } else {
            return customAddQuery(this.getEndpointString(), PropertiesContext.get("dfs.authorization_key"));
        }
    }

    private String customAddQuery(String endpointString, String sharedSecret) {
        String querySymbol = "?";
        if (endpointString.contains("?")) {
            querySymbol = "&";
        }
        return querySymbol + "key=" + sharedSecret;
    }
}
