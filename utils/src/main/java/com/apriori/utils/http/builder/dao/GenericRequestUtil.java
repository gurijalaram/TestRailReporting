package com.apriori.utils.http.builder.dao;

import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.interfaces.RequestArea;
import com.apriori.utils.http.utils.ResponseWrapper;

@Deprecated
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

    public static <T> ResponseWrapper<T> patch(RequestEntity requestEntity, RequestArea requestArea) {
        return  requestArea.patch(requestEntity);
    }

    public static <T> ResponseWrapper<T> postMultipart(RequestEntity requestEntity, RequestArea requestArea) {
        return  requestArea.postMultipart(requestEntity);
    }
}
