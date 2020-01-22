package com.apriori.pageobjects.utils;

import com.apriori.apibase.http.builder.common.response.common.AuthenticateJSON;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.utils.constants.Constants;

import java.util.HashMap;

public class APIAuthentication {

    /**
     * Fetch Authorization header for user
     *
     * @param userName - userName of logged user
     * @return Authorization Header
     */
    public HashMap<String, String> initAuthorizationHeader(String userName) {
        return new HashMap<String, String>() {{
                put("Authorization", "Bearer " + authenticateUser(userName,  userName.split("@")[0]));
                put("apriori.tenantgroup", "default");
                put("apriori.tenant", "default");
                put("Content-Type", "application/vnd.apriori.v1+json");
            }};
    }

    private String authenticateUser(String userName, String password) {
        return ((AuthenticateJSON) new HTTPRequest().defaultFormAuthorization(userName, password)
            .customizeRequest()
            .setReturnType(AuthenticateJSON.class)
            .setEndpoint(Constants.getBaseUrl() + "ws/auth/token")
            .setAutoLogin(false)
            .commitChanges()
            .connect()
            .post()).getAccessToken();
    }
}
