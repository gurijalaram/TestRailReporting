package com.apriori.utils.http.builder.dao;

import com.apriori.utils.constants.CommonConstants;

public class ServiceConnector {

    /**
     * Generate a url for a micro-service using default parameters
     *
     * @return
     */
    public static String getServiceUrl() {
        return getServiceUrl(null, null, null);
    }

    /**
     * Generate a url for a micro-service using default parameters
     *
     * @param host
     * @return
     */
    public static String getServiceUrl(String host) {
        return getServiceUrl(host, null, null);
    }

    /**
     * Generate a url for a micro-service
     *
     * @param host
     * @param port
     * @param secretKey
     * @return
     */
    public static String getServiceUrl(String host, String port, String secretKey) {
        if (host == null || host.isEmpty()) {
            host = CommonConstants.getServiceHost();
        }

        if (port == null || port.isEmpty()) {
            port = CommonConstants.getServicePort();
        }

        if (secretKey == null || secretKey.isEmpty()) {
            secretKey = CommonConstants.getSecretKey();
        }

        StringBuilder url = new StringBuilder();
        url.append(String.format("https://%s", host));

        if (port != null) {
            url.append(String.format(":%s", port));
        }

        url.append("/%s?key=").append(secretKey);
        return url.toString();
    }
}