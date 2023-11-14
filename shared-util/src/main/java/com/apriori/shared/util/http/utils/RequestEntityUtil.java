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


    /**
     * Use a random user received by {@link UserUtil#getUser()}
     * in all requests initialized by the current RequestEntityUtil object
     * @return current RequestEntityUtil object
     */
    public RequestEntityUtil useRandomUser() {
        this.userCredentials = UserUtil.getUser();
        return this;
    }

    /**
     * Use a random user received by {@link UserUtil#getUser(String)}
     * in all requests initialized by the current RequestEntityUtil object
     * @return current RequestEntityUtil object
     */
    public RequestEntityUtil useRandomUser(final String accessLevel) {
        this.userCredentials = UserUtil.getUser(accessLevel);
        return this;
    }

    /**
     * Use a custom user
     * in all requests initialized by the current RequestEntityUtil object
     * @return current RequestEntityUtil object
     */
    public RequestEntityUtil useCustomUser(final UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
        return this;
    }

    /**
     * Automatically insert token of initialized user
     * into all requests initialized by the current RequestEntityUtil object
     * @return current RequestEntityUtil object
     */
    public RequestEntityUtil useTokenInRequests() {
        this.token = this.getEmbeddedUser().getToken();
        return this;
    }

    /**
     * Automatically insert apUserContext of initialized user
     * into all requests initialized by the current RequestEntityUtil object
     * @return current RequestEntityUtil object
     */
    public RequestEntityUtil useApUserContextInRequests() {
        this.apUserContext = new AuthUserContextUtil().getAuthUserContext(this.getEmbeddedUser().getEmail());
        return this;
    }

    /**
     * Get the current embedded user, used in the requests.
     * @return
     */
    public UserCredentials getEmbeddedUser() {
        return userCredentials != null ? this.userCredentials : this.useRandomUser().userCredentials;
    }


    /**
     * Init HTTP request with a RequestEntityUtil configurations and embedded user
     * @param endpoint
     * @param returnType
     * @return
     */
    public RequestEntity init(EndpointEnum endpoint, Class<?> returnType) {
        return new RequestEntity()
            .endpoint(endpoint)
            .returnType(returnType)
            .token(token)
            .apUserContext(apUserContext);
    }

    /**
     * Init HTTP request with a RequestEntityUtil configurations and another, custom user.
     * @param endpoint
     * @param returnType
     * @return
     */
    public RequestEntity init( final UserCredentials userCredentials, EndpointEnum endpoint, Class<?> returnType) {
        return new RequestEntity()
            .returnType(returnType)
            .endpoint(endpoint)
            .token(token != null ? userCredentials.getToken() : null)
            .apUserContext(apUserContext != null ? new AuthUserContextUtil().getAuthUserContext(userCredentials.getEmail()) : null);
    }
}
