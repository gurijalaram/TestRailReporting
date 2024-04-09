package com.apriori.shared.util.http.utils;

import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.entity.UserAuthenticationEntity;
import com.apriori.shared.util.interfaces.EndpointEnum;

@Deprecated
public class RequestEntityUtil_Old {

    private static String token;
    private static String apUserContext;

    @Deprecated
    public static String useTokenForRequests(final String tokenForRequests) {
        return token = tokenForRequests;
    }

    @Deprecated
    public static String useApUserContextForRequests(final UserCredentials userForAppUserContext) {
        return apUserContext = new AuthUserContextUtil().getAuthUserContext(userForAppUserContext.getEmail());
    }

    public static RequestEntity initBuilder(EndpointEnum endpoint, Class<?> returnType) {
        return RequestEntity.builder()
            .endpoint(endpoint)
            .returnType(returnType)
            .token(token)
            .apUserContext(apUserContext)
            .build();
    }

    @Deprecated
    public static RequestEntity init(EndpointEnum endpoint, Class<?> returnType) {
        return new RequestEntity()
            .endpoint(endpoint)
            .returnType(returnType)
            .token(token)
            .apUserContext(apUserContext);
    }

    @Deprecated
    public static RequestEntity init(EndpointEnum endpoint, final UserCredentials userCredentials, Class<?> returnType) {
        return new RequestEntity()
            .userAuthenticationEntity(new UserAuthenticationEntity(userCredentials.getEmail(), userCredentials.getPassword()))
            .returnType(returnType)
            .endpoint(endpoint)
            .token(token)
            .apUserContext(apUserContext);
    }
}
