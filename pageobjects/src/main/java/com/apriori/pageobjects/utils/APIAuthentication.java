package com.apriori.pageobjects.utils;

import com.apriori.apibase.http.builder.common.entity.RequestEntity;
import com.apriori.apibase.http.builder.common.response.common.AuthenticateJSON;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.utils.constants.Constants;

import java.util.HashMap;

public class APIAuthentication {

    private static String accessToken = null;
    private String username;
    private String password;

    public APIAuthentication(String username) {
        this.username = username;
        this.password = this.username.split("@")[0];
    }

    /**
     * Fetch Authorization header for user
     *
     * @return Authorization Header
     */
    private HashMap<String, String> initAuthorizationHeader() {
        return new HashMap<String, String>() {{
                put("Authorization", "Bearer " + getAccessToken());
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

    public RequestEntity requestAuthorisation(String endPoint) {
        return new HTTPRequest()
            .unauthorized()
            .customizeRequest()
            .setHeaders(new APIAuthentication(this.username).initAuthorizationHeader())
            .setEndpoint(Constants.getBaseUrl() + endPoint);
    }
}