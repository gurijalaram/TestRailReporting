package com.apriori.pageobjects.utils;

import com.apriori.apibase.http.builder.common.response.common.AuthenticateJSON;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.apibase.utils.ResponseWrapper;
import com.apriori.utils.constants.Constants;

import org.apache.commons.collections4.map.PassiveExpiringMap;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class APIAuthentication {

    private String accessToken = null;
    private int timeToLive = 0;

    /**
     * Fetch Authorization header for user
     *
     * @return Authorization Header
     */
    public HashMap<String, String> initAuthorizationHeader(String username) {
        return new HashMap<String, String>() {{
                put("Authorization", "Bearer " + getCachedToken(username));
                put("apriori.tenantgroup", "default");
                put("apriori.tenant", "default");
                put("Content-Type", "application/vnd.apriori.v1+json");
            }};
    }

    private String getCachedToken(String username) {
        String password = username.split("@")[0];

        if (accessToken == null && timeToLive < 1) {
            ResponseWrapper<AuthenticateJSON> tokenDetails =  new HTTPRequest().defaultFormAuthorization(username, password)
                .customizeRequest()
                .setReturnType(AuthenticateJSON.class)
                .setEndpoint(Constants.getBaseUrl() + "ws/auth/token")
                .setAutoLogin(false)
                .commitChanges()
                .connect()
                .post();

            timeToLive = tokenDetails.getResponseEntity().getExpiresIn();
            accessToken = tokenDetails.getResponseEntity().getAccessToken();
        }

        PassiveExpiringMap<String, String> tokenCache = new PassiveExpiringMap<>(TimeUnit.SECONDS.toMillis(timeToLive));
        tokenCache.put("Token", accessToken);

        return tokenCache.isEmpty() ? tokenCache.put("Token", accessToken) : tokenCache.get("Token");
    }
}