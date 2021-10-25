package com.apriori.utils.http.enums;


/**
 * @author kpatel
 */
public interface EndpointEnum {

    String getEndpoint(Object... variables);

    String getEndpoint(String customer, Object... variables);

    String getEndpointString();
}
