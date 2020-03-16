package com.apriori.apibase.http.builder.dao;

import com.apriori.apibase.http.builder.common.entity.RequestEntity;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.apibase.http.builder.service.RequestAreaUiAuth;
import com.apriori.utils.constants.Constants;

import com.apriori.utils.users.UserUtil;
import org.apache.http.HttpStatus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ServiceConnector {

    /**
     * Use this function to send a request url with out Rest Assured encoding the string first
     *
     * @param url
     * @param klass
     * @return
     */
    public static Object getServiceNoEncoding(String url, Class klass) {
//        return GenericRequestUtil.get(RequestEntity.initRequest(url, UserUtil.getUser(), klass)
//                .setFollowRedirection(true)
//                .);
        return new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setEndpoint(url)
            .setReturnType(klass)
            .setStatusCode(HttpStatus.SC_OK, HttpStatus.SC_MOVED_PERMANENTLY)
            .setFollowRedirection(true)
            .commitChanges()
            .connect()
            .disableEncoding()
            .get();
    }

    /**
     * Send a url encoded request
     *
     * @param url
     * @param klass
     * @return
     */
    public static Object getService(String url, Class klass) {
        return GenericRequestUtil.get(
                RequestEntity.init(url, UserUtil.getUser(), klass).setFollowRedirection(true),
                new RequestAreaUiAuth()
        );
//        return new HTTPRequest()
//            .unauthorized()
//            .customizeRequest()
//            .setEndpoint(url)
//            .setReturnType(klass)
//            .setStatusCode(HttpStatus.SC_OK, HttpStatus.SC_MOVED_PERMANENTLY)
//            .setFollowRedirection(true)
//            .commitChanges()
//            .connect()
//            .enableEncoding()
//            .get();
    }

    /**
     * Generate a url for a micro-service using default parameters
     *
     * @return
     */
    public static String getServiceUrl() {
        return getServiceUrl(null, null, null);
    }


    /**
     * Generate a url for a micro-service
     *
     * @param service
     * @return
     */
    public static String getServiceUrl(String host, String port, String secretKey) {
        if (host == null || host.isEmpty()) {
            host = Constants.getServiceHost();
        }

        if (port == null || port.isEmpty()) {
            port = Constants.getServicePort();
        }

        if (secretKey == null || secretKey.isEmpty()) {
            secretKey = Constants.getSecretKey();
        }

        StringBuilder url = new StringBuilder();
        url.append(String.format("http://%s", host));

        if (port != null) {
            url.append(String.format(":%s", port));
        }

        url.append("/%s?key=" + secretKey);
        return url.toString();
    }

    /**
     * Encode a url string using the Java encoder instead of the Rest-Assured encoder
     *
     * @param url
     * @param klass
     * @return
     */
    public static String urlEncode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("Unknown encoding" + e);
        }

    }

    /**
     * Only spaces are encoded in url parameters
     */
    public static String encodeSpaces(String url) {
        return url.replace(" ", "%20");
    }

}
