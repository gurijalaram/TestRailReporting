package com.apriori.interfaces;

import com.apriori.http.utils.ResponseWrapper;

public interface Request {

    <T> ResponseWrapper<T> get();

    <T> ResponseWrapper<T> post();

    <T> ResponseWrapper<T> getMultipart();

    <T> ResponseWrapper<T> postMultipart();

    <T> ResponseWrapper<T> put();

    <T> ResponseWrapper<T> delete();

    <T> ResponseWrapper<T> patch();
}
