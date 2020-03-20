package com.apriori.internalapi.util;

import com.apriori.apibase.http.builder.common.entity.RequestEntity;
import com.apriori.apibase.http.builder.dao.GenericRequestUtil;
import com.apriori.apibase.http.builder.service.RequestAreaCds;
import com.apriori.apibase.utils.ResponseWrapper;

public class CdsTestUtil extends TestUtil {

    protected <T> ResponseWrapper<T> getCommonRequest(String url, boolean urlEncoding, Class klass) {
        return GenericRequestUtil.get(
                RequestEntity.init(url, klass).setUrlEncodingEnabled(urlEncoding),
                new RequestAreaCds()
        );
    }
}
