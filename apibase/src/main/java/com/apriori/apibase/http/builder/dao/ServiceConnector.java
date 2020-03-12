package com.apriori.apibase.http.builder.dao;

import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.utils.constants.Constants;

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
        return new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setEndpoint(url)
                .setReturnType(klass)
                .setStatusCode(HttpStatus.SC_OK, HttpStatus.SC_MOVED_PERMANENTLY)
                .setFollowRedirection(true)
                .commitChanges()
                .connect()
                .enableEncoding()
                .get();
    }

    /** Send a url encoded request
     *
     * @param url
     * @param klass
     * @param statusCode
     * @return
             */
    public static Object getService(String url, Class klass, int statusCode) {
        return new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setEndpoint(url)
                .setReturnType(klass)
                .setStatusCode(statusCode)
                .setFollowRedirection(true)
                .commitChanges()
                .connect()
                .enableEncoding()
                .get();
    }

    /**
     * Send a post request to a service
     *
     * @param url
     * @param klass
     * @param body
     * @return
     */
    public static Object postToService(String url, Class klass, Object body) {
        return new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setEndpoint(url)
                .setBody(body)
                .setReturnType(klass)
                .setStatusCode(HttpStatus.SC_OK, HttpStatus.SC_MOVED_PERMANENTLY)
                .setFollowRedirection(true)
                .commitChanges()
                .connect()
                .post();
    }

    /**
     * Send a post request to a service
     *
     * @param url
     * @param klass
     * @param body
     * @return
     */
    public static Object postToService(String url, Class klass, Object body, int statusCode) {
        return new HTTPRequest()
                .unauthorized()
                .customizeRequest()
                .setEndpoint(url)
                .setBody(body)
                .setReturnType(klass)
                .setStatusCode(statusCode)
                .setFollowRedirection(true)
                .commitChanges()
                .connect()
                .post();
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
     * Generate a url for a micro-service using default parameters
     *
     * @param host
     *
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
     *
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
        url.append(String.format("https://%s", host));

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
