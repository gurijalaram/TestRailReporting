package com.apriori.utils.http2.utils;

import com.apriori.utils.http.builder.common.entity.UserAuthenticationEntity;
import com.apriori.utils.http.enums.EndpointEnum;
import com.apriori.utils.http2.builder.common.entity.RequestEntity;
import com.apriori.utils.properties.PropertiesContext;
import com.apriori.utils.users.UserCredentials;

public class RequestEntityUtil {

    private static String token;

    public static String useTokenForRequests(final String tokenForRequests) {
        return token = tokenForRequests;
    }

    public static RequestEntity.RequestEntityBuilder initBuilder(EndpointEnum endpoint, Class<?> returnType) {
        return RequestEntity.builder()
            .endpoint(endpoint)
            .returnType(returnType)
            .token(token);
    }

    public static RequestEntity init(EndpointEnum endpoint, Class<?> returnType) {
        return new RequestEntity()
            .endpoint(endpoint)
            .returnType(returnType)
            .token(token);
    }

    public static RequestEntity init(EndpointEnum endpoint, final UserCredentials userCredentials, Class<?> returnType) {
        return new RequestEntity()
            .userAuthenticationEntity(new UserAuthenticationEntity(userCredentials.getUsername(), userCredentials.getPassword()))
            .returnType(returnType)
            .endpoint(endpoint)
            .token(token);
    }

    public static RequestEntity initWithApUserContext(EndpointEnum endpoint, Class<?> returnType) {
        return initBuilder(endpoint, returnType)
            .header("ap-user-context", PropertiesContext.getStr("${env}.ap_user_context"))
            .build();
    }
}
