package com.apriori.apibase.utils;

import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaApi;
import com.apriori.utils.http.utils.ResponseWrapper;

import java.util.Map;

public class CommonRequestUtil extends TestUtil {

    public  <T> ResponseWrapper<T> getCommonRequest(String url, boolean urlEncoding, Class klass) {
        return GenericRequestUtil.get(
                RequestEntity.init(url, klass).setUrlEncodingEnabled(urlEncoding),
                new RequestAreaApi()
        );
    }

    public  <T> ResponseWrapper<T> getCommonRequest(String url, boolean urlEncoding, Class klass, Map<String, String> headers) {
        return GenericRequestUtil.get(
            RequestEntity.init(url, klass).setUrlEncodingEnabled(urlEncoding)
            .setHeaders(headers),
            new RequestAreaApi()
        );
    }

    public  <T> ResponseWrapper<T> postCommonRequest(String url, boolean urlEncoding, Class klass) {
        return GenericRequestUtil.post(
                RequestEntity.init(url, klass).setUrlEncodingEnabled(urlEncoding),
                new RequestAreaApi()
        );
    }

    public  <T> ResponseWrapper<T> patchCommonRequest(String url, boolean urlEncoding, Class klass) {
        return GenericRequestUtil.patch(
                RequestEntity.init(url, klass).setUrlEncodingEnabled(urlEncoding),
                new RequestAreaApi()
        );
    }
}