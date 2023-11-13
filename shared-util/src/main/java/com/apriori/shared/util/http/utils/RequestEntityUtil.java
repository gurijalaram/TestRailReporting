package com.apriori.shared.util.http.utils;

import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.file.user.UserUtil;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.http.models.entity.UserAuthenticationEntity;
import com.apriori.shared.util.interfaces.EndpointEnum;

public class RequestEntityUtil {

    private UserCredentials userCredentials;
    private String token;
    private String apUserContext;

    public RequestEntityUtil useRandomUser() {
        this.userCredentials = UserUtil.getUser();
        return this;
    }

    public RequestEntityUtil useRandomUser(final String accessLevel) {
        this.userCredentials = UserUtil.getUser(accessLevel);
        return this;
    }

    public RequestEntityUtil useTokenInRequests() {
        this.token = this.getEmbeddedUser().getToken();
        return this;
    }

    public RequestEntityUtil useApUserContextInRequests() {
        this.apUserContext = new AuthUserContextUtil().getAuthUserContext(this.getEmbeddedUser().getEmail());
        return this;
    }

    public UserCredentials getEmbeddedUser() {
        return userCredentials != null ? this.userCredentials : this.useRandomUser().userCredentials;
    }

//    public String useTokenForRequests(final String tokenForRequests) {
//        return token = tokenForRequests;
//    }
//
//    public String useApUserContextForRequests(final UserCredentials userForAppUserContext) {
//        return apUserContext = new AuthUserContextUtil().getAuthUserContext(userForAppUserContext.getEmail());
//    }

    public RequestEntity init(EndpointEnum endpoint, Class<?> returnType) {
        return new RequestEntity()
            .endpoint(endpoint)
            .returnType(returnType)
            .token(token)
            .apUserContext(apUserContext);
    }

    public RequestEntity init(EndpointEnum endpoint, final UserCredentials userCredentials, Class<?> returnType) {
        return new RequestEntity()
            .userAuthenticationEntity(new UserAuthenticationEntity(userCredentials.getEmail(), userCredentials.getPassword()))
            .returnType(returnType)
            .endpoint(endpoint)
            .token(token)
            .apUserContext(apUserContext);
    }
}
