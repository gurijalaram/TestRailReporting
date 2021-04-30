package com.apriori.utils.http2.utils;

import com.apriori.utils.http.builder.common.entity.UserAuthenticationEntity;
import com.apriori.utils.http.enums.EndpointEnum;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.users.UserCredentials;

public class RequestEntityUtil {

    public static RequestEntity demoInit(EndpointEnum endpoint, Class<?> returnType) {
        return new RequestEntity()
            .endpoint(endpoint)
            .returnType(returnType);
    }

    public static RequestEntity init(EndpointEnum endpoint, Class<?> returnType) {
        return new RequestEntity()
            .endpoint(endpoint)
            .returnType(returnType);
    }

    public static RequestEntity init(EndpointEnum endpoint, final UserCredentials userCredentials, Class<?> returnType) {
        return new RequestEntity()
            .userAuthenticationEntity(new UserAuthenticationEntity(userCredentials.getUsername(), userCredentials.getPassword()))
            .returnType(returnType)
            .endpoint(endpoint);
    }
}
