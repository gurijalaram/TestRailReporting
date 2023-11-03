package com.apriori.cic.agent.enums;

import com.apriori.shared.util.interfaces.ExternalEndpointEnum;
import com.apriori.shared.util.properties.PropertiesContext;

public enum NexusAPIEnum implements ExternalEndpointEnum {

    NEXUS_CIC_AGENT_SEARCH("service/rest/v1/search?repository=%s"),
    NEXUS_CIC_AGENT_SEARCH_BY_GROUP("service/rest/v1/search?repository=%s&group=%s"),
    NEXUS_CIC_AGENT_DOWNLOAD_URL("repository/%s/%s");

    private String endpoint;

    NexusAPIEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {
        return (getEndpointString().contains(PropertiesContext.get("global.nexus.api_url")))
            ? String.format(getEndpointString(), variables) : PropertiesContext.get("global.nexus.api_url") + String.format(getEndpointString(), variables);
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
