package com.apriori.bcs.controller;

import com.apriori.bcs.utils.Constants;

public class BcsBase {
    private static String baseUrl;

    static {
        baseUrl = Constants.getCisServiceHost() + "/customers";
    }

    /**
     * Return bcs url with customer identity appended
     *
     * @return url
     */
    public static String getCisUrl() {
        StringBuilder url = new StringBuilder(baseUrl).append("/").append(Constants.getCisCustomerIdentity())
                .append("/%s?key=").append(Constants.getSecretKey());
        return url.toString();
    }

    /**
     * Return base bcs url
     *
     * @return url
     */
    public static String getBaseCisUrl() {
        StringBuilder url = new StringBuilder(baseUrl).append("?key=").append(Constants.getSecretKey());
        return url.toString();
    }

    /**
     * Get report types url
     *
     * @return url
     */
    public static String getReportTypesUrl() {
        StringBuilder url;
        url = new StringBuilder("https://" + Constants.getCisServiceHost().concat("/report-types")).append("%s?key=")
                .append(Constants.getSecretKey());
        return url.toString();
    }

    /**
     * Get the base batch url (returns a list o batches)
     *
     * @return url
     */
    public static String getBatchUrl() {
        String url;
        url = getCisUrl();
        url = String.format(url, "batches%s");
        return url;
    }

    /**
     * Get batch url using the default batch identity
     *
     * @return url
     */
    public static String getBatchUrlWithIdentity() {
        String url = String.format(getBatchUrl(), "/" + Constants.getCisBatchIdentity() + "%s");
        return url;
    }

    /**
     * Get url with batch identity
     *
     * @param identity the batch identity
     * @return url
     */
    public static String getBatchUrlWithIdentity(String identity) {
        String url = String.format(getBatchUrl(), "/" + identity + "%s");
        return url;
    }
}