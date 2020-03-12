package com.apriori.apibase.utils;

public class ResponseWrapper<T> {

    private int statusCode;
    private String body;
    private T responseEntity;

    public static <T>  ResponseWrapper<T> build(int statusCode, String body,  T responseEntity){
        return new ResponseWrapper<T>(statusCode, body, responseEntity);
    }

    public ResponseWrapper(int statusCode, String body,  T responseEntity) {
        this.responseEntity = responseEntity;
        this.statusCode = statusCode;
        this.body = body;
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

    public void setResponseEntity(T responseEntity) {
        this.responseEntity = responseEntity;
    }
}
