package com.apriori.pageobjects.utils;

import com.apriori.apibase.http.builder.common.entity.RequestEntity;
import com.apriori.apibase.http.builder.common.response.common.AuthenticateJSON;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.utils.constants.Constants;

import org.apache.commons.collections4.map.PassiveExpiringMap;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class APIAuthentication {

    private static String accessToken = null;
    private static int expireTime;
    private String username;
    private String password;

    public APIAuthentication(String username) {
        this.username = username;
        this.password = this.username.split("@")[0];
    }

    public RequestEntity requestAuthorisation(String endPoint) {
        return new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(new APIAuthentication(this.username).initAuthorizationHeader())
            .setEndpoint(Constants.getBaseUrl() + endPoint);
    }

    /**
     * Fetch Authorization header for user
     *
     * @return Authorization Header
     */
    private HashMap<String, String> initAuthorizationHeader() {
        return new HashMap<String, String>() {{
                put("Authorization", "Bearer " + getCachedToken());
                put("apriori.tenantgroup", "default");
                put("apriori.tenant", "default");
                put("Content-Type", "application/vnd.apriori.v1+json");
            }};
    }

    private String getAccessToken() {
        return accessToken = accessToken == null
            ? ((AuthenticateJSON) new HTTPRequest().defaultFormAuthorization(this.username, this.password)
            .customizeRequest()
            .setReturnType(AuthenticateJSON.class)
            .setEndpoint(Constants.getBaseUrl() + "ws/auth/token")
            .setAutoLogin(false)
            .commitChanges()
            .connect()
            .post()).getAccessToken()
            : accessToken;
    }

    private int getTimeToLive() {
        return expireTime = expireTime < 1
            ? ((AuthenticateJSON) new HTTPRequest().defaultFormAuthorization(this.username, this.password)
                .customizeRequest()
                .setReturnType(AuthenticateJSON.class)
                .setEndpoint(Constants.getBaseUrl() + "ws/auth/token")
                .setAutoLogin(false)
                .commitChanges()
                .connect()
                .post()).getExpiresIn()
                : expireTime;
    }

    private String getCachedToken() {
        PassiveExpiringMap<String, String> tokenCache = new PassiveExpiringMap<>(TimeUnit.SECONDS.toMillis(getTimeToLive()));
        tokenCache.put("Token", getAccessToken());

        return tokenCache.isEmpty() ? tokenCache.put("Token", getAccessToken()) : tokenCache.get("Token");
    }
}