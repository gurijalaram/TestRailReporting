package com.apriori.acs.utils;

import com.apriori.acs.enums.acs.AcsApiEnum;
import com.apriori.http.models.entity.RequestEntity;
import com.apriori.http.models.request.HTTPRequest;
import com.apriori.http.utils.RequestEntityUtil;
import com.apriori.http.utils.ResponseWrapper;

import org.apache.commons.collections4.map.PassiveExpiringMap;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

// TODO: 25/09/2023 cn - mark for deletion
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
    /*public HashMap<String, String> initAuthorizationHeaderNoContent() {
        return new HashMap<String, String>() {
            {
                put("Authorization", "Bearer " + new AuthorizationUtil().getTokenAsString());
                put("apriori.tenantgroup", "default");
                put("apriori.tenant", "default");
            }
        };
    }*/

    public String getTokenSingular(String username) {
        return getCachedToken(username);
    }

    private String getCachedToken(String username) {
        String password = username.split("@")[0];

        if (accessToken == null && timeToLive < 1) {
            RequestEntity requestEntity = RequestEntityUtil.initBuilder(AcsApiEnum.POST_AUTH_TOKEN, AuthenticateJSON.class)
                .xwwwwFormUrlEncoded(AuthorizationFormUtil.getDefaultAuthorizationForm(username,password))
                .build();

            ResponseWrapper<AuthenticateJSON> tokenDetails = HTTPRequest.build(requestEntity).post();

            timeToLive = tokenDetails.getResponseEntity().getExpiresIn();
            accessToken = tokenDetails.getResponseEntity().getAccessToken();
        }

        PassiveExpiringMap<String, String> tokenCache = new PassiveExpiringMap<>(TimeUnit.SECONDS.toMillis(timeToLive));
        tokenCache.put("Token", accessToken);

        return tokenCache.isEmpty() ? tokenCache.put("Token", accessToken) : tokenCache.get("Token");
    }
}