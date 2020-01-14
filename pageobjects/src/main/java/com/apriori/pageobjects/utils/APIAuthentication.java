package com.apriori.pageobjects.utils;

import com.apriori.apibase.http.builder.common.response.common.AuthenticateJSON;
import com.apriori.apibase.http.builder.service.HTTPRequest;
import com.apriori.utils.constants.Constants;
import com.apriori.utils.users.UserUtil;

import java.util.HashMap;

public class APIAuthentication {

    private final String apiUsername = UserUtil.currentUser.getUsername();
    private final String apiPassword = apiUsername.split("@")[0];

    public HashMap<String, String> initAuthorizationHeader() {
        return new HashMap<String, String>() {{
                put("Authorization", "Bearer " + authenticateUser(apiUsername, apiPassword));
                put("apriori.tenantgroup", "default");
                put("apriori.tenant", "default");
                put("Content-Type", "application/vnd.apriori.v1+json");
            }};
    }

    public String authenticateUser(String username, String password) {
        return ((AuthenticateJSON) new HTTPRequest().defaultFormAuthorization(username, password)
            .customizeRequest()
            .setReturnType(AuthenticateJSON.class)
            .setEndpoint(Constants.getBaseUrl() + "ws/auth/token")
            .setAutoLogin(false)
            .commitChanges()
            .connect()
            .post()).getAccessToken();
    }
}
