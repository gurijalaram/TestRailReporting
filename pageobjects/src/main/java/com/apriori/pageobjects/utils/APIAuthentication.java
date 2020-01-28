package com.apriori.pageobjects.utils;

import com.apriori.apibase.http.builder.common.response.common.AuthenticateJSON;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.utils.constants.Constants;

import java.util.HashMap;

public class APIAuthentication {

    private String accessToken = null;
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
    public HashMap<String, String> initAuthorizationHeader() {
        return new HashMap<String, String>() {{
                put("Authorization", "Bearer " + authenticateUser());
                put("apriori.tenantgroup", "default");
                put("apriori.tenant", "default");
                put("Content-Type", "application/vnd.apriori.v1+json");
            }};
    }

    private String authenticateUser() {
        if (accessToken == null) {
            accessToken = ((AuthenticateJSON) new HTTPRequest().defaultFormAuthorization(this.username, this.password)
                .customizeRequest()
                .setReturnType(AuthenticateJSON.class)
                .setEndpoint(Constants.getBaseUrl() + "ws/auth/token")
                .setAutoLogin(false)
                .commitChanges()
                .connect()
                .post()).getAccessToken();
        }
        return accessToken;
    }
}
