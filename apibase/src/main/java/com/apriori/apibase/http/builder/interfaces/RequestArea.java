package com.apriori.apibase.http.builder.interfaces;

import com.apriori.apibase.http.builder.common.entity.RequestEntity;
import com.apriori.apibase.utils.ResponseWrapper;

public interface RequestArea {

    <T> ResponseWrapper<T> get(RequestEntity requestEntity);

    <T> ResponseWrapper<T> post(RequestEntity requestEntity);

    <T> ResponseWrapper<T> postMultipart(RequestEntity requestEntity);

    <T> ResponseWrapper<T> put(RequestEntity requestEntity);

    <T> ResponseWrapper<T> delete(RequestEntity requestEntity);

    <T> ResponseWrapper<T> patch(RequestEntity requestEntity);
}
