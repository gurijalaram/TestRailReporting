package com.apriori.apibase.utils;

import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.dao.GenericRequestUtil;
import com.apriori.utils.http.builder.service.RequestAreaCds;
import com.apriori.utils.http.utils.ResponseWrapper;

public class CdsTestUtil extends TestUtil {

    protected <T> ResponseWrapper<T> getCommonRequest(String url, boolean urlEncoding, Class klass) {
        return GenericRequestUtil.get(
                RequestEntity.init(url, klass).setUrlEncodingEnabled(urlEncoding),
                new RequestAreaCds()
        );
    }
}
