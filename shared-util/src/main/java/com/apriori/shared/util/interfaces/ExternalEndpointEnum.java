package com.apriori.shared.util.interfaces;

import com.apriori.shared.util.http.utils.AwsParameterStoreUtil;
import com.apriori.shared.util.properties.PropertiesContext;

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

        return querySymbol + "key=" + getSecretKey();
    }

    default String getSecretKey() {
        String secretKey;
        try {
            //TODO : should be removed from config file when work with staging
            secretKey = PropertiesContext.get("secret_key");
        } catch (IllegalArgumentException e) {
            secretKey = AwsParameterStoreUtil.getSystemParameter("/" + PropertiesContext.get("aws_parameter_store_name") + "/shared/environment-secret-key");
        }
        return secretKey;
    }
}
