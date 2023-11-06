package com.apriori.shared.util.http.utils;

import io.restassured.http.Headers;

/**
 * Response wrapper.
 * Should wrap all HTTP responses, to provide the same communication interface with them.
 *
 * @param <T> - type of response entity.
 */
public class ResponseWrapper<T> {

    private int statusCode;
    private Headers headers;
    private String body;
    private T responseEntity;

    public ResponseWrapper(final int statusCode, final Headers headers, final String body, T responseEntity) {
        this.responseEntity = responseEntity;
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
    }

    public static <T> ResponseWrapper<T> build(final int statusCode, final Headers headers, final String body, T responseEntity) {
        return new ResponseWrapper<>(statusCode, headers, body, responseEntity);
    }

    public Headers getHeaders() {
        return headers;
    }

    public ResponseWrapper<T> setHeaders(Headers headers) {
        this.headers = headers;
        return this;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public T getResponseEntity() {
        return responseEntity;
    }
}
