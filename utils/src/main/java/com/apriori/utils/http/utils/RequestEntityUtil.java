package com.apriori.utils.http.utils;

import com.apriori.utils.authusercontext.AuthUserContextUtil;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.common.entity.UserAuthenticationEntity;
import com.apriori.utils.http.enums.EndpointEnum;
import com.apriori.utils.reader.file.user.UserCredentials;
import com.apriori.utils.reader.file.user.UserUtil;

import java.util.HashMap;
import java.util.Map;

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
            .headers(initApUserContext());
    }

    public static RequestEntity init(EndpointEnum endpoint, Class<?> returnType) {
        return new RequestEntity()
            .endpoint(endpoint)
            .returnType(returnType)
            .token(token)
            .headers(initApUserContext());
    }


    public static RequestEntity init(EndpointEnum endpoint, final UserCredentials userCredentials, Class<?> returnType) {
        return new RequestEntity()
            .userAuthenticationEntity(new UserAuthenticationEntity(userCredentials.getEmail(), userCredentials.getPassword()))
            .returnType(returnType)
            .endpoint(endpoint)
            .token(token)
            .headers(initApUserContext());
    }

    private static Map<String, String> initApUserContext() {
        if (apUserContext == null) {
            return null;
        }

        return new HashMap<String, String>() {{
                put("ap-user-context", apUserContext);
            }
        };
    }

    //TODO z: will be removed after VDS update
    public static RequestEntity initWithApUserContext(EndpointEnum endpoint, Class<?> returnType) {
        return initBuilder(endpoint, returnType)
            .header("ap-user-context",
                apUserContext != null ? apUserContext : new AuthUserContextUtil().getAuthUserContext(UserUtil.getUser().getEmail()))
            .build();
    }
}
