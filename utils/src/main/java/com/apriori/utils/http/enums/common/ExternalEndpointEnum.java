package com.apriori.utils.http.enums.common;

import com.apriori.utils.AwsParameterStoreUtil;
import com.apriori.utils.http.enums.EndpointEnum;
import com.apriori.utils.properties.PropertiesContext;

/**
 * @author kpatel
 */
public interface ExternalEndpointEnum extends EndpointEnum {

    default String getEndpoint(Object... variables) {
        return System.getProperty("baseUrl") + "ws" + String.format(getEndpointString(), variables);
    }

    default String addQuery(String endpointString) {
        String querySymbol = "?";

        if (endpointString.contains("?")) {
            querySymbol = "&";
        }

        String secretKey;
        try {
            secretKey = PropertiesContext.get("secret_key");
        } catch (IllegalArgumentException e) {
            secretKey = AwsParameterStoreUtil.getSystemParameter("");
        }

        return querySymbol + "key=" +
    }
}
