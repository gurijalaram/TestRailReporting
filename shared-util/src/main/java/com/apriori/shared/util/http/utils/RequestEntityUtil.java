package com.apriori.shared.util.http.utils;

import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.entity.UserAuthenticationEntity;
import com.apriori.shared.util.interfaces.EndpointEnum;

public class RequestEntityUtil {

    private static String token;
    private static String apUserContext;

    public static String useTokenForRequests(final String tokenForRequests) {
        return token = tokenForRequests;
    }

    public static String useApUserContextForRequests(final UserCredentials userForAppUserContext) {
        return apUserContext = new AuthUserContextUtil().getAuthUserContext(userForAppUserContext.getEmail());
    }

    public static RequestEntity.RequestEntityBuilder initBuilder(EndpointEnum endpoint, Class<?> returnType) {
        return RequestEntity.builder()
            .endpoint(endpoint)
            .returnType(returnType)
            .token(token)
            .apUserContext(apUserContext);
    }

    public static RequestEntity init(EndpointEnum endpoint, Class<?> returnType) {
        return new RequestEntity()
            .endpoint(endpoint)
            .returnType(returnType)
            .token(token)
            .apUserContext(apUserContext);
    }

    public static RequestEntity init(EndpointEnum endpoint, final UserCredentials userCredentials, Class<?> returnType) {
        return new RequestEntity()
            .userAuthenticationEntity(new UserAuthenticationEntity(userCredentials.getEmail(), userCredentials.getPassword()))
            .returnType(returnType)
            .endpoint(endpoint)
            .token(token)
            .apUserContext(apUserContext);
    }
}
