package com.apriori.utils.http.builder.interfaces;

import com.apriori.utils.http.utils.ResponseWrapper;

public interface Request {

    <T> ResponseWrapper<T> get();

    <T> ResponseWrapper<T> post();

    <T> ResponseWrapper<T> postMultipart();

    <T> ResponseWrapper<T> put();

    <T> ResponseWrapper<T> delete();

    <T> ResponseWrapper<T> patch();
}
