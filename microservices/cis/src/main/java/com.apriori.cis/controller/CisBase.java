package com.apriori.cis.controller;

import com.apriori.cis.utils.Constants;
import com.apriori.utils.constants.CommonConstants;

public class CisBase {
    private static String baseUrl;

    static {
        baseUrl = "https://" + Constants.getCisServiceHost() + "/customers";
    }

    public static String getCisUrl() {
        StringBuilder url = new StringBuilder(baseUrl).append("/").append(Constants.getCisCustomerIdentity())
                .append("/%s?key=").append(Constants.getSecretKey());
        return url.toString();
    }

    public static String getBaseCisUrl() {
        StringBuilder url = new StringBuilder(baseUrl).append("?key=").append(CommonConstants.getSecretKey());
        return url.toString();
    }

    public static String getReportTypesUrl() {
        StringBuilder url;
        url = new StringBuilder("https://" + Constants.getCisServiceHost().concat("/report-types")).append("%s?key=")
                .append(Constants.getSecretKey());
        return url.toString();
    }

    public static String getBatchUrl() {
        String url;
        url = getCisUrl();
        url = String.format(url, "batches%s");
        return url;
    }

    public static String getBatchUrlWithIdentity() {
        String url = String.format(getBatchUrl(), "/" + Constants.getCisBatchIdentity() + "%s");
        return url;
    }

    public static String getBatchUrlWithIdentity(String identity) {
        String url = String.format(getBatchUrl(), "/" + identity + "%s");
        return url;
    }


}
