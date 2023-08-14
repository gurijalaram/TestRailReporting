package com.apriori.utils.http.builder.request;


import com.apriori.utils.http.builder.interfaces.Request;

import lombok.extern.slf4j.Slf4j;

/**
 * HTTP request builder
 *
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
