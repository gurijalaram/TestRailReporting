package com.apriori.utils.http.enums.common;

import com.apriori.utils.http.enums.EndpointEnum;

/**
 * @author kpatel
 */
public interface ExternalEndpointEnum extends EndpointEnum {

    default String getEndpoint(Object... variables) {
        return System.getProperty("baseUrl") + "ws" + String.format(getEndpointString(), variables);
    }

    default String getEndpoint(String customer, Object... variables) {
        return System.getProperty("baseUrl") + "ws" + String.format(getEndpointString(), variables);
    }

    default String getSchemaLocation() {
        return null;
    }
}
