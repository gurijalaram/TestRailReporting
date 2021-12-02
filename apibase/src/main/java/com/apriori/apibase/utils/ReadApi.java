package com.apriori.apibase.utils;

import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

public interface ReadApi<E extends ExternalEndpointEnum> {
    default <T> ResponseWrapper<T> getCommonRequest(E apiEnum, Class<T> klass, String... inlineVariables) {
        RequestEntity request = RequestEntityUtil.init(apiEnum, klass).inlineVariables(inlineVariables);
        return HTTPRequest.build(request).get();
    }
}
