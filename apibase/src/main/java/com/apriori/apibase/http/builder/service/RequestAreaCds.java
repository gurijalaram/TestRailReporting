package com.apriori.apibase.http.builder.service;

import com.apriori.apibase.http.builder.common.entity.RequestEntity;
import com.apriori.apibase.http.builder.interfaces.RequestArea;
import com.apriori.apibase.utils.ResponseWrapper;

public class RequestAreaCds implements RequestArea {

    @Override
    public <T> ResponseWrapper<T> get(RequestEntity requestEntity) {
        return RequestInitService.build(requestEntity)
                .connect()
                .get();
    }

    @Override
    public <T> ResponseWrapper<T> post(RequestEntity requestEntity) {
        return RequestInitService.build(requestEntity)
                .connect()
                .post();
    }

    @Override
    public <T> ResponseWrapper<T> postMultipart(RequestEntity requestEntity) {
        return RequestInitService.build(requestEntity)
                .connect()
                .postMultiPart();
    }

    @Override
    public <T> ResponseWrapper<T> put(RequestEntity requestEntity) {
        return RequestInitService.build(requestEntity)
                .connect()
                .put();
    }

    @Override
    public <T> ResponseWrapper<T> delete(RequestEntity requestEntity) {
        return RequestInitService.build(requestEntity)
                .connect()
                .delete();
    }

    @Override
    public <T> ResponseWrapper<T> patch(RequestEntity requestEntity) {
        return RequestInitService.build(requestEntity)
                .connect()
                .patch();
    }

}
