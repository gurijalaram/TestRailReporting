package com.apriori.dds.enums;

import com.apriori.interfaces.ExternalEndpointEnum;
import com.apriori.properties.PropertiesContext;

public enum DDSApiEnum implements ExternalEndpointEnum {

    // REPORTS
    CUSTOMER_DISCUSSIONS("%s/discussions"),
    CUSTOMER_DISCUSSION(CUSTOMER_DISCUSSIONS.getEndpointString().concat("/%s")),
    CUSTOMER_DISCUSSION_COMMENTS(CUSTOMER_DISCUSSION.getEndpointString().concat("/comments")),
    CUSTOMER_DISCUSSION_COMMENT(CUSTOMER_DISCUSSION_COMMENTS.getEndpointString().concat("/%s")),
    CUSTOMER_SEARCH_DISCUSSIONS("%s/discussions/search");

    private final String endpoint;

    DDSApiEnum(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String getEndpointString() {
        return endpoint;
    }

    @Override
    public String getEndpoint(Object... variables) {

        return PropertiesContext.get("dds.api_url") + "customers/"
            + String.format(getEndpointString(), variables) + this.addQuery(getEndpointString());
    }
}
