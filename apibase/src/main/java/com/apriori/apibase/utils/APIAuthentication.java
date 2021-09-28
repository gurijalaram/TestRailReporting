package com.apriori.apibase.utils;

import com.apriori.apibase.enums.BaseAPIEnum;
import com.apriori.apibase.services.response.objects.AuthenticateJSON;
import com.apriori.utils.http.builder.common.entity.RequestEntity;
import com.apriori.utils.http.builder.common.entity.UserAuthenticationEntity;
import com.apriori.utils.http.builder.request.HTTPRequest;
import com.apriori.utils.http.utils.RequestEntityUtil;
import com.apriori.utils.http.utils.ResponseWrapper;

import org.apache.commons.collections4.map.PassiveExpiringMap;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

// TODO ALL: test it
public class APIAuthentication {
    private String accessToken = null;
    private int timeToLive = 0;

    /**
     * Fetch Authorization header for user
     *
     * @return Authorization Header
     */
    public HashMap<String, String> initAuthorizationHeader(String username) {
        return new HashMap<String, String>() {
            {
                put("Authorization", "Bearer " + getCachedToken(username));
                put("apriori.tenantgroup", "default");
                put("apriori.tenant", "default");
                put("Content-Type", "application/vnd.apriori.v1+json");
            }
        };
    }

    /**
     * Fetch Authorization header for user
     *
     * @return Authorization Header
     */
    public HashMap<String, String> initAuthorizationHeaderNoContent(String username) {
        return new HashMap<String, String>() {
            {
                put("Authorization", "Bearer " + getCachedToken(username));
                put("apriori.tenantgroup", "default");
                put("apriori.tenant", "default");
            }
        };
    }

    private String getCachedToken(String username) {
        String password = username.split("@")[0];

        if (accessToken == null && timeToLive < 1) {
            RequestEntity requestEntity = RequestEntityUtil.init(BaseAPIEnum.POST_AUTH_TOKEN, AuthenticateJSON.class)
                .userAuthenticationEntity(new UserAuthenticationEntity(username, password));

            ResponseWrapper<AuthenticateJSON> tokenDetails = HTTPRequest.build(requestEntity).post();

            timeToLive = tokenDetails.getResponseEntity().getExpiresIn();
            accessToken = tokenDetails.getResponseEntity().getAccessToken();
        }

        PassiveExpiringMap<String, String> tokenCache = new PassiveExpiringMap<>(TimeUnit.SECONDS.toMillis(timeToLive));
        tokenCache.put("Token", accessToken);

        return tokenCache.isEmpty() ? tokenCache.put("Token", accessToken) : tokenCache.get("Token");
    }
}