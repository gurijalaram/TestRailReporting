package com.apriori.utils.http2.builder.service;

import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.http2.builder.interfaces.Request;

import lombok.extern.slf4j.Slf4j;

/**
 * HTTP request builder
 *
 * Entry point to create HTTP request.
 *
 * @version 2.0
 */
@Slf4j
public class HTTP2Request {

    public static Request build(RequestEntity requestEntity) {
        log.debug("Request data:\n{}", requestEntity);

        return BaseRequestImpl.init(requestEntity);
    }
}
