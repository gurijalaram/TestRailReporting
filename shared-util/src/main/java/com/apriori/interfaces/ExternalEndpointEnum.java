package com.apriori.interfaces;

import com.apriori.http.utils.AwsParameterStoreUtil;
import com.apriori.properties.PropertiesContext;

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
            //TODO z: should be removed from config file when work with staging
            secretKey = PropertiesContext.get("secret_key");
        } catch (IllegalArgumentException e) {
            secretKey = AwsParameterStoreUtil.getSystemParameter("/" + PropertiesContext.get("aws_parameter_store_name") + "/shared/environment-secret-key");
        }

        return querySymbol + "key=" + secretKey;
    }
}
