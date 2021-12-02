package com.apriori.apibase.utils;

import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.enums.common.ExternalEndpointEnum;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

/**
 * Represents an api that contains delete features.
 */
public interface DeleteApi<E extends ExternalEndpointEnum> {
    /**
     * Calls the delete method
     *
     * @return The response of what was deleted
     */
    default ResponseWrapper<String> delete(E apiEnum, String... inlineVariables) {
        return HTTPRequest.build(RequestEntityUtil.init(apiEnum, null).inlineVariables(inlineVariables)).delete();
    }
}
