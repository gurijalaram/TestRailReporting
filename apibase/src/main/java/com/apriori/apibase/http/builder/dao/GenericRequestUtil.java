package com.apriori.apibase.http.builder.dao;

import com.apriori.apibase.http.builder.common.entity.RequestEntity;
import com.apriori.apibase.http.builder.interfaces.RequestArea;
import com.apriori.apibase.utils.ResponseWrapper;

public class GenericRequestUtil {

    public static <T> ResponseWrapper<T> get(RequestEntity requestEntity, RequestArea requestArea) {
        return  requestArea.get(requestEntity);
    }

    public static <T> ResponseWrapper<T> post(RequestEntity requestEntity, RequestArea requestArea) {
        return  requestArea.post(requestEntity);
    }

    public static <T> ResponseWrapper<T> put(RequestEntity requestEntity, RequestArea requestArea) {
        return  requestArea.put(requestEntity);
    }

    public static <T> ResponseWrapper<T> delete(RequestEntity requestEntity, RequestArea requestArea) {
        return  requestArea.delete(requestEntity);
    }

}
