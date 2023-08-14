package com.apriori.utils.http.builder.interfaces;



public interface Request {

    <T> ResponseWrapper<T> get();

    <T> ResponseWrapper<T> post();

    <T> ResponseWrapper<T> getMultipart();

    <T> ResponseWrapper<T> postMultipart();

    <T> ResponseWrapper<T> put();

    <T> ResponseWrapper<T> delete();

    <T> ResponseWrapper<T> patch();
}
