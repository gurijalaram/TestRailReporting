package com.apriori.utils.http.builder.service;

import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.interfaces.RequestArea;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http2.builder.service.HTTP2Request;

public class RequestAreaApi implements RequestArea {

    @Override
    public <T> ResponseWrapper<T> get(RequestEntity requestEntity) {
        return RequestInitService.build(requestEntity)
                .customizeRequest()
                .setFollowRedirection(true)
                .commitChanges()
                .connect()
                .get();
    }

    @Override
    public <T> ResponseWrapper<T> post(RequestEntity requestEntity) {
        return RequestInitService.build(requestEntity)
                .customizeRequest()
                .setFollowRedirection(true)
                .commitChanges()
                .connect()
                .post();
    }

    @Override
    public <T> ResponseWrapper<T> postMultipart(RequestEntity requestEntity) {
        return RequestInitService.build(requestEntity)
                .customizeRequest()
                .setFollowRedirection(true)
                .commitChanges()
                .connect()
                .postMultiPart();
    }

    @Override
    public <T> ResponseWrapper<T> put(RequestEntity requestEntity) {
        return RequestInitService.build(requestEntity)
                .customizeRequest()
                .setFollowRedirection(true)
                .commitChanges()
                .connect()
                .put();
    }

    @Override
    public <T> ResponseWrapper<T> delete(RequestEntity requestEntity) {
        return RequestInitService.build(requestEntity)
                .customizeRequest()
                .setFollowRedirection(true)
                .commitChanges()
                .connect()
                .delete();
    }

    @Override
    public <T> ResponseWrapper<T> patch(RequestEntity requestEntity) {
        return RequestInitService.build(requestEntity)
                .customizeRequest()
                .setFollowRedirection(true)
                .commitChanges()
                .connect()
                .patch();
    }

}
