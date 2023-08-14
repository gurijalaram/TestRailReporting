package com.apriori.utils.http.builder.request;


import com.apriori.utils.http.builder.interfaces.Request;


class BaseRequestImpl implements Request {

    private RequestEntity requestEntity;

    public static BaseRequestImpl init(RequestEntity requestEntity) {
        return new BaseRequestImpl(requestEntity);
    }

    private BaseRequestImpl(RequestEntity requestEntity) {
        this.requestEntity = requestEntity;
    }

    @Override
    public <T> ResponseWrapper<T> get() {
        return initRequestData().get();
    }

    @Override
    public <T> ResponseWrapper<T> post() {
        return initRequestData().post();
    }

    @Override
    public <T> ResponseWrapper<T> getMultipart() {
        return initRequestData().getWithNoLogInfo();
    }

    @Override
    public <T> ResponseWrapper<T> postMultipart() {
        return initRequestData().postMultiPart();
    }

    @Override
    public <T> ResponseWrapper<T> put() {
        return initRequestData().put();
    }

    @Override
    public <T> ResponseWrapper<T> delete() {
        return initRequestData().delete();
    }

    @Override
    public <T> ResponseWrapper<T> patch() {
        return initRequestData().patch();
    }

    private ConnectionManager<?> initRequestData() {
        return new ConnectionManager<>(this.requestEntity, this.requestEntity.returnType());
    }
}
