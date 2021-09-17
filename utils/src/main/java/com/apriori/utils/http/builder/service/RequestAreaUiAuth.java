package com.apriori.utils.http.builder.service;

import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.interfaces.RequestArea;
import com.apriori.utils.http.utils.ResponseWrapper;
import com.apriori.utils.http.utils.WebDriverUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestAreaUiAuth implements RequestArea {
    private static Map<String, String> authTokens = new ConcurrentHashMap<>();

    @Override
    public <T> ResponseWrapper<T> get(RequestEntity requestEntity) {
        return null;
    }

    @Override
    public <T> ResponseWrapper<T> post(RequestEntity requestEntity) {
        return null;
    }

    @Override
    public <T> ResponseWrapper<T> postMultipart(RequestEntity requestEntity) {
        return null;
    }

    @Override
    public <T> ResponseWrapper<T> put(RequestEntity requestEntity) {
        return null;
    }

    @Override
    public <T> ResponseWrapper<T> delete(RequestEntity requestEntity) {
        return null;
    }

    @Override
    public <T> ResponseWrapper<T> patch(RequestEntity requestEntity) {
        return null;
    }
}
