package com.apriori.shared.util.http.utils;

import com.apriori.shared.util.file.user.UserCredentials;
import com.apriori.shared.util.http.models.entity.RequestEntity;
import com.apriori.shared.util.interfaces.EndpointEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestEntityUtil {

    private final UserCredentials userCredentials;
    private String token;
    private String apUserContext;

    public RequestEntityUtil(final UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
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
     * Automatically insert token of initialized user
     * into all requests initialized by the current RequestEntityUtil object
     * @return current RequestEntityUtil object
     */
    public RequestEntityUtil useCustomTokenInRequests(final String token) {
        this.token = token;
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
        this.validateIsUserPresenceThrowExceptionIfNot();
        return this.userCredentials;
    }

    private void validateIsUserPresenceThrowExceptionIfNot() {
        if (this.userCredentials == null) {
            final String error = "User for the request was not initialized. Use RequestEntityUtilBuilder to initialize user.";
            log.error(error);
            throw new IllegalArgumentException(error);
        }
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
    public RequestEntity init(final UserCredentials userCredentials, EndpointEnum endpoint, Class<?> returnType) {
        return new RequestEntity()
            .returnType(returnType)
            .endpoint(endpoint)
            .token(token != null ? userCredentials.getToken() : null)
            .apUserContext(apUserContext != null ? new AuthUserContextUtil().getAuthUserContext(userCredentials.getEmail()) : null);
    }
}
