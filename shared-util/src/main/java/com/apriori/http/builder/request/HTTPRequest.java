package com.apriori.http.builder.request;

import com.apriori.http.builder.entity.RequestEntity;
import com.apriori.interfaces.Request;

import lombok.extern.slf4j.Slf4j;

/**
 * HTTP request builder
 * <p>
 * Entry point to create HTTP request.
 *
 * @version 2.0
 */
@Slf4j
public class HTTPRequest {

    public static Request build(RequestEntity requestEntity) {
        log.debug("Request data:{}", requestEntity);

        return BaseRequestImpl.init(requestEntity);
    }
}
