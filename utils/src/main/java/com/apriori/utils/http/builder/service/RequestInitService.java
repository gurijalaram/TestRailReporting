package com.apriori.utils.http.builder.service;

import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.ConnectionManager;

public class RequestInitService {

    private RequestEntity requestEntity;

    public static RequestInitService build(RequestEntity requestEntity) {
        return new RequestInitService(requestEntity);
    }

    private RequestInitService(RequestEntity requestEntity) {
        requestEntity.setRequestInitService(this);
        this.requestEntity = requestEntity;
    }

    public RequestEntity customizeRequest() {
        return requestEntity;
    }

    public ConnectionManager<?> connect() {
        return new ConnectionManager<>(this.requestEntity, this.requestEntity.getReturnType());
    }
}
